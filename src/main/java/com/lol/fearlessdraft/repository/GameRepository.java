package com.lol.fearlessdraft.repository;

import com.lol.fearlessdraft.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {
}
