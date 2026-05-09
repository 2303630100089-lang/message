package com.pulsesphere.media.domain

import java.time.Instant
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document("media_objects")
data class MediaObject(
    @Id val id: String? = null,
    @Indexed val userId: String,
    val filename: String,
    val contentType: String,
    val size: Long,
    val storagePath: String,
    val createdAt: Instant = Instant.now(),
)
