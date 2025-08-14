package com.lol.fearlessdraft.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "champion_tbl")
@Getter
@Builder
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
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
