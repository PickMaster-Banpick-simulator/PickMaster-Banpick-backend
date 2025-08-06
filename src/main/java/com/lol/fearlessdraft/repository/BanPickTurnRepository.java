package com.lol.fearlessdraft.repository;

import com.lol.fearlessdraft.entity.BanPickTurn;
import com.lol.fearlessdraft.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BanPickTurnRepository extends JpaRepository<BanPickTurn,Long> {

    Optional<BanPickTurn> findByGameAndTurnOrder(Game game, int turnOrder);

    int countByGame(Game game);
}
