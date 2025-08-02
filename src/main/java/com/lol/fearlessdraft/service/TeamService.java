package com.lol.fearlessdraft.service;

import com.lol.fearlessdraft.dto.team.TeamRequestDto;
import com.lol.fearlessdraft.dto.team.TeamResponseDto;
import com.lol.fearlessdraft.entity.Team;
import com.lol.fearlessdraft.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
   public TeamResponseDto createTeam(TeamRequestDto teamRequestDto) {
            Team team  =  Team.builder().
                          teamName(teamRequestDto.teamName())
                                  .teamLogo(teamRequestDto.teamLogo())
                    .build();
       Team saved  =  teamRepository.save(team);

            return  new TeamResponseDto(saved.getId(), saved.getTeamName(), saved.getTeamLogo());
   }

  @Transactional
   public TeamResponseDto updateTeam(TeamRequestDto teamRequestDto) {
      Team team = teamRepository.findByName(teamRequestDto.teamName()).orElseThrow(()->
          new IllegalArgumentException("Team not found"));
       team.updateTeam(teamRequestDto.teamName(), teamRequestDto.teamLogo());

      return new TeamResponseDto(team.getId(), team.getTeamName(), team.getTeamLogo());
   }
   @Transactional
   public  void deleteTeam( Long id) {
        teamRepository.deleteById(id);
   }


}
