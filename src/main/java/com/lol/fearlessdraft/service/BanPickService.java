package com.lol.fearlessdraft.service;

import com.lol.fearlessdraft.dto.banpick.ChampionSelectAction;
import com.lol.fearlessdraft.entity.BanPickState;
import com.lol.fearlessdraft.entity.Champion;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;





import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class BanPickService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String KEY_PREFIX = "banpick:state:";

    public BanPickState processChampionSelect(String roomId, ChampionSelectAction action) {
        String key = KEY_PREFIX + roomId;
        BanPickState currentState = (BanPickState) redisTemplate.opsForValue().get(key);

        if (currentState == null) {
            currentState = BanPickState.createInitialState();
        }

        // 레코드는 불변이므로, 수정을 위해 새로운 리스트를 생성
        List<Champion> newBlueBans = new ArrayList<>(currentState.blueBans());
        List<Champion> newRedBans = new ArrayList<>(currentState.redBans());
        List<Champion> newBluePicks = new ArrayList<>(currentState.bluePicks());
        List<Champion> newRedPicks = new ArrayList<>(currentState.redPicks());

        if ("ban".equals(action.action())) {
            List<Champion> bans = "blue".equals(action.team()) ? newBlueBans : newRedBans;
            updateFirstNull(bans, action.selectedChampion());
        } else if ("pick".equals(action.action())) {
            List<Champion> picks = "blue".equals(action.team()) ? newBluePicks : newRedPicks;
            updateFirstNull(picks, action.selectedChampion());
        }

        // 변경된 내용으로 새로운 BanPickState 레코드 인스턴스 생성
        BanPickState nextState = new BanPickState(
                currentState.turnIndex() + 1,
                newBlueBans,
                newRedBans,
                newBluePicks,
                newRedPicks
        );

        redisTemplate.opsForValue().set(key, nextState, 2, TimeUnit.HOURS);
        return nextState;
    }

    public BanPickState getBanPickState(String roomId) {
        String key = KEY_PREFIX + roomId;
        BanPickState state = (BanPickState) redisTemplate.opsForValue().get(key);
        if (state == null) {
            state = BanPickState.createInitialState();
            redisTemplate.opsForValue().set(key, state, 2, TimeUnit.HOURS);
        }
        return state;
    }

    private void updateFirstNull(List<Champion> list, Champion element) {
        int index = list.indexOf(null);
        if (index != -1) {
            list.set(index, element);
        }
    }

    public void deleteBanPickState(String roomId) {
        String key = KEY_PREFIX + roomId;
        redisTemplate.delete(key);
    }
}