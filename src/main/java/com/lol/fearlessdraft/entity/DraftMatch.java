package com.lol.fearlessdraft.entity;

import com.lol.fearlessdraft.entity.enums.MatchType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DraftMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MatchType matchType;

    @Column(nullable = false)
    private String blueTeamName;

    @Column(nullable = false)
    private String redTeamName;

    private int blueWins;

    private int redWins;

    @Builder
    public DraftMatch(Room room, MatchType matchType, String blueTeamName, String redTeamName) {
        this.room = room;
        this.matchType = matchType;
        this.blueTeamName = blueTeamName;
        this.redTeamName = redTeamName;
        this.blueWins = 0;
        this.redWins = 0;
    }
}
