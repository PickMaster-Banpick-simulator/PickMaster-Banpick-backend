package com.lol.fearlessdraft.service;

import com.lol.fearlessdraft.entity.Game;
import com.lol.fearlessdraft.entity.Match;
import com.lol.fearlessdraft.entity.Team;
import com.lol.fearlessdraft.repository.GameRepository;
import com.lol.fearlessdraft.repository.MatchRepository;
import com.lol.fearlessdraft.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GameService {


    private final GameRepository gameRepository;
    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    public GameService(GameRepository gameRepository, MatchRepository matchRepository, TeamRepository teamRepository) {
        this.gameRepository = gameRepository;
        this.matchRepository = matchRepository;
        this.teamRepository = teamRepository;
    }
    @Transactional
    public Game createGameForMatch(Long matchId, Long gameOrder) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found"));
        Long nextGameOrder = gameRepository.findMaxGameOrderByMatchId(matchId)
                .map(order -> order + 1)
                .orElse(1L); // 첫 번째 게임이면 1L부터 시작
        Game game = Game.builder()
                .match(match)
                .gameOrder(gameOrder)
                .blueTeam(match.getTeamA())  // 예시: 고정으로 TeamA가 블루
                .redTeam(match.getTeamB())   // 고정으로 TeamB가 레드
                .build();

        return gameRepository.save(game);
    }

    @Transactional
    public void recordGameResult(Long gameId, Long winnerTeamId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Game not found"));

        Match match = game.getMatch();
        Team winner = teamRepository.findById(winnerTeamId)
                .orElseThrow(() -> new IllegalArgumentException("Winner team not found"));

        match.recordGameResult(winner); // Match 엔티티에서 승리 수 처리

        if (match.isMatchOver()) {
            // 시리즈 종료 처리 (DB 저장 또는 WebSocket 알림 가능)
            System.out.println("시리즈 종료: 최종 승자 - " + (match.getTeamAWins() > match.getTeamBWins() ? "A팀" : "B팀"));
        }

        matchRepository.save(match);
    }

}
