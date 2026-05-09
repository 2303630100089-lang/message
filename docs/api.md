# API Design

## REST Endpoints (Gateway)
- `POST /auth/signup`
- `POST /auth/login`
- `POST /auth/verify-otp`
- `POST /auth/refresh`
- `GET /users/me`
- `GET /chats`
- `POST /chats/{chatId}/messages`
- `GET /feeds/home`
- `POST /media/upload`

## GraphQL
- `query feed(home: FeedInput)`
- `mutation createPost(input: PostInput)`

## gRPC (Internal)
- Realtime fanout
- Messaging persistence
- Media processing

## WebSocket/STOMP
- `/ws` for realtime connections
- Topics: `/topic/presence`, `/topic/chat.{chatId}`

## Auth Flow
- JWT access token + refresh token rotation
- Device-bound sessions with risk scoring
