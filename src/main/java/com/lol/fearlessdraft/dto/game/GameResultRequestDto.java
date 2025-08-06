package com.lol.fearlessdraft.dto.game;

public record GameResultRequestDto(
        Long gameId,
        Long winnerTeamId
) {
}
