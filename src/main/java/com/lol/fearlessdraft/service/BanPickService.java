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

        // ✅ Fearless Draft 룰 적용 (PICK일 경우만 체크)
        if (message.actionType() ==  BanPickActionType.PICK) {
            Long matchId = game.getMatch().getId();
            Long currentGameNumber = game.getGameOrder();
            Set<String> previousPicks = getPreviousPicks(matchId, currentGameNumber, team.getId());

            if (previousPicks.contains(message.championName())) {
                throw new IllegalArgumentException("이 챔피언은 이전 게임에서 이미 픽하여 선택할 수 없습니다.");
            }
        }

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

    public Set<String> getPreviousPicks(Long matchId, Long currentGameNumber, Long teamId) {
        List<PickBan> previousPicks = pickBanRepository
                .findByMatchIdAndTeamIdAndGameNumberLessThanAndType(
                        matchId, teamId, currentGameNumber, BanPickActionType.PICK);

        return previousPicks.stream()
                .map(PickBan::getChampionName)
                .collect(Collectors.toSet());
    }
}
