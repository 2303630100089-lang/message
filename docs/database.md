# Database Design

## MongoDB Collections
- users, profiles, devices, sessions
- chats, messages, message_reads
- media_assets, media_variants
- feeds, posts, comments
- communities, channels, groups
- notifications
- followers
- analytics
- ai_memory
- wallets, payments, subscriptions

## Indexing Strategy
- Compound indexes on (chatId, createdAt) for messages
- TTL index on ephemeral messages and presence
- Text indexes for search fallback

## Sharding Strategy
- users and messages sharded by userId/chatId hash
- media_assets sharded by ownerId

## Replication & Backup
- Multi-region replica sets
- Nightly snapshots + point-in-time recovery
