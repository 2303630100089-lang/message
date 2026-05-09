package com.pulsesphere.chat.domain

import java.time.Instant
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document("messages")
data class Message(
    @Id val id: String? = null,
    @Indexed val conversationId: String,
    @Indexed val senderId: String,
    val contentType: String,
    val content: String? = null,
    val mediaIds: List<String> = emptyList(),
    val createdAt: Instant = Instant.now(),
)
