package com.lol.fearlessdraft.controller;

import com.lol.fearlessdraft.common.dto.CommonResDto;
import com.lol.fearlessdraft.dto.match.CreateMatchRequest;
import com.lol.fearlessdraft.dto.match.MatchResponse;
import com.lol.fearlessdraft.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @PostMapping
    public ResponseEntity<CommonResDto<MatchResponse>> createMatch(@RequestBody CreateMatchRequest createMatchRequest) {
        MatchResponse matchResponse = matchService.createMatch(createMatchRequest);
        CommonResDto<MatchResponse> response = new CommonResDto<>(HttpStatus.CREATED, "매치가 성공적으로 생성되었습니다.", matchResponse);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<CommonResDto<MatchResponse>> getMatchByRoomId(@PathVariable String roomId) {
        MatchResponse matchResponse = matchService.findMatchByRoomId(roomId);
        CommonResDto<MatchResponse> response = new CommonResDto<>(HttpStatus.OK, "매치 상태 조회 성공", matchResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
