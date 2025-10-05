package com.lol.fearlessdraft.dto.room;

import com.lol.fearlessdraft.entity.Room;
import com.lol.fearlessdraft.repository.RoomRepository;

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
