package com.lol.fearlessdraft.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class BanPickState implements Serializable {

    private int turnIndex = 0;
    private String currentTurn; // "blue:ban", "red:pick" ...
    private List<Champion> blueBans = new ArrayList<>();
    private List<Champion> redBans = new ArrayList<>();
    private List<Champion> bluePicks = new ArrayList<>();
    private List<Champion> redPicks = new ArrayList<>();
    private boolean isBanPickFinished = false;

    public void updateTurnIndex(int turnIndex) {
        this.turnIndex = turnIndex;
    }

    public void updateCurrentTurn(String currentTurn) {
        this.currentTurn = currentTurn;
    }

    public void finishBanPick() {
        this.isBanPickFinished = true;
    }
}
