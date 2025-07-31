package com.lol.fearlessdraft.repository;

import com.lol.fearlessdraft.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TeamRepository extends JpaRepository<Team, Long> {
}
