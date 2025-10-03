package com.lol.fearlessdraft.service;

import com.lol.fearlessdraft.dto.team.TeamRequestDto;
import com.lol.fearlessdraft.dto.team.TeamResponseDto;
import com.lol.fearlessdraft.entity.Team;
import com.lol.fearlessdraft.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class TeamService {

    private  final TeamRepository teamRepository;


    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }
   @Transactional(readOnly = true)
    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    @Transactional
    public TeamResponseDto createTeam(String teamName, String teamCode, MultipartFile teamLogoFile) throws  IOException {
        String logoFileName = null;

        if (teamLogoFile != null && !teamLogoFile.isEmpty()) {
            // 예: src/main/resources/static/images/teams 에 저장
            String uploadDir = "C:/Users/naseokhoo/Desktop/fearless_draft/FearlessDraft/src/main/resources/static/images";
            logoFileName = teamLogoFile.getOriginalFilename();
            Path filePath = Paths.get(uploadDir + logoFileName);
            Files.createDirectories(filePath.getParent());
            teamLogoFile.transferTo(filePath.toFile());
        }

        Team team = Team.builder()
                .teamName(teamName)
                .teamCode(teamCode)
                .teamLogo(logoFileName)
                .build();

        Team saved = teamRepository.save(team);
        return new TeamResponseDto(saved.getId(), saved.getTeamName(), saved.getTeamLogo(), saved.getTeamCode());
    }

    @Transactional
   public TeamResponseDto updateTeam(TeamRequestDto teamRequestDto) {
      Team team = teamRepository.findByTeamName(teamRequestDto.teamName()).orElseThrow(()->
          new IllegalArgumentException("Team not found"));
       team.updateTeam(teamRequestDto.teamName(), teamRequestDto.teamLogo());

      return new TeamResponseDto(team.getId(), team.getTeamName(), team.getTeamLogo(), team.getTeamCode());
   }
   @Transactional
   public  void deleteTeam( Long id) {
        teamRepository.deleteById(id);
   }


}
