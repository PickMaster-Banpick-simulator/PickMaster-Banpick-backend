package com.lol.fearlessdraft.dto.match;

import com.lol.fearlessdraft.entity.Match;
import com.lol.fearlessdraft.entity.MatchType;



public record MatchResponseDto(
    Long matchId,
    String matchName,
    Integer numberOfGames,
    String teamAName,
    String teamBName,
      MatchType matchType,
    boolean allowSpectators

) {
    public static MatchResponseDto from(Match match) {
        return new MatchResponseDto(
                match.getId(),
                match.getMatchName(),
                match.getNumberOfGames(),
                match.getTeamA().getTeamName(),
                match.getTeamB().getTeamName(),
                match.getMatchType(),
                match.isAllowSpectators()
        );
    }
}
