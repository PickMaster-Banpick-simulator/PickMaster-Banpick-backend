package com.lol.fearlessdraft.entity;


import java.io.Serializable;
import java.util.List;

import java.util.stream.Stream;

public record BanPickState(
    int turnIndex,
    List<Champion> blueBans,
    List<Champion> redBans,
    List<Champion> bluePicks,
    List<Champion> redPicks) implements Serializable {

    // 초기 상태를 생성하는 정적 팩토리 메서드
    public static BanPickState createInitialState() {
        return new BanPickState(
            0,
            Stream.generate(() -> (Champion) null).limit(5).toList(),
            Stream.generate(() -> (Champion) null).limit(5).toList(),
            Stream.generate(() -> (Champion) null).limit(5).toList(),
            Stream.generate(() -> (Champion) null).limit(5).toList()
        );
    }
}
