package com.lol.fearlessdraft.dto.room;

import com.lol.fearlessdraft.entity.Room;

public record RoomResponse(
        String id ,
        String name

) {
    public static   RoomResponse createRoomResponse(
        Room room
    ) {
        return new RoomResponse(
                room.getId(),
                room.getName()

        );
    }

}
