package com.lol.fearlessdraft.repository;

import com.lol.fearlessdraft.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, Long> {
}
