package com.lol.fearlessdraft.repository;

import com.lol.fearlessdraft.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoomRepository extends JpaRepository<Room,String> {
}
