package com.pulsesphere.common.events

import java.time.Instant

object Topics {
    const val USER_EVENTS = "user.events"
    const val CHAT_EVENTS = "chat.events"
    const val MEDIA_EVENTS = "media.events"
    const val NOTIFICATION_EVENTS = "notification.events"
    const val FEED_EVENTS = "feed.events"
}

data class EventEnvelope<T>(
    val id: String,
    val type: String,
    val createdAt: Instant,
    val payload: T,
)

data class UserRegisteredEvent(
    val userId: String,
    val email: String,
    val createdAt: Instant,
)

data class MessageCreatedEvent(
    val messageId: String,
    val conversationId: String,
    val senderId: String,
    val recipientIds: List<String>,
    val contentType: String,
    val contentPreview: String?,
    val mediaIds: List<String>,
    val createdAt: Instant,
)

data class MediaUploadedEvent(
    val mediaId: String,
    val userId: String,
    val contentType: String,
    val size: Long,
    val createdAt: Instant,
)

data class NotificationCreatedEvent(
    val notificationId: String,
    val userId: String,
    val title: String,
    val body: String,
    val createdAt: Instant,
)
