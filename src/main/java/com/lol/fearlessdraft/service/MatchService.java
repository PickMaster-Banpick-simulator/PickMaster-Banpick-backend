package com.lol.fearlessdraft.service;

import com.lol.fearlessdraft.dto.match.MatchRequestDto;
import com.lol.fearlessdraft.dto.match.MatchResponseDto;
import com.lol.fearlessdraft.entity.Match;
import com.lol.fearlessdraft.entity.Team;
import com.lol.fearlessdraft.repository.MatchRepository;
import com.lol.fearlessdraft.repository.TeamRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class MatchService {

    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final PasswordEncoder passwordEncoder;
    public MatchService(MatchRepository matchRepository, TeamRepository teamRepository, PasswordEncoder passwordEncoder) {
        this.matchRepository = matchRepository;
        this.teamRepository = teamRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Transactional
    public MatchResponseDto createMatch(MatchRequestDto matchRequestDto) {
        Team teamA = teamRepository.findById(matchRequestDto.teamAId())
                .orElseThrow(() -> new IllegalArgumentException("Team A not found"));
        Team teamB = teamRepository.findById(matchRequestDto.teamBId())
                .orElseThrow(() -> new IllegalArgumentException("Team B not found"));

        Optional<Match> existingMatch = matchRepository.findByTeamAAndTeamB(teamA, teamB);
        if (existingMatch.isPresent()) {
            if (!passwordEncoder.matches(matchRequestDto.matchPassword(), existingMatch.get().getMatchPassword())) {
                throw new IllegalArgumentException("이미 동일한 매치가 존재하지만 비밀번호가 일치하지 않습니다.");
            }

            Match match = existingMatch.get();
            return MatchResponseDto.builder()
                    .matchId(match.getId())
                    .matchName(match.getMatchName())
                    .teamAName(teamA.getTeamName())
                    .teamBName(teamB.getTeamName())
                    .numberOfGames(match.getNumberOfGames())
                    .matchType(match.getMatchType())
                    .allowSpectators(match.isAllowSpectators())
                    .build();
        }

        String encodedPassword = passwordEncoder.encode(matchRequestDto.matchPassword());
        Match match = Match.builder()
                .matchName(matchRequestDto.matchName())
                .matchPassword(encodedPassword)
                .numberOfGames(matchRequestDto.numberOfGames())
                .matchType(matchRequestDto.matchType())
                .allowSpectators(matchRequestDto.allowSpectators())
                .teamA(teamA)
                .teamB(teamB)
                .build();

        Match savedMatch = matchRepository.save(match);

        return MatchResponseDto.builder()
                .matchId(savedMatch.getId())
                .matchName(savedMatch.getMatchName())
                .teamAName(teamA.getTeamName())
                .teamBName(teamB.getTeamName())
                .numberOfGames(savedMatch.getNumberOfGames())
                .matchType(savedMatch.getMatchType())
                .allowSpectators(savedMatch.isAllowSpectators())
                .build();
    }




}
