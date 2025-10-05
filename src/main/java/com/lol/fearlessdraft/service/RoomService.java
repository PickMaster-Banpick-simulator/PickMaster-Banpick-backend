package com.lol.fearlessdraft.service;

import com.lol.fearlessdraft.dto.room.CreateRoomRequest;
import com.lol.fearlessdraft.dto.room.RoomResponse;
import com.lol.fearlessdraft.entity.Room;
import com.lol.fearlessdraft.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final BanPickService banPickService;
    public RoomService(RoomRepository roomRepository, BanPickService banPickService) {
        this.roomRepository = roomRepository;
        this.banPickService = banPickService;
    }

    @Transactional
    public Room createRoom (CreateRoomRequest createRoomRequest) {
                    String roomId = UUID.randomUUID().toString().substring(0, 5);
                    Room newRoom = Room.of(roomId , createRoomRequest.roomName());
                    roomRepository.save(newRoom);
        banPickService.createInitialBanPickState(newRoom.getId());

                    return newRoom;
      }

    @Transactional
    public void deleteRoom(String roomId) {
        // 1. MySQL에서 방 정보 삭제
        roomRepository.deleteById(roomId);

        // 2. Redis에서 해당 방의 밴픽 상태 데이터 삭제
        banPickService.deleteBanPickState(roomId);
    }


}
