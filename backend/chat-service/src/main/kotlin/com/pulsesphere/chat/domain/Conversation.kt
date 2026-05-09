package com.pulsesphere.chat.domain

import java.time.Instant
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document("conversations")
data class Conversation(
    @Id val id: String? = null,
    val type: ConversationType,
    @Indexed val participants: List<String>,
    val title: String? = null,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now(),
)

enum class ConversationType {
    DIRECT,
    GROUP,
}
