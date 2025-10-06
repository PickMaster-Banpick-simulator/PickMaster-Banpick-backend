package com.lol.fearlessdraft.entity;

import java.io.Serializable;

// Redis 직렬화를 위해 Serializable을 구현한 레코드
public record Champion(String id, String name, String image) implements Serializable {}
