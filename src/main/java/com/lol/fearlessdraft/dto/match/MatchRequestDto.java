package com.lol.fearlessdraft.dto.match;

import com.lol.fearlessdraft.entity.MatchType;

public record MatchRequestDto(
        Long teamAId,
        Long teamBId,
        String matchName,
        String matchPassword,
        Integer numberOfGames,
        MatchType matchType,
        boolean allowSpectators

) {
}
