package com.lol.fearlessdraft.controller;

import com.lol.fearlessdraft.dto.BanPickDto;
import com.lol.fearlessdraft.dto.PlayerDto;
import com.lol.fearlessdraft.dto.RoomDto;
import com.lol.fearlessdraft.entity.Player;
import com.lol.fearlessdraft.entity.Room;
import com.lol.fearlessdraft.service.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class GameController {

   private final GameService gameService;
   private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/room/{roomId}/join")
    public void joinRoom(@DestinationVariable String roomId, @Payload PlayerDto joinRequest, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        headerAccessor.getSessionAttributes().put("roomId", roomId); // Disconnect 시 roomId를 알기 위해 세션에 저장

        Room room = gameService.findRoomById(roomId).orElse(null);
        if (room == null) return;

        // 간단한 팀 분배 로직
        String team = room.getBlueTeamPlayers().size() <= room.getRedTeamPlayers().size() ? "blue" : "red";
        Player newPlayer = new Player(sessionId, joinRequest.getName(), false);

        Room updatedRoom = gameService.addPlayerToRoom(roomId, team, newPlayer);
        broadcastRoomState(roomId, updatedRoom);
    }

    @MessageMapping("/room/{roomId}/ready")
    public void ready(@DestinationVariable String roomId, @Payload PlayerDto readyRequest, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        Room updatedRoom = gameService.setPlayerReady(roomId, sessionId, readyRequest.isReady());

        // 모든 플레이어가 준비되면 게임 시작 신호 전송
        if (updatedRoom.areAllPlayersReady()) {
            messagingTemplate.convertAndSend("/topic/room/" + roomId + "/start", "start");
        }

        broadcastRoomState(roomId, updatedRoom);
    }

    @MessageMapping("/room/{roomId}/select")
    public void selectChampion(@DestinationVariable String roomId, @Payload BanPickDto.SelectRequest selectRequest) {
        Room updatedRoom = gameService.handleChampionSelect(
                roomId,
                selectRequest.getTeam(),
                selectRequest.getAction(),
                selectRequest.getChampion().toModel()
        );
        broadcastBanPickState(roomId, updatedRoom);
    }

    public void broadcastRoomState(String roomId, Room room) {
        List<PlayerDto> blueTeam = room.getBlueTeamPlayers().values().stream().map(PlayerDto::from).collect(Collectors.toList());
        List<PlayerDto> redTeam = room.getRedTeamPlayers().values().stream().map(PlayerDto::from).collect(Collectors.toList());

        RoomDto.StateResponse response = new RoomDto.StateResponse(room.getId(), room.getName(), blueTeam, redTeam);
        messagingTemplate.convertAndSend("/topic/room/" + roomId, response);
    }

    public void broadcastBanPickState(String roomId, Room room) {
        BanPickDto.StateResponse response = BanPickDto.StateResponse.from(room.getBanPickState());
        messagingTemplate.convertAndSend("/topic/room/" + roomId + "/banpick", response);
    }

}
