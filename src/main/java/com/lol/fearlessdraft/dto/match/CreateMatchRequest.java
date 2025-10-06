package com.lol.fearlessdraft.dto.match;

import com.lol.fearlessdraft.entity.enums.MatchType;

public record CreateMatchRequest(
        String roomId,
        MatchType matchType,
        String blueTeamName,
        String redTeamName
) {
}
