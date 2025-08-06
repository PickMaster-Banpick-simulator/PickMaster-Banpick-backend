package com.lol.fearlessdraft.controller;

import com.lol.fearlessdraft.common.dto.CommonResDto;
import com.lol.fearlessdraft.dto.match.MatchRequestDto;
import com.lol.fearlessdraft.dto.match.MatchResponseDto;
import com.lol.fearlessdraft.service.MatchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @PostMapping("/create")
    public ResponseEntity<MatchResponseDto> createMatch(@RequestBody MatchRequestDto requestDto) {
        MatchResponseDto responseDto = matchService.createMatch(requestDto);
        return ResponseEntity.ok(responseDto);
    }
    @GetMapping("/{matchId}")
    public ResponseEntity<CommonResDto> getMatch(@PathVariable Long matchId) {
        MatchResponseDto response = matchService.getMatchById(matchId);
        return ResponseEntity.ok(new CommonResDto(
                HttpStatus.OK, "매치 조회 성공", response
        ));
    }

    // 매치 전체 조회
    @GetMapping
    public ResponseEntity<CommonResDto> getAllMatches() {
        List<MatchResponseDto> matches = matchService.getAllMatches();
        return ResponseEntity.ok(new CommonResDto(
                HttpStatus.OK, "전체 매치 조회 성공", matches
        ));
    }

    // 매치 삭제
    @DeleteMapping("/{matchId}")
    public ResponseEntity<CommonResDto> deleteMatch(@PathVariable Long matchId) {
        matchService.deleteMatch(matchId);
        return ResponseEntity.ok(new CommonResDto(
                HttpStatus.OK, "매치 삭제 성공", null
        ));
    }
}
