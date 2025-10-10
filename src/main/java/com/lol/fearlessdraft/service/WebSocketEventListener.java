package com.lol.fearlessdraft.service;


import com.lol.fearlessdraft.controller.GameController;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final GameService gameService;
    private final GameController gameController;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");

        if (roomId != null && sessionId != null) {
            gameService.findRoomById(roomId).ifPresent(room -> {
                room.getBlueTeamPlayers().remove(sessionId);
                room.getRedTeamPlayers().remove(sessionId);
                gameService.saveRoom(room);
                gameController.broadcastRoomState(roomId, room); // GameController의 메서드 재사용
            });
        }

}
