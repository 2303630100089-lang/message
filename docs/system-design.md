# System Design

## High-Level Architecture
- API Gateway routes REST/GraphQL traffic to microservices.
- Realtime service manages WebSocket/STOMP, presence, and typing.
- Messaging service handles chat persistence and sync.
- Media service manages uploads, transcoding, and CDN distribution.
- Search service indexes content in Elasticsearch.
- Notifications service dispatches push and in-app notifications.

## Service Boundaries
- **Auth Service**: identity, sessions, devices, passkeys.
- **Messaging Service**: chat threads, message lifecycle, read states.
- **Realtime Service**: socket connections, presence, fanout.
- **Media Service**: uploads, thumbnails, streaming metadata.
- **Search Service**: index updates, federated search queries.
- **Notifications Service**: APNS/FCM, email/SMS, digest.
- **Gateway**: rate limits, auth middleware, request shaping.

## Data Flow
1. Client authenticates and opens WebSocket.
2. Messages go to Messaging Service via REST/gRPC.
3. Messaging emits events to Kafka; Realtime consumes and pushes via WebSocket.
4. Search consumes indexing events into Elasticsearch.

## Realtime Topology
- WebSocket clusters with Redis pub/sub and Kafka backplane.
- Presence stored in Redis with TTL heartbeats.
- Edge routing with sticky sessions and socket failover.

## Deployment Topology
- Multi-region Kubernetes clusters with autoscaling.
- CDN fronting media assets.
- Zero-downtime rollout with blue/green or canary.
