# FearlessDraft

This is a project for a League of Legends draft simulator.

## DTOs

### MatchResponse.java
```java
package com.lol.fearlessdraft.dto.match;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchResponse {
    private String matchId;
    private String roomName;
    private String hostName;
    private String redTeamUser;
    private String blueTeamUser;
    private String matchType; // bo3, bo5
}
```

### CreateMatchRequest.java
```java
package com.lol.fearlessdraft.dto.match;

import lombok.Data;

@Data
public class CreateMatchRequest {
    private String roomName;
    private String hostName;
    private String redTeamUser;
    private String blueTeamUser;
    private String matchType; // bo3, bo5
}
```

### RoomResponse.java
```java
package com.lol.fearlessdraft.dto.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponse {
    private String roomId;
    private String roomName;
    private String hostName;
    private String redTeamUser;
    private String blueTeamUser;
}
```

### ChampionSelectAction.java
```java
package com.lol.fearlessdraft.dto.banpick;

import lombok.Data;

@Data
public class ChampionSelectAction {
    private String type; // BAN or PICK
    private String championName;
    private String team; // RED or BLUE
    private int pickOrder;
}
```

### CreateRoomRequest.java
```java
package com.lol.fearlessdraft.dto.room;

import lombok.Data;

@Data
public class CreateRoomRequest {
    private String roomName;
    private String hostName;
}
```

## API Endpoints

### BanPickController (WebSocket)
- **`POST /app/banpick/{roomId}`**
  - **Description:** 챔피언 선택 액션을 처리하고, 해당 방의 밴픽 상태를 업데이트합니다.
  - **Request Body:** `ChampionSelectAction`
  - **Sends to:** `/topic/banpick/{roomId}`
  - **Response:** `BanPickState`

### MatchController (REST)
- **Base Path:** `/api/matches`
- **`POST /api/matches`**
  - **Description:** 새로운 매치를 생성합니다.
  - **Request Body:** `CreateMatchRequest`
  - **Response:** `CommonResDto<MatchResponse>` (HTTP 201 Created)
- **`GET /api/matches/room/{roomId}`**
  - **Description:** 방 ID를 통해 매치 상세 정보를 조회합니다.
  - **Response:** `CommonResDto<MatchResponse>` (HTTP 200 OK)

### RoomController (REST)
- **Base Path:** `/api/rooms`
- **`POST /api/rooms`**
  - **Description:** 새로운 방을 생성합니다.
  - **Request Body:** `CreateRoomRequest`
  - **Response:** `CommonResDto<RoomResponse>` (HTTP 201 Created)
- **`DELETE /api/rooms`**
  - **Description:** 방 ID를 통해 방을 삭제합니다.
  - **Request Parameter:** `roomId`
  - **Response:** `CommonResDto<Void>` (HTTP 200 OK)
- **`GET /api/rooms/{roomId}`**
  - **Description:** 방 ID를 통해 방 상세 정보를 조회합니다.
  - **Response:** `CommonResDto<RoomResponse>` (HTTP 200 OK)
