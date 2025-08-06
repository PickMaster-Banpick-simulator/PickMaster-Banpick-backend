package com.lol.fearlessdraft.controller;

import com.lol.fearlessdraft.common.dto.CommonResDto;
import com.lol.fearlessdraft.dto.game.GameResultRequestDto;
import com.lol.fearlessdraft.dto.game.GameStartRequestDto;
import com.lol.fearlessdraft.entity.Game;
import com.lol.fearlessdraft.entity.Match;
import com.lol.fearlessdraft.entity.Team;
import com.lol.fearlessdraft.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/games")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }


    @PostMapping("/start")
     public ResponseEntity <CommonResDto> startGame( @RequestBody GameStartRequestDto request ){
        Game game = gameService.createGameForMatch(request.matchId(),  request.gameOrder());
        CommonResDto response = new CommonResDto(
                HttpStatus.OK,
                "게임이 성공적으로 시작되었습니다.",
                game
        );

        return ResponseEntity.ok(response);
     }
    @PostMapping("/result")
      public ResponseEntity <CommonResDto> recordGameResult(@RequestBody GameResultRequestDto request ){
          gameService.recordGameResult(request.gameId(), request.winnerTeamId());
          CommonResDto response = new CommonResDto(
                  HttpStatus.OK,
                  "게임 결과가 정상적으로 기록되었습니다.",
                  null
          );

          return ResponseEntity.ok(response);
      }


}
