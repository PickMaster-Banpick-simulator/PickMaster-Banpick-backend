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

import java.util.List;
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
            Match match = existingMatch.get();
            if (!passwordEncoder.matches(matchRequestDto.matchPassword(), match.getMatchPassword())) {
                throw new IllegalArgumentException("이미 동일한 매치가 존재하지만 비밀번호가 일치하지 않습니다.");
            }
            return MatchResponseDto.from(match);
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
        return MatchResponseDto.from(savedMatch);
    }

    @Transactional(readOnly = true)
    public List<MatchResponseDto> getAllMatches() {
        return matchRepository.findAll().stream()
                .map(MatchResponseDto::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public MatchResponseDto getMatchById(Long matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found"));
        return MatchResponseDto.from(match);
    }

    @Transactional
    public void deleteMatch(Long matchId) {
        matchRepository.deleteById(matchId);
    }
}
