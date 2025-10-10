package com.lol.fearlessdraft.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Player implements Serializable {

    private String sessionId;
    private String username;
    private boolean isReady = false;

    public void updateReady(boolean isReady) {
        this.isReady = isReady;
    }
}
