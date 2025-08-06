package com.lol.fearlessdraft.repository;

import com.lol.fearlessdraft.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {

    @Query("SELECT MAX(g.gameOrder) FROM Game g WHERE g.match.id = :matchId")
    Optional<Long> findMaxGameOrderByMatchId(@Param("matchId") Long matchId);
}
