🔹 1. 전체 팀 목록 조회
Method: GET

URL: /api/teams

설명: 모든 팀 정보를 조회합니다.

✅ Request
없음

✅ Response
```json
복사
편집
{
"status": "OK",
"message": "모든 팀 조회 성공",
"data": [
{
"id": 1,
"name": "Team Alpha"
},
{
"id": 2,
"name": "Team Beta"
}
]
}`
🔹 2. 팀 생성
Method: POST

URL: /api/teams

설명: 새로운 팀을 생성합니다.

✅ Request Body
json
복사
편집
{
"name": "Team Alpha"
}
✅ Response
json
복사
편집
{
"status": "OK",
"message": "팀 생성 성공",
"data": {
"id": 1,
"name": "Team Alpha"
}
}
🔹 3. 팀 수정
Method: PUT

URL: /api/teams

설명: 기존 팀의 이름을 수정합니다.

✅ Request Body
json
복사
편집
{
"id": 1,
"name": "Team Alpha Updated"
}
✅ Response
json
복사
편집
{
"status": "OK",
"message": "팀 수정 성공",
"data": {
"id": 1,
"name": "Team Alpha Updated"
}
}
🔹 4. 팀 삭제
Method: DELETE

URL: /api/teams/{teamId}

설명: 팀을 삭제합니다.

✅ Path Variable
이름	타입	설명
teamId	Long	삭제할 팀 ID

✅ Response
json
복사
편집
{
"status": "OK",
"message": "팀 삭제 성공",
"data": null
}
📌 응답 공통 구조 (CommonResDto)
json
복사
편집
{
"status": "OK", // 또는 "BAD_REQUEST", "INTERNAL_SERVER_ERROR" 등
"message": "응답 메시지",
"data": ... // 요청에 따른 데이터 객체
}