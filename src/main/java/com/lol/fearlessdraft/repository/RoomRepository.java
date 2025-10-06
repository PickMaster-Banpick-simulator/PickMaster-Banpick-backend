package com.lol.fearlessdraft.repository;

import com.lol.fearlessdraft.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface RoomRepository extends JpaRepository<Room,String> {

    Optional<Room> findRoomById(String roomId);
}
