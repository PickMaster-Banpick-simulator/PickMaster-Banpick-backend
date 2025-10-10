# FearlessDraft

This is a project for a League of Legends draft simulator.

## WebSocket API

### GameController (WebSocket)

#### Subscriptions

-   `/topic/room/{roomId}`: 해당 방의 상태를 구독합니다. 플레이어 목록, 준비 상태 등 방의 전반적인 정보를 받습니다.
-   `/topic/room/{roomId}/start`: 모든 플레이어가 준비 완료 시 "start" 메시지를 받습니다.
-   `/topic/room/{roomId}/banpick`: 밴픽 상태를 구독합니다. 밴/픽 챔피언, 현재 턴 등의 정보를 받습니다.

#### Messages

-   `POST /app/room/{roomId}/join`
    -   **Description:** 방에 입장합니다.
    -   **Request Body:** `PlayerDto`
        ```json
        {
          "name": "T1 Faker"
        }
        ```
-   `POST /app/room/{roomId}/ready`
    -   **Description:** 준비 상태를 변경합니다.
    -   **Request Body:** `PlayerDto`
        ```json
        {
          "ready": true
        }
        ```
-   `POST /app/room/{roomId}/select`
    -   **Description:** 챔피언을 밴 또는 픽합니다.
    -   **Request Body:** `BanPickDto.SelectRequest`
        ```json
        {
          "team": "blue",
          "action": "ban",
          "champion": {
            "id": "Aatrox",
            "name": "아트록스",
            "image": "Aatrox.png"
          }
        }
        ```