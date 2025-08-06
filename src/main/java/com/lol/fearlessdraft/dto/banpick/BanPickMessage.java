package com.lol.fearlessdraft.dto.banpick;

import com.lol.fearlessdraft.entity.BanPickActionType;
import lombok.Builder;


public record BanPickMessage(


        Long matchId,
        Long gameId,
        BanPickActionType actionType, // "BAN" or "PICK"
        String championName,
        String teamName,
        int turnOrder
) {
}
