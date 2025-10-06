package com.lol.fearlessdraft.repository;

import com.lol.fearlessdraft.entity.DraftMatch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DraftMatchRepository extends JpaRepository<DraftMatch, Long> {
    Optional<DraftMatch> findByRoom_Id(String roomId);
}
