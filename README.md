# Fearless Draft API 명세서

## 📌 Base URL
```
/api
```

---

## ✅ Team API

### 🔹 전체 팀 조회
- **URL**: `/teams`
- **Method**: `GET`
- **Response**:
```json
{
  "status": "OK",
  "message": "모든 팀 조회 성공",
  "data": [ ... 팀 리스트 ... ]
}
```

### 🔹 팀 생성
- **URL**: `/teams`
- **Method**: `POST`
- **Request Body**:
```json
{
  "name": "팀 이름",
  "coach": "감독 이름"
}
```
- **Response**:
```json
{
  "status": "OK",
  "message": "팀 생성 성공",
  "data": { ... 생성된 팀 정보 ... }
}
```

### 🔹 팀 수정
- **URL**: `/teams`
- **Method**: `PUT`
- **Request Body**:
```json
{
  "id": 1,
  "name": "변경된 팀 이름",
  "coach": "변경된 감독 이름"
}
```
- **Response**:
```json
{
  "status": "OK",
  "message": "팀 수정 성공",
  "data": { ... 수정된 팀 정보 ... }
}
```

### 🔹 팀 삭제
- **URL**: `/teams/{teamId}`
- **Method**: `DELETE`
- **Response**:
```json
{
  "status": "OK",
  "message": "팀 삭제 성공",
  "data": null
}
```

---

## ✅ Match API

### 🔹 매치 생성
- **URL**: `/matches/create`
- **Method**: `POST`
- **Request Body**:
```json
{
  "matchName": "매치 이름",
  "matchPassword": "1234",
  "teamAId": 1,
  "teamBId": 2,
  "numberOfGames": 3,
  "banRule": "STANDARD",
  "matchType": "BO3",
  "allowSpectators": true
}
```
- **Response**:
```json
{
  ... 생성된 매치 정보 ...
}
```

### 🔹 매치 단건 조회
- **URL**: `/matches/{matchId}`
- **Method**: `GET`

### 🔹 매치 전체 조회
- **URL**: `/matches`
- **Method**: `GET`

### 🔹 매치 삭제
- **URL**: `/matches/{matchId}`
- **Method**: `DELETE`

---

## ✅ Game API

### 🔹 게임 시작
- **URL**: `/games/start`
- **Method**: `POST`
- **Request Body**:
```json
{
  "matchId": 1,
  "gameOrder": 1
}
```
- **Response**:
```json
{
  "status": "OK",
  "message": "게임이 성공적으로 시작되었습니다.",
  "data": { ... 게임 정보 ... }
}
```

### 🔹 게임 결과 등록
- **URL**: `/games/result`
- **Method**: `POST`
- **Request Body**:
```json
{
  "gameId": 1,
  "winnerTeamId": 2
}
```
- **Response**:
```json
{
  "status": "OK",
  "message": "게임 결과가 정상적으로 기록되었습니다.",
  "data": null
}
```

---

## ✅ BanPick WebSocket API

### 🔹 밴픽 선택
- **URL**: `/app/select/{gameId}` (STOMP 메시지)
- **Method**: WebSocket Message
- **Payload**:
```json
{
  "teamId": 1,
  "championId": 101,
  "actionType": "BAN"
}
```

### 🔹 밴픽 진행 상황 구독
- **URL**: `/topic/banpick/progress/{gameId}`

### 🔹 밴픽 완료 구독
- **URL**: `/topic/banpick/complete/{gameId}`

### 🔹 에러 메시지 구독
- **URL**: `/topic/banpick/error/{gameId}`