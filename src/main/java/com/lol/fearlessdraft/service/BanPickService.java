package com.lol.fearlessdraft.service;

import com.lol.fearlessdraft.entity.Game;
import com.lol.fearlessdraft.entity.PickBan;
import com.lol.fearlessdraft.repository.GameRepository;
import com.lol.fearlessdraft.repository.MatchRepository;
import com.lol.fearlessdraft.repository.PickBanRepository;
import com.lol.fearlessdraft.repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BanPickService {


    private final GameRepository gameRepository;
    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final PickBanRepository pickBanRepository;
    public BanPickService(TeamRepository teamRepository, MatchRepository matchRepository, GameRepository gameRepository, PickBanRepository pickBanRepository) {
        this.teamRepository = teamRepository;
        this.matchRepository = matchRepository;
        this.gameRepository = gameRepository;
        this.pickBanRepository = pickBanRepository;
    }

    public List<BanPickResponseDto> getBanPickHistoryByGame(Game game) {
        List<PickBan> picks = PickBanRepository.findAllByGameOrderByTurnOrderAsc(game);

        return picks.stream().map(pick ->
                BanPickResponseDto.builder()
                        .teamId(pick.getTeam().getId())
                        .teamName(pick.getTeam().getTeamName())
                        .championId(pick.getChampionId())
                        .actionType(pick.getActionType())
                        .turnOrder(pick.getTurnOrder())
                        .build()
        ).toList();
    }

}
