package com.lol.fearlessdraft.service;

import com.lol.fearlessdraft.repository.GameRepository;
import com.lol.fearlessdraft.repository.MatchRepository;
import org.springframework.stereotype.Service;

@Service
public class GameService {


    private final GameRepository gameRepository;
    private final MatchRepository matchRepository;

    public GameService(GameRepository gameRepository, MatchRepository matchRepository) {
        this.gameRepository = gameRepository;
        this.matchRepository = matchRepository;
    }
}
