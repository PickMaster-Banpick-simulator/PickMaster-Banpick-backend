package com.lol.fearlessdraft.dto.banpick;

import com.lol.fearlessdraft.entity.BanPickActionType;

import com.lol.fearlessdraft.entity.PickBan;



public record BanPickResponseDto(
        Long teamId,
        String teamName,
        String championName,
        BanPickActionType actionType,
        int turnOrder
) {
    public static  BanPickResponseDto of( PickBan pickBan ) {
     return  new BanPickResponseDto(pickBan.getId(),
                                    pickBan.getTeam().getTeamName(),
                                    pickBan.getChampionName(),
                                    pickBan.getType(),
                                   pickBan.getTurnOrder()

     ) ;
    }



}
