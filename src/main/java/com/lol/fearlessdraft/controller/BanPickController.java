package com.lol.fearlessdraft.controller;

import com.lol.fearlessdraft.dto.banpick.ChampionSelectAction;
import com.lol.fearlessdraft.entity.BanPickState;
import com.lol.fearlessdraft.service.BanPickService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class BanPickController {

    private final BanPickService banPickService;

    @MessageMapping("/banpick/{roomId}")
    @SendTo("/topic/banpick/{roomId}")
    public BanPickState handleChampionSelect(@DestinationVariable String roomId, ChampionSelectAction action) {
        return banPickService.processChampionSelect(roomId, action);
    }
}
