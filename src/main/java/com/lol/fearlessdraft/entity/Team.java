package com.lol.fearlessdraft.entity;

import jakarta.persistence.*;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "team_tbl")
public class Team {
    @Id
    @Column(name = "team_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

    @Column(nullable = false ,length = 20)
   private String teamName;

   @Column(nullable = false)
   private String teamLogo;
    @Column(nullable = false, length = 10)
    private String teamCode;
    @OneToMany(mappedBy = "teamA")
    private List<Match> matchesAsTeamA = new ArrayList<>();

    @OneToMany(mappedBy = "teamB")
    private List<Match> matchesAsTeamB = new ArrayList<>();

    public void updateTeam(String teamName, String teamLogo) {
        this.teamName = teamName;
        this.teamLogo = teamLogo;
    }
}
