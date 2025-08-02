package com.lol.fearlessdraft.dto.match;

import com.lol.fearlessdraft.entity.MatchType;
import lombok.Builder;

@Builder
public record MatchResponseDto(
    Long matchId,
    String matchName,
    Integer numberOfGames,
    String teamAName,
    String teamBName,
      MatchType matchType,
    boolean allowSpectators

) {
}
