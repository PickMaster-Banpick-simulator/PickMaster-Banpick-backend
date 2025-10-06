package com.lol.fearlessdraft.controller;

import com.lol.fearlessdraft.common.dto.CommonResDto;
import com.lol.fearlessdraft.dto.room.CreateRoomRequest;
import com.lol.fearlessdraft.dto.room.RoomResponse;
import com.lol.fearlessdraft.entity.Room;
import com.lol.fearlessdraft.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<CommonResDto<RoomResponse>> createRoom(@RequestBody CreateRoomRequest createRoomRequest) {
        Room createdRoom = roomService.createRoom(createRoomRequest);
        RoomResponse roomResponse = RoomResponse.createRoomResponse(createdRoom);
        CommonResDto<RoomResponse> response = new CommonResDto<>(HttpStatus.CREATED, "방 생성 성공", roomResponse);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<CommonResDto<Void>> deleteRoomById(@RequestParam String roomId) {
        roomService.deleteRoom(roomId);
        CommonResDto<Void> response = new CommonResDto<>(HttpStatus.OK, "방 삭제 성공", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<CommonResDto<RoomResponse>> getRoomById(@PathVariable String roomId) {
        Room room = roomService.getRoom(roomId);
        RoomResponse roomResponse = RoomResponse.createRoomResponse(room);
        CommonResDto<RoomResponse> response = new CommonResDto<>(HttpStatus.OK, "방 조회 성공", roomResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
