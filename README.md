1. `POST /api/rooms`
   `json                                                                                                                                                                                                                                                             
               {                                                                                                                                                                                                                                                     
                 "statusCode": 201,                                                                                                                                                                                                                                  
                 "statusMessage": "방 생성 성공",                                                                                                                                                                                                                    
                 "result": {                                                                                                                                                                                                                                         
                   "roomId": "string",                                                                                                                                                                                                                               
                   "roomName": "string"                                                                                                                                                                                                                              
                 }                                                                                                                                                                                                                                                   
               }                                                                                                                                                                                                                                                     
               `


2. `GET /api/rooms/{roomId}`
3. `DELETE /api/rooms`
   `json                                                                                                                                                                                                                                                             
               {                                                                                                                                                                                                                                                     
                 "statusCode": 200,                                                                                                                                                                                                                                  
                 "statusMessage": "방 삭제 성공",                                                                                                                                                                                                                    
                 "result": null                                                                                                                                                                                                                                      
               }                                                                                                                                                                                                                                                     
               `

### MatchController - /api/matches


1. `POST /api/matches`
2. `GET /api/matches/room/{roomId}`
1. `@MessageMapping("/banpick/{roomId}")`
   To make the documentation complete, I need the structure of the DTOs:
*   CreateMatchRequest
*   MatchResponse
*   ChampionSelectAction
*   BanPickState


I'll use glob to find the paths to these files and then read_many_files to get their content.네, 컨트롤러를 바탕으로 API 명세서를 작성해 드리겠습니다.

  ---                                                                                                                                                                                                                                                               

## FearlessDraft API 명세서

### 1. Room API

Base URL: /api/rooms

#### 1.1 방 생성


- Endpoint: POST /                                                                                                                                                                                                                                               
  `json                                                                                                                                                                                                                                                             
      {                                                                                                                                                                                                                                                             
        "statusCode": 201,                                                                                                                                                                                                                                          
        "statusMessage": "방 생성 성공",                                                                                                                                                                                                                            
        "result": {                                                                                                                                                                                                                                                 
          "roomId": "string",                                                                                                                                                                                                                                       
          "roomName": "string"                                                                                                                                                                                                                                      
        }                                                                                                                                                                                                                                                           
      }                                                                                                                                                                                                                                                             
      `

#### 1.2 방 조회


- Endpoint: GET /{roomId}                                                                                                                                                                                                                                        
  `json                                                                                                                                                                                                                                                             
      {                                                                                                                                                                                                                                                             
        "statusCode": 200,                                                                                                                                                                                                                                          
        "statusMessage": "방 조회 성공",                                                                                                                                                                                                                            
        "result": {                                                                                                                                                                                                                                                 
          "roomId": "string",                                                                                                                                                                                                                                       
          "roomName": "string"                                                                                                                                                                                                                                      
        }                                                                                                                                                                                                                                                           
      }                                                                                                                                                                                                                                                             
      `

#### 1.3 방 삭제


- Endpoint: DELETE ?roomId={roomId}                                                                                                                                                                                                                              
  `json                                                                                                                                                                                                                                                             
      {                                                                                                                                                                                                                                                             
        "statusCode": 200,                                                                                                                                                                                                                                          
        "statusMessage": "방 삭제 성공",                                                                                                                                                                                                                            
        "result": null                                                                                                                                                                                                                                              
      }                                                                                                                                                                                                                                                             
      `

  ---                                                                                                                                                                                                                                                               

### 2. Match API

Base URL: /api/matches

#### 2.1 매치 생성


- Endpoint: POST /                                                                                                                                                                                                                                               
  `json                                                                                                                                                                                                                                                             
      {                                                                                                                                                                                                                                                             
        "statusCode": 201,                                                                                                                                                                                                                                          
        "statusMessage": "매치가 성공적으로 생성되었습니다.",                                                                                                                                                                                                       
        "result": {                                                                                                                                                                                                                                                 
          "matchId": "number",                                                                                                                                                                                                                                      
          "roomId": "string",                                                                                                                                                                                                                                       
          "matchType": "string",                                                                                                                                                                                                                                    
          "blueTeamName": "string",                                                                                                                                                                                                                                 
          "redTeamName": "string"                                                                                                                                                                                                                                   
        }                                                                                                                                                                                                                                                           
      }                                                                                                                                                                                                                                                             
      `

#### 2.2 매치 상태 조회


- Endpoint: GET /room/{roomId}                                                                                                                                                                                                                                   
  `json                                                                                                                                                                                                                                                             
      {                                                                                                                                                                                                                                                             
        "statusCode": 200,                                                                                                                                                                                                                                          
        "statusMessage": "매치 상태 조회 성공",                                                                                                                                                                                                                     
        "result": {                                                                                                                                                                                                                                                 
          "matchId": "number",                                                                                                                                                                                                                                      
          "roomId": "string",                                                                                                                                                                                                                                       
          "matchType": "string",                                                                                                                                                                                                                                    
          "blueTeamName": "string",                                                                                                                                                                                                                                 
          "redTeamName": "string"                                                                                                                                                                                                                                   
        }                                                                                                                                                                                                                                                           
      }                                                                                                                                                                                                                                                             
      `

  ---                                                                                                                                                                                                                                                               

### 3. Ban-Pick API (WebSocket)

Prefix: /app                                                                                                                                                                                                                                                      
Topic: /topic

#### 3.1 챔피언 선택/밴 액션 처리

- Endpoint (Subscribe & Send): /app/banpick/{roomId}
  `json
  {
  "phase": "string", // 예: "BAN", "PICK"
  "turn": "string", // 예: "BLUE", "RED"
  "blueTeam": {
  "bans": ["number"],
  "picks": ["number"]
  },
  "redTeam": {
  "bans": ["number"],
  "picks": ["number"]
  },
  "timer": "number"
  }