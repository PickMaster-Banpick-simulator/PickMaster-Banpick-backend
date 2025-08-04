package com.lol.fearlessdraft.dto.banpick;

import com.lol.fearlessdraft.entity.Type;

public record BanPickResponse(

        Long matchId,
        Long gameId,
        String phase, // e.g., "BAN_PHASE_1", "PICK_PHASE_2" 등
        Type actionType, // "BAN" or "PICK"
        String championName,
        String teamName,
        String nextTurnTeamName // 다음 밴픽할 팀

) {
}
