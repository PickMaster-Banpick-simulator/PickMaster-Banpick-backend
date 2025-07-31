package com.lol.fearlessdraft.entity;


import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table
@Getter
public class Game {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long gameOrder;
    // 🔹 이 게임은 어떤 매치의 일부인가?
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    // 🔹 블루팀, 레드팀 역할로써 연결
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blue_team_id")
    private Team blueTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "red_team_id")
    private Team redTeam;
}
