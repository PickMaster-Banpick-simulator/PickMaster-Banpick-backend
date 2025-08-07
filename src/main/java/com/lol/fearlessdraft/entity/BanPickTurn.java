package com.lol.fearlessdraft.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@Table(name = "ban_pick_turn_tbl")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class BanPickTurn {

     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private BanPickActionType actionType;
    @Column(nullable = false)
    private Integer turnOrder;

    @Column(nullable = false)
    private String teamCode; // "A" or "B"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    private Game game;// BAN or PICK

    public BanPickTurn(String teamCode, BanPickActionType actionType) {
        this.teamCode = teamCode;
        this.actionType = actionType;
    }


    public void TurnOrder(int turnOrder) {
        this.turnOrder = turnOrder;
    }
    @ManyToOne
    @JoinColumn(name = "match_id")
    private Match match;

}
