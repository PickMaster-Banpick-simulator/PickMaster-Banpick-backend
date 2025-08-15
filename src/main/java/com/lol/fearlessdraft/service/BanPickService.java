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
    private final TeamRepository teamRepository;
    private final PickBanRepository pickBanRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final BanPickTurnFactory banPickTurnFactory;


    public BanPickService(
            TeamRepository teamRepository,

            GameRepository gameRepository,
            PickBanRepository pickBanRepository,
            SimpMessagingTemplate messagingTemplate,
            BanPickTurnFactory banPickTurnFactory) {
        this.teamRepository = teamRepository;
        this.gameRepository = gameRepository;
        this.pickBanRepository = pickBanRepository;
        this.messagingTemplate = messagingTemplate;
        this.banPickTurnFactory = banPickTurnFactory;

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

        BanRule banRule = match.getBanRule(); // 매치별 밴픽 룰

        // 이전 게임에서의 픽/밴 챔피언 목록 조회
        Set<String> myTeamRestrictedChampions = getPreviousPicksAndBans(matchId, currentGameNumber, teamId);
        Set<String> opponentTeamRestrictedChampions = getPreviousPicksAndBans(matchId, currentGameNumber, opponentTeamId);

        // 룰에 따른 챔피언 선택 제한
        validateChampionSelection(message, banRule, myTeamRestrictedChampions, opponentTeamRestrictedChampions);

        // 표준 밴픽 턴 검증
        validateTurn(message, team);

        // 저장
        PickBan pickBan = PickBan.builder()
                .game(game)
                .team(team)
                .championName(message.championName())
                .type(message.actionType())
                .turnOrder(message.turnOrder())
                .build();

        pickBanRepository.save(pickBan);

        // 진행 상황 전송
        List<BanPickResponseDto> progress = getBanPickHistoryByGame(game);
        messagingTemplate.convertAndSend("/topic/banpick/progress/" + game.getId(), progress);
    }

    private void validateChampionSelection(
            BanPickMessage message,
            BanRule banRule,
            Set<String> myTeamRestricted,
            Set<String> opponentRestricted
    ) {
        if (message.actionType() != BanPickActionType.PICK && message.actionType() != BanPickActionType.BAN) {
            return; // PICK/BAN이 아닐 경우 제한 없음
        }

        if (myTeamRestricted.contains(message.championName())) {
            throw new IllegalArgumentException("이 챔피언은 이전 게임에서 이미 픽 또는 밴하여 선택할 수 없습니다.");
        }

        if (banRule == BanRule.HARDFEARLESS &&
                opponentRestricted.contains(message.championName())) {
            throw new IllegalArgumentException("상대 팀이 이전 게임에서 픽 또는 밴한 챔피언은 선택할 수 없습니다.");
        }
    }

    private void validateTurn(BanPickMessage message, Team team) {
        List<BanPickTurn> turns = banPickTurnFactory.createStandardTurns();

        BanPickTurn expectedTurn = turns.stream()
                .filter(t -> t.getTurnOrder() == message.turnOrder())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 턴입니다."));

        if (!expectedTurn.getTeamCode().equals(team.getTeamCode()) ||
                expectedTurn.getActionType() != message.actionType()) {
            throw new IllegalArgumentException("현재 턴과 일치하지 않는 팀 또는 액션입니다.");
        }
    }

        @Transactional(readOnly = true)
    public List<BanPickResponseDto> getBanPickHistoryByGame(Game game) {
        return pickBanRepository.findAllByGameOrderByTurnOrderAsc(game)
                .stream()
                .map(BanPickResponseDto::of
                )
                .toList();
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

    private Set<String> getPreviousPicksAndBans(Long matchId, Long currentGameNumber, Long teamId) {
        return pickBanRepository.findByMatchIdAndTeamIdAndGameNumberLessThanAndTypeIn(
                        matchId,
                        teamId,
                        currentGameNumber,
                        List.of(BanPickActionType.PICK, BanPickActionType.BAN)
                ).stream()
                .map(PickBan::getChampionName)
                .collect(Collectors.toSet());
    }

    private Long getOpponentTeamId(Match match, Team myTeam) {
        if (match.getTeamA().getId().equals(myTeam.getId())) {
            return match.getTeamB().getId();
        } else if (match.getTeamB().getId().equals(myTeam.getId())) {
            return match.getTeamA().getId();
        }
        throw new IllegalArgumentException("팀이 매치에 속해있지 않습니다.");
    }
}
