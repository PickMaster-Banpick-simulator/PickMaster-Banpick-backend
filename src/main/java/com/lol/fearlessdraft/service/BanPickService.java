package com.lol.fearlessdraft.service;

import com.lol.fearlessdraft.common.BanPickTurnFactory;
import com.lol.fearlessdraft.dto.banpick.BanPickMessage;
import com.lol.fearlessdraft.dto.banpick.BanPickResponseDto;
import com.lol.fearlessdraft.entity.*;
import com.lol.fearlessdraft.repository.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;




@Service
public class BanPickService {

    private final GameRepository gameRepository;
    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final PickBanRepository pickBanRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final BanPickTurnFactory banPickTurnFactory;
    private final BanPickTurnRepository banPickTurnRepository;

    public BanPickService(
            TeamRepository teamRepository,
            MatchRepository matchRepository,
            GameRepository gameRepository,
            PickBanRepository pickBanRepository,
            SimpMessagingTemplate messagingTemplate,
            BanPickTurnFactory banPickTurnFactory,
            BanPickTurnRepository banPickTurnRepository) {
        this.teamRepository = teamRepository;
        this.matchRepository = matchRepository;
        this.gameRepository = gameRepository;
        this.pickBanRepository = pickBanRepository;
        this.messagingTemplate = messagingTemplate;
        this.banPickTurnFactory = banPickTurnFactory;
        this.banPickTurnRepository = banPickTurnRepository;
    }

    @Transactional
    public void processBanPickSelection(BanPickMessage message) {
        Game game = gameRepository.findById(message.gameId())
                .orElseThrow(() -> new IllegalArgumentException("게임을 찾을 수 없습니다. id=" + message.gameId()));

        Team team = teamRepository.findByTeamName(message.teamName())
                .orElseThrow(() -> new IllegalArgumentException("팀을 찾을 수 없습니다. name=" + message.teamName()));

        Match match = game.getMatch();
        Long matchId = match.getId();
        Long currentGameNumber = game.getGameOrder();
        Long teamId = team.getId();
        Long opponentTeamId = getOpponentTeamId(match, team);

        // 하드피어리스 룰 적용: 이전 경기에서 팀과 상대팀의 PICK, BAN 챔피언 모두 중복 제한
        Set<String> myTeamRestrictedChampions = getPreviousPicksAndBans(matchId, currentGameNumber, teamId);
        Set<String> opponentTeamRestrictedChampions = getPreviousPicksAndBans(matchId, currentGameNumber, opponentTeamId);

        if (message.actionType() == BanPickActionType.PICK || message.actionType() == BanPickActionType.BAN) {
            if (myTeamRestrictedChampions.contains(message.championName())) {
                throw new IllegalArgumentException("이 챔피언은 이전 게임에서 이미 픽 또는 밴하여 선택할 수 없습니다.");
            }
            if (opponentTeamRestrictedChampions.contains(message.championName())) {
                throw new IllegalArgumentException("상대 팀이 이전 게임에서 픽 또는 밴한 챔피언은 선택할 수 없습니다.");
            }
        }

        // 표준 밴픽 턴 검증
        List<BanPickTurn> turns = banPickTurnFactory.createStandardTurns();

        BanPickTurn expectedTurn = turns.stream()
                .filter(t -> t.getTurnOrder() == message.turnOrder())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 턴입니다."));

        if (!expectedTurn.getTeamCode().equals(team.getTeamCode()) ||
                expectedTurn.getActionType() != message.actionType()) {
            throw new IllegalArgumentException("현재 턴과 일치하지 않는 팀 또는 액션입니다.");
        }

        PickBan pickBan = PickBan.builder()
                .game(game)
                .team(team)
                .championName(message.championName())
                .type(message.actionType())
                .turnOrder(message.turnOrder())
                .build();

        pickBanRepository.save(pickBan);

        List<BanPickResponseDto> progress = getBanPickHistoryByGame(game);

        messagingTemplate.convertAndSend("/topic/banpick/progress/" + game.getId(), progress);
    }

    @Transactional(readOnly = true)
    public List<BanPickResponseDto> getBanPickHistoryByGame(Game game) {
        List<PickBan> picks = pickBanRepository.findAllByGameOrderByTurnOrderAsc(game);

        return picks.stream().map(pick ->
                BanPickResponseDto.builder()
                        .teamId(pick.getTeam().getId())
                        .teamName(pick.getTeam().getTeamName())
                        .championName(pick.getChampionName())
                        .actionType(pick.getType())
                        .turnOrder(pick.getTurnOrder())
                        .build()
        ).toList();
    }

    @Transactional(readOnly = true)
    public List<BanPickResponseDto> getBanPickHistoryByGameId(Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("게임을 찾을 수 없습니다. id=" + gameId));
        return getBanPickHistoryByGame(game);
    }

    @Transactional(readOnly = true)
    public boolean isBanPickCompleted(Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("게임을 찾을 수 없습니다. id=" + gameId));
        long count = pickBanRepository.countByGame(game);
        return count >= banPickTurnFactory.createStandardTurns().size();
    }

    /**
     * 이전 경기에서 해당 팀이 PICK 또는 BAN 한 챔피언 목록 반환
     * @param matchId 현재 매치 ID
     * @param currentGameNumber 현재 경기 순서
     * @param teamId 팀 ID
     * @return 제한 대상 챔피언 이름 집합
     */
    private Set<String> getPreviousPicksAndBans(Long matchId, Long currentGameNumber, Long teamId) {
        List<PickBan> previousRecords = pickBanRepository.findByMatchIdAndTeamIdAndGameNumberLessThanAndTypeIn(
                matchId,
                teamId,
                currentGameNumber,
                List.of(BanPickActionType.PICK, BanPickActionType.BAN)
        );

        return previousRecords.stream()
                .map(PickBan::getChampionName)
                .collect(Collectors.toSet());
    }

    /**
     * 상대 팀 ID 조회 - Match 엔티티에 teamA, teamB가 있으므로 그걸 활용
     */
    private Long getOpponentTeamId(Match match, Team myTeam) {
        if (match.getTeamA().getId().equals(myTeam.getId())) {
            return match.getTeamB().getId();
        } else if (match.getTeamB().getId().equals(myTeam.getId())) {
            return match.getTeamA().getId();
        } else {
            throw new IllegalArgumentException("팀이 매치에 속해있지 않습니다.");
        }
    }
}
