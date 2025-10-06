package com.lol.fearlessdraft.service;

import com.lol.fearlessdraft.dto.room.CreateRoomRequest;

import com.lol.fearlessdraft.entity.Room;
import com.lol.fearlessdraft.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.UUID;


@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final BanPickService banPickService;

    @Transactional
    public Room createRoom(CreateRoomRequest createRoomRequest) {
        String roomId = UUID.randomUUID().toString().substring(0, 5);
        Room newRoom = Room.of(roomId, createRoomRequest.roomName());
        roomRepository.save(newRoom);
        banPickService.getBanPickState(roomId); // A new ban-pick state is created in Redis
        return newRoom;
    }


    @Transactional
    public void deleteRoom(String roomId) {
        // 1. MySQL에서 방 정보 삭제
        roomRepository.deleteById(roomId);
        banPickService.deleteBanPickState(roomId);
        // 2. Redis에서 해당 방의 밴픽 상태 데이터 삭제

    }
    @Transactional(readOnly = true)
    public Room getRoom(String roomId) {

        return roomRepository.findById(roomId).orElseThrow(()->new IllegalArgumentException("해당 방을 찾을 수 없습니다. ID:"+ roomId));
    }

}
