package com.lol.fearlessdraft.controller;


import com.lol.fearlessdraft.service.BanPickService;
import com.lol.fearlessdraft.service.GameService;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class BanPickController {
    private final SimpMessagingTemplate messagingTemplate;
    private final GameService gameService;
    private final BanPickService banPickService;

    public BanPickController(SimpMessagingTemplate messagingTemplate, GameService gameService, BanPickService banPickService) {
        this.messagingTemplate = messagingTemplate;
        this.gameService = gameService;
        this.banPickService = banPickService;
    }


}
