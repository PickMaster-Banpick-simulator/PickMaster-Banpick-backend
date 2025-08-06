package com.lol.fearlessdraft.controller;

import com.lol.fearlessdraft.common.dto.CommonResDto;
import com.lol.fearlessdraft.dto.team.TeamRequestDto;
import com.lol.fearlessdraft.dto.team.TeamResponseDto;
import com.lol.fearlessdraft.entity.Team;
import com.lol.fearlessdraft.service.TeamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping
    public ResponseEntity<CommonResDto> getAllTeams() {
        List<Team> teams = teamService.getAllTeams();
        return ResponseEntity.ok(new CommonResDto(
                HttpStatus.OK,
                "모든 팀 조회 성공",
                teams
        ));
    }

    // 팀 생성
    @PostMapping
    public ResponseEntity<CommonResDto> createTeam(@RequestBody TeamRequestDto request) {
        TeamResponseDto response = teamService.createTeam(request);
        return ResponseEntity.ok(new CommonResDto(
                HttpStatus.OK,
                "팀 생성 성공",
                response
        ));
    }

    // 팀 수정
    @PutMapping
    public ResponseEntity<CommonResDto> updateTeam(@RequestBody TeamRequestDto request) {
        TeamResponseDto response = teamService.updateTeam(request);
        return ResponseEntity.ok(new CommonResDto(
                HttpStatus.OK,
                "팀 수정 성공",
                response
        ));
    }

    // 팀 삭제
    @DeleteMapping("/{teamId}")
    public ResponseEntity<CommonResDto> deleteTeam(@PathVariable Long teamId) {
        teamService.deleteTeam(teamId);
        return ResponseEntity.ok(new CommonResDto(
                HttpStatus.OK,
                "팀 삭제 성공",
                null
        ));
    }

}