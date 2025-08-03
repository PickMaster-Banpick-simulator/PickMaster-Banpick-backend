package com.lol.fearlessdraft.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "pick_ban_tbl")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PickBan {

    @Id
    @Column(name = "pick_ban_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;

     private String championName;

     @Enumerated(EnumType.STRING)
     private Type type;

    @Column(nullable = false)
    private int turnOrder;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;


}
