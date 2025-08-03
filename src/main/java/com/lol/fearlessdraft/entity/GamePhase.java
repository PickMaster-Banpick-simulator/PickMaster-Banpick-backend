package com.lol.fearlessdraft.entity;

public enum GamePhase {

    WAITING,        // 대기 중
    BANNING,        // 밴 중
    PICKING,        // 픽 중
    CONFIRMING,     // 라인업 확인 또는 록인
    IN_PROGRESS,    // 실제 게임 진행 중
    COMPLETED,      // 해당 게임 종료됨
    MATCH_OVER      // 시리즈 종료 (3:0, 3:1, 3:2 등)
}
