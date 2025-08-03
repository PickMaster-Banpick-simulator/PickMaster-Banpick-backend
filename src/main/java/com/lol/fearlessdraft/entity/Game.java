package com.lol.fearlessdraft.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "game_tbl")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Game {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long gameOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blue_team_id")
    private Team blueTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "red_team_id")
    private Team redTeam;

  @Enumerated(EnumType.STRING)
  private GamePhase gamePhase;

  @Column(nullable = false)
  private Integer blueTeamWinCount = 0;

  @Column(nullable = false)
  private Integer redTeamWinCount = 0;

  // 이번 게임 결과 (true: blue win, false: red win, null: 아직 안끝남)
  private Boolean blueTeamWin;
}
