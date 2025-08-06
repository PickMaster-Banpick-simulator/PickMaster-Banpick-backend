package com.lol.fearlessdraft.common;

import com.lol.fearlessdraft.entity.BanPickActionType;
import com.lol.fearlessdraft.entity.BanPickTurn;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
public class BanPickTurnFactory {

    public static List<BanPickTurn> createStandardTurns() {
        AtomicInteger turnCounter = new AtomicInteger(1);

        return Stream.of(
                        turns("A", BanPickActionType.BAN, 3),
                        turns("B", BanPickActionType.BAN, 3),
                        turns("A", BanPickActionType.PICK, 1),
                        turns("B", BanPickActionType.PICK, 2),
                        turns("A", BanPickActionType.PICK, 2),
                        turns("B", BanPickActionType.PICK, 1),
                        turns("A", BanPickActionType.BAN, 2),
                        turns("B", BanPickActionType.BAN, 2),
                        turns("A", BanPickActionType.PICK, 2),
                        turns("B", BanPickActionType.PICK, 2)
                ).flatMap(List::stream)
                .map(turn -> {
                    turn.TurnOrder(turnCounter.getAndIncrement());
                    return turn;
                })
                .toList();
    }

    private static List<BanPickTurn> turns(String team, BanPickActionType type, int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> new BanPickTurn( team, type))
                .toList();
    }
}
