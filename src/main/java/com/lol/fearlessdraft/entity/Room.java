package com.lol.fearlessdraft.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room {

    @Id
    @Column(length = 32)
    private String id;

    @Column(nullable = false)
    private String name;

    private Room( String id , String name ) {
        this.id = id;
        this.name = name;
    }

    public static Room of(String id, String name) {
        return new Room(id, name);
    }
}
