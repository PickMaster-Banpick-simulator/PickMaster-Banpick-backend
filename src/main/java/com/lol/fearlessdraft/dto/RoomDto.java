package com.lol.fearlessdraft.dto;

import lombok.Data;
import lombok.Value;

import java.util.List;

public class RoomDto {

    @Data
   public static class CreateRequest {
       private String name;
   }

   @Value
   public static class CreateResponse {
        String id;
        String name;
   }

   public static class StateResponse {
        String id ;
        String name;
       List<PlayerDto> blueTeamPlayers;
       List<PlayerDto> redTeamPlayers;
   }
}
