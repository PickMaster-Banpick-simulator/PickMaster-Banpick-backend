package com.lol.fearlessdraft.repository;

import com.lol.fearlessdraft.entity.Match;
import com.lol.fearlessdraft.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MatchRepository extends JpaRepository<Match, Long> {

    Optional<Match> findByTeamAAndTeamB(Team teamA, Team teamB);
}
