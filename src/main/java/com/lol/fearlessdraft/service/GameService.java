package com.lol.fearlessdraft.service;

import com.lol.fearlessdraft.entity.BanPickState;
import com.lol.fearlessdraft.entity.Champion;
import com.lol.fearlessdraft.entity.Player;
import com.lol.fearlessdraft.entity.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service

public class GameService {

    private final RedisTemplate<String, Room> redisTemplate;
    private static final String ROOM_KEY_PREFIX = "gameroom:";

    private static final List<String> BANPICK_ORDER = List.of(
            "blue:ban", "red:ban", "blue:ban", "red:ban", "blue:ban", "red:ban",
            "blue:pick", "red:pick", "red:pick", "blue:pick", "blue:pick", "red:pick",
            "red:ban", "blue:ban", "red:ban", "blue:ban",
            "red:pick", "blue:pick", "blue:pick", "red:pick"
    );

    public GameService(RedisTemplate<String, Room> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Room createRoom(String roomName) {
        Room room = Room.create(roomName);
        room.getBanPickState().updateCurrentTurn(BANPICK_ORDER.get(0));
        saveRoomWithTTL(room, Duration.ofHours(2));
        return room;
    }

    public Optional<Room> findRoomById(String roomId) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(ROOM_KEY_PREFIX + roomId));
    }

    public void saveRoom(Room room) {
        String key = ROOM_KEY_PREFIX + room.getId();
        Long expire = redisTemplate.getExpire(key);
        if (expire != null && expire > 0) {
            saveRoomWithTTL(room, Duration.ofSeconds(expire));
        } else {
            saveRoomWithTTL(room, Duration.ofHours(2));
        }
    }

    // saveRoomWithTTL 메서드의 파라미터 타입을 Room으로 수정
    private void saveRoomWithTTL(Room room, Duration ttl) {
        redisTemplate.opsForValue().set(ROOM_KEY_PREFIX + room.getId(), room, ttl);
    }

    // getRoomOrThrow 메서드의 반환 타입을 Room으로 수정
    private Room getRoomOrThrow(String roomId) {
        return findRoomById(roomId)
                .orElseThrow(() -> new NoSuchElementException("Room not found with id: " + roomId));
    }

    public Room addPlayerToRoom(String roomId, String team, Player player) {
        Room room = getRoomOrThrow(roomId);
        if ("blue".equalsIgnoreCase(team)) {
            room.getBlueTeamPlayers().put(player.getSessionId(), player);
        } else {
            room.getRedTeamPlayers().put(player.getSessionId(), player);
        }
        saveRoom(room);
        return room;
    }

    public Room setPlayerReady(String roomId, String sessionId, boolean isReady) {
        Room room = getRoomOrThrow(roomId);
        Player player = room.getBlueTeamPlayers().get(sessionId);
        if (player == null) {
            player = room.getRedTeamPlayers().get(sessionId);
        }
        if (player != null) {
            player.updateReady(isReady);
            saveRoom(room);
        }
        return room;
    }

    // handleChampionSelect 메서드의 반환 타입을 Room으로 수정
    public Room handleChampionSelect(String roomId, String team, String action, Champion champion) {
        Room room = getRoomOrThrow(roomId);
        BanPickState state = room.getBanPickState();

        // 현재 턴이 맞는지 검증
        String expectedTurn = team + ":" + action;
        if (!state.getCurrentTurn().equalsIgnoreCase(expectedTurn)) {
            // 잘못된 턴 요청은 무시하거나 예외 처리
            return room;
        }

        // 밴 또는 픽 처리
        if ("ban".equals(action)) {
            if ("blue".equals(team)) state.getBlueBans().add(champion);
            else state.getRedBans().add(champion);
        } else { // pick
            if ("blue".equals(team)) state.getBluePicks().add(champion);
            else state.getRedPicks().add(champion);
        }

        // 다음 턴으로 넘기기
        state.updateTurnIndex(state.getTurnIndex() + 1);
        if (state.getTurnIndex() < BANPICK_ORDER.size()) {
            state.updateCurrentTurn(BANPICK_ORDER.get(state.getTurnIndex()));
        } else {
            // 밴픽 종료
            state.updateCurrentTurn("finished");
            state.finishBanPick();
        }

        saveRoom(room);
        return room;
    }
}