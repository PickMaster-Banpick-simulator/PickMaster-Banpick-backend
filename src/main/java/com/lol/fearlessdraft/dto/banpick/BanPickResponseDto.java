package com.lol.fearlessdraft.dto.banpick;

import com.lol.fearlessdraft.entity.BanPickActionType;

import lombok.Builder;

@Builder
public record BanPickResponseDto(
        Long teamId,
        String teamName,
        String championName,
        BanPickActionType actionType,
        int turnOrder
) {}
