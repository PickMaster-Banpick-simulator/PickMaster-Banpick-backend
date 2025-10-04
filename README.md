✦ 네, 알겠습니다. 지금까지 만든 API에 대한 명세서를 작성해 드리겠습니다.

이 명세서를 바탕으로 프론트엔드 개발을 진행하거나, API 테스트 도구(Postman 등)를 사용하여 백엔드가 잘 동작하는지 확인할 수 있습니다.

  ---                                                                                                                                                                                                                                                               

## Pick Master API 명세서


Base URL: http://localhost:8080

### 1. Room API

방 생성, 조회, 입장을 관리합니다.

#### 1.1 방 생성


- Endpoint: POST /api/rooms                                                                                                                                                                                                                                      
  `json                                                                                                                                                                                                                                                             
      {                                                                                                                                                                                                                                                             
        "statusCode": 201,                                                                                                                                                                                                                                          
        "message": "방 생성 성공",                                                                                                                                                                                                                                  
        "data": {                                                                                                                                                                                                                                                   
          "id": 1,                                                                                                                                                                                                                                                  
          "name": "새로운 방 이름",                                                                                                                                                                                                                                 
          "hasPassword": true                                                                                                                                                                                                                                       
        }                                                                                                                                                                                                                                                           
      }                                                                                                                                                                                                                                                             
      `


1.2 전체 방 목록 조회
`json                                                                                                                                                                                                                                                             
    {                                                                                                                                                                                                                                                             
      "statusCode": 200,                                                                                                                                                                                                                                          
      "message": "전체 방 목록 조회 성공",                                                                                                                                                                                                                        
      "data": [                                                                                                                                                                                                                                                   
        {                                                                                                                                                                                                                                                         
          "id": 1,                                                                                                                                                                                                                                                
          "name": "새로운 방 이름",                                                                                                                                                                                                                               
          "hasPassword": true                                                                                                                                                                                                                                     
        },                                                                                                                                                                                                                                                        
        {                                                                                                                                                                                                                                                         
          "id": 2,                                                                                                                                                                                                                                                
          "name": "비번 없는 방",                                                                                                                                                                                                                                 
          "hasPassword": false                                                                                                                                                                                                                                    
        }                                                                                                                                                                                                                                                         
      ]                                                                                                                                                                                                                                                           
    }                                                                                                                                                                                                                                                             
    `

#### 1.3 방 입장 (비밀번호 확인)


- Endpoint: POST /api/rooms/{roomId}/enter                                                                                                                                                                                                                       
  `json                                                                                                                                                                                                                                                             
      {                                                                                                                                                                                                                                                             
        "password": "사용자가 입력한 비밀번호"                                                                                                                                                                                                                      
      }                                                                                                                                                                                                                                                             
      `
-   Success Response (200 OK):

`json                                                                                                                                                                                                                                                             
    {                                                                                                                                                                                                                                                             
      "statusCode": 200,                                                                                                                                                                                                                                          
      "message": "방 입장 성공",                                                                                                                                                                                                                                  
      "data": true                                                                                                                                                                                                                                                
    }                                                                                                                                                                                                                                                             
    `
-   Error Response (401 UNAUTHORIZED):                                                                                                                                                                                                                            
    `json                                                                                                                                                                                                                                                         
    {                                                                                                                                                                                                                                                             
      "statusCode": 401,                                                                                                                                                                                                                                          
      "message": "비밀번호가 틀렸습니다.",                                                                                                                                                                                                                        
      "data": false                                                                                                                                                                                                                                               
    }                                                                                                                                                                                                                                                             
    `

  ---                                                                                                                                                                                                                                                               


2. Match API
- Endpoint: POST /api/matches                                                                                                                                                                                                                                    
  `json                                                                                                                                                                                                                                                             
      {                                                                                                                                                                                                                                                             
        "roomId": 1,                                                                                                                                                                                                                                                
        "matchType": "BO3", // "BO1", "BO3", "BO5" 중 하나                                                                                                                                                                                                          
        "blueTeamName": "블루팀",                                                                                                                                                                                                                                   
        "redTeamName": "레드팀"                                                                                                                                                                                                                                     
      }                                                                                                                                                                                                                                                             
      `
-   Success Response (201 CREATED):

`json                                                                                                                                                                                                                                                             
    {                                                                                                                                                                                                                                                             
      "statusCode": 201,                                                                                                                                                                                                                                          
      "message": "매치가 성공적으로 생성되었습니다.",                                                                                                                                                                                                             
      "data": {                                                                                                                                                                                                                                                   
        "matchId": 1,                                                                                                                                                                                                                                             
        "roomId": 1,                                                                                                                                                                                                                                              
        "matchType": "BO3",                                                                                                                                                                                                                                       
        "blueTeamName": "블루팀",                                                                                                                                                                                                                                 
        "redTeamName": "레드팀"                                                                                                                                                                                                                                   
      }                                                                                                                                                                                                                                                           
    }                                                                                                                                                                                                                                                             
    `


2.2 현재 매치 상태 조회
- Endpoint: GET /api/matches/room/{roomId}                                                                                                                                                                                                                       
  `json                                                                                                                                                                                                                                                             
      {                                                                                                                                                                                                                                                             
        "statusCode": 200,                                                                                                                                                                                                                                          
        "message": "매치 상태 조회 성공",                                                                                                                                                                                                                           
        "data": {                                                                                                                                                                                                                                                   
          "matchId": 1,                                                                                                                                                                                                                                             
          "matchType": "BO3",                                                                                                                                                                                                                                       
          "blueTeamName": "블루팀",                                                                                                                                                                                                                                 
          "redTeamName": "레드팀",                                                                                                                                                                                                                                  
          "blueWins": 0,                                                                                                                                                                                                                                            
          "redWins": 1,                                                                                                                                                                                                                                             
          "currentGameNumber": 2,                                                                                                                                                                                                                                   
          "banPick": {                                                                                                                                                                                                                                              
            "turnIndex": 0,                                                                                                                                                                                                                                         
            "blueBans": [null, null, null, null, null],                                                                                                                                                                                                             
            "redBans": [null, null, null, null, null],                                                                                                                                                                                                              
            "bluePicks": [null, null, null, null, null],                                                                                                                                                                                                            
            "redPicks": [null, null, null, null, null],                                                                                                                                                                                                             
            "winner": null                                                                                                                                                                                                                                          
          }                                                                                                                                                                                                                                                         
        }                                                                                                                                                                                                                                                           
      }                                                                                                                                                                                                                                                             
      `

  ---                                                                                                                                                                                                                                                               

### 3. Ban/Pick WebSocket API

실시간 밴픽 데이터 교환을 위한 웹소켓 명세입니다.

-   WebSocket Endpoint: /ws (최초 연결 주소)

#### 3.1 구독 (Subscribe)


- Topic: /topic/banpick/{roomId}
- Destination: /app/banpick/{roomId}
  `json
      {
        "type": "메시지 타입", // "BANPICK_UPDATE", "GAME_FINISH_UPDATE"
        "payload": { ... }
      }
      `

-   `BANPICK_UPDATE` 발행 Payload:
    -   클라이언트가 챔피언을 선택(밴 또는 픽)했을 때 서버로 전송합니다.

`json
    // 클라이언트가 턴에 맞게 챔피언 하나를 추가한 상태
    {
      "turnIndex": 1, // 다음 턴 인덱스
      "blueBans": [{ "id": "Aatrox", "name": "아트록스", ... }, null, ...],
      "redBans": [...],
      "bluePicks": [...],
      "redPicks": [...]
    }
    `


- `GAME_FINISH_UPDATE` 발행 Payload:
