package com.lol.fearlessdraft.entity;


import lombok.*;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


@Data
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room  implements Serializable {



    private String id;

    private String name;

    private Map<String, Player> blueTeamPlayers = new ConcurrentHashMap<>();
    private Map<String, Player> redTeamPlayers = new ConcurrentHashMap<>();

    private Room( String id , String name ) {
        this.id = id;
        this.name = name;
    }
    private BanPickState banPickState = new BanPickState();
    public static Room create( String name) {
        Room room = new Room();

        room.id = UUID.randomUUID().toString();
        room.name = name;


        return room;
    }

    public boolean areAllPlayersReady() {
        if (blueTeamPlayers.isEmpty() || redTeamPlayers.isEmpty()) {
            return false;
        }
        boolean blueReady = blueTeamPlayers.values().stream().allMatch(Player::isReady);
        boolean redReady = redTeamPlayers.values().stream().allMatch(Player::isReady);
        return blueReady && redReady;
    }
}
