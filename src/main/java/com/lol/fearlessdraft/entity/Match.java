package com.lol.fearlessdraft.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "match_tbl")
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Enumerated(EnumType.STRING)
    @Column(name = "ban_rule", nullable = false)
    private BanRule banRule;

    @Enumerated(EnumType.STRING)
    @Column(name = "match_type", nullable = false)
    private MatchType matchType;


    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Game> games = new ArrayList<>();


    @Column(name = "allow_spectators")
    private boolean allowSpectators;


    public boolean isMatchOver() {
        long teamAWins = games.stream().filter(g -> g.getWinner() != null && g.getWinner().equals(teamA)).count();
        long teamBWins = games.stream().filter(g -> g.getWinner() != null && g.getWinner().equals(teamB)).count();

        if (matchType == MatchType.BO3) return teamAWins == 2 || teamBWins == 2;
        else if (matchType == MatchType.BO5) return teamAWins == 3 || teamBWins == 3;
        return false;
    }

    public Team getWinner() {
        long teamAWins = games.stream().filter(g -> g.getWinner() != null && g.getWinner().equals(teamA)).count();
        long teamBWins = games.stream().filter(g -> g.getWinner() != null && g.getWinner().equals(teamB)).count();

        if (teamAWins > teamBWins) return teamA;
        else if (teamBWins > teamAWins) return teamB;
        else return null;
    }
}
