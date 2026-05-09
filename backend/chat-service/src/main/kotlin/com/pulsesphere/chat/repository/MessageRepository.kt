package com.pulsesphere.chat.repository

import com.pulsesphere.chat.domain.Message
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux

interface MessageRepository : ReactiveMongoRepository<Message, String> {
    fun findByConversationIdOrderByCreatedAtDesc(conversationId: String, pageable: Pageable): Flux<Message>
}
