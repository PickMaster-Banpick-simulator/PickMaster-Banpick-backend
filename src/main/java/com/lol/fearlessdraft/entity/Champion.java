package com.lol.fearlessdraft.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "champion_tbl")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Champion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // 예: "Ahri"

    @Column(nullable = true)
    private String imageUrl; // 예: "/images/champions/ahri.png"


}
