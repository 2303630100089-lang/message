package com.pulsesphere.chat.repository

import com.pulsesphere.chat.domain.Conversation
import com.pulsesphere.chat.domain.ConversationType
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ConversationRepository : ReactiveMongoRepository<Conversation, String> {
    fun findByParticipantsContains(userId: String): Flux<Conversation>

    @Query("{ 'type': 'DIRECT', 'participants': { \$all: ?0, \$size: 2 } }")
    fun findDirectConversation(participants: List<String>): Mono<Conversation>
}
