package com.lol.fearlessdraft.dto.banpick;

import com.lol.fearlessdraft.entity.Type;

public record BanPickMessage(


        Long matchId,
        Long gameId,
        Type actionType, // "BAN" or "PICK"
        String championName,
        String teamName
) {
}
