package com.lol.fearlessdraft.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "match_tbl")
public class Match {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_id")
    private Long id;

    @Column(name = "match_name")
    private String matchName;
    @Column(name = "match_password")
    private String matchPassword;

    @Column(name = "number_of_games")
    private Integer numberOfGames;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_a_id")
    private Team teamA;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_b_id")
    private Team teamB;

}
