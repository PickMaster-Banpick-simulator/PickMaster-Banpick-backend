package com.lol.fearlessdraft.dto.banpick;

import com.lol.fearlessdraft.entity.Champion;

public record ChampionSelectAction(Champion selectedChampion, String team, String action) {}
