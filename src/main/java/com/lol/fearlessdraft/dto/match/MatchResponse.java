package com.lol.fearlessdraft.dto.match;

import com.lol.fearlessdraft.entity.BanPickState;
import com.lol.fearlessdraft.entity.DraftMatch;
import com.lol.fearlessdraft.entity.enums.MatchType;

public record MatchResponse(
        Long matchId,
        MatchType matchType,
        String blueTeamName,
        String redTeamName,
        int blueWins,
        int redWins,
        BanPickState banPick
) {
    public static MatchResponse of(DraftMatch match, BanPickState banPickState) {
        return new MatchResponse(
                match.getId(),
                match.getMatchType(),
                match.getBlueTeamName(),
                match.getRedTeamName(),
                match.getBlueWins(),
                match.getRedTeamWins(),
                banPickState
        );
    }
}
