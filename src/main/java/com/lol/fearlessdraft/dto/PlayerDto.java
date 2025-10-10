package com.lol.fearlessdraft.dto;

import com.lol.fearlessdraft.entity.Player;
import lombok.Value;

@Value
public class PlayerDto {
  String id;
  String name;
  boolean isReady;

  public static PlayerDto from( Player player) {
      return new PlayerDto(player.getSessionId() , player.getUsername(), player.isReady());
  }
}
