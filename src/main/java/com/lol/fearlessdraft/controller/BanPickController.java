package com.lol.fearlessdraft.controller;


import com.lol.fearlessdraft.dto.banpick.BanPickMessage;
import com.lol.fearlessdraft.dto.banpick.BanPickResponseDto;

import com.lol.fearlessdraft.service.BanPickService;



import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/banpick")
public class BanPickController {


    private final BanPickService banPickService;
    private final SimpMessagingTemplate messagingTemplate;
    public BanPickController( BanPickService banPickService, SimpMessagingTemplate messagingTemplate) {

        this.banPickService = banPickService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("select/{gameId}")
    public void handleBanPick(@DestinationVariable Long gameId, BanPickMessage message) {
        try {
            banPickService.processBanPickSelection(message);

            List<BanPickResponseDto> progress = banPickService.getBanPickHistoryByGameId(gameId);
            messagingTemplate.convertAndSend("/topic/banpick/progress/" + gameId, progress);

            if (banPickService.isBanPickCompleted(gameId)) {
                messagingTemplate.convertAndSend("/topic/banpick/complete/" + gameId, "밴픽 완료");
            }

        } catch (IllegalArgumentException e) {

            messagingTemplate.convertAndSend("/topic/banpick/error/" + gameId, e.getMessage());
        }
    }


}
