package com.lol.fearlessdraft.repository;

import com.lol.fearlessdraft.entity.BanPickActionType;
import com.lol.fearlessdraft.entity.Game;
import com.lol.fearlessdraft.entity.PickBan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PickBanRepository extends JpaRepository<PickBan, Long > {


    List <PickBan> findAllByGameOrderByTurnOrderAsc(Game game) ;




    @Query("SELECT pb FROM PickBan pb " +
            "WHERE pb.game.match.id = :matchId AND pb.team.id = :teamId " +
            "AND pb.game.gameOrder < :currentGameNumber AND pb.type = :type")
    List<PickBan> findByMatchIdAndTeamIdAndGameNumberLessThanAndType(
            @Param("matchId") Long matchId,
            @Param("teamId") Long teamId,
            @Param("currentGameNumber") Long currentGameNumber,
            @Param("type") BanPickActionType type);

    long countByGame(Game game);


}
