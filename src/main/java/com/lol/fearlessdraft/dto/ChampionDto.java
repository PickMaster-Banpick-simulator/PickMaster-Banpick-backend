package com.lol.fearlessdraft.dto;

import com.lol.fearlessdraft.entity.Champion;
import lombok.Value;

@Value
public class ChampionDto {

    String id;
    String name;
    String image;

    public static ChampionDto from(Champion champion) {
        if (champion == null) return null;
        return new ChampionDto(champion.getId(), champion.getName(), champion.getImage());
    }

    public Champion toModel() {
        return new Champion(this.id, this.name, this.image);
    }
}


