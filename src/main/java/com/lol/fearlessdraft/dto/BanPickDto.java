package com.lol.fearlessdraft.dto;

import com.lol.fearlessdraft.entity.BanPickState;
import lombok.Data;
import lombok.Value;

public class BanPickDto {

    @Data
    public static class SelectRequest {
        private String team;
        private String action; // "ban" or "pick"
        private ChampionDto champion;
    }

    @Value
    public static class StateResponse {
        int turnIndex;
        String currentTurn;
        List<ChampionDto> blueBans;
        List<ChampionDto> redBans;
        List<ChampionDto> bluePicks;
        List<ChampionDto> redPicks;
        boolean isBanPickFinished;

        public static StateResponse from(BanPickState state) {
            return new StateResponse(
                    state.getTurnIndex(),
                    state.getCurrentTurn(),
                    state.getBlueBans().stream().map(ChampionDto::from).collect(Collectors.toList()),
                    state.getRedBans().stream().map(ChampionDto::from).collect(Collectors.toList()),
                    state.getBluePicks().stream().map(ChampionDto::from).collect(Collectors.toList()),
                    state.getRedPicks().stream().map(ChampionDto::from).collect(Collectors.toList()),
                    state.isBanPickFinished()
            );
        }
    }
}
