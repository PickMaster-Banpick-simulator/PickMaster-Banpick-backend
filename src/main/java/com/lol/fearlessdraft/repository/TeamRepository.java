package com.lol.fearlessdraft.repository;

import com.lol.fearlessdraft.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team>findByName(String name);
}
