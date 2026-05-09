package com.pulsesphere.chat.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.pulsesphere.chat.domain.Conversation
import com.pulsesphere.chat.domain.ConversationType
import com.pulsesphere.chat.domain.Message
import com.pulsesphere.chat.repository.ConversationRepository
import com.pulsesphere.chat.repository.MessageRepository
import com.pulsesphere.common.events.EventEnvelope
import com.pulsesphere.common.events.MessageCreatedEvent
import com.pulsesphere.common.events.Topics
import java.time.Instant
import java.util.UUID
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.data.domain.PageRequest
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class ChatService(
    private val conversationRepository: ConversationRepository,
    private val messageRepository: MessageRepository,
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val objectMapper: ObjectMapper,
) {
    suspend fun createDirectConversation(userId: String, participantId: String): ConversationResponse {
        require(userId != participantId) { "Cannot create a direct conversation with yourself" }
        val participants = listOf(userId, participantId).sorted()
        val existing = conversationRepository.findDirectConversation(participants).awaitSingleOrNull()
        if (existing != null) return existing.toResponse()
        val conversation = conversationRepository.save(
            Conversation(type = ConversationType.DIRECT, participants = participants),
        ).awaitSingle()
        return conversation.toResponse()
    }

    suspend fun createGroupConversation(userId: String, request: CreateGroupConversationRequest): ConversationResponse {
        val participants = (request.participantIds + userId).distinct()
        val conversation = conversationRepository.save(
            Conversation(
                type = ConversationType.GROUP,
                participants = participants,
                title = request.title,
            ),
        ).awaitSingle()
        return conversation.toResponse()
    }

    suspend fun listConversations(userId: String): List<ConversationResponse> =
        conversationRepository.findByParticipantsContains(userId)
            .map { it.toResponse() }
            .collectList()
            .awaitSingle()

    suspend fun listMessages(userId: String, conversationId: String, limit: Int, offset: Int): List<MessageResponse> {
        val conversation = conversationRepository.findById(conversationId).awaitSingleOrNull()
            ?: throw IllegalArgumentException("Conversation not found")
        require(conversation.participants.contains(userId)) { "Not a member of this conversation" }
        val pageable = PageRequest.of(offset / limit, limit)
        return messageRepository.findByConversationIdOrderByCreatedAtDesc(conversationId, pageable)
            .map { it.toResponse() }
            .collectList()
            .awaitSingle()
    }

    suspend fun sendMessage(userId: String, conversationId: String, request: SendMessageRequest): MessageResponse {
        val conversation = conversationRepository.findById(conversationId).awaitSingleOrNull()
            ?: throw IllegalArgumentException("Conversation not found")
        require(conversation.participants.contains(userId)) { "Not a member of this conversation" }
        val message = messageRepository.save(
            Message(
                conversationId = conversationId,
                senderId = userId,
                contentType = request.contentType,
                content = request.content,
                mediaIds = request.mediaIds,
            ),
        ).awaitSingle()
        publishMessageEvent(conversation, message)
        return message.toResponse()
    }

    private fun publishMessageEvent(conversation: Conversation, message: Message) {
        val recipients = conversation.participants.filterNot { it == message.senderId }
        val event = EventEnvelope(
            id = UUID.randomUUID().toString(),
            type = "MessageCreated",
            createdAt = Instant.now(),
            payload = MessageCreatedEvent(
                messageId = message.id ?: "",
                conversationId = message.conversationId,
                senderId = message.senderId,
                recipientIds = recipients,
                contentType = message.contentType,
                contentPreview = message.content?.take(120),
                mediaIds = message.mediaIds,
                createdAt = message.createdAt,
            ),
        )
        kafkaTemplate.send(Topics.CHAT_EVENTS, message.conversationId, objectMapper.writeValueAsString(event))
    }
}

data class CreateGroupConversationRequest(
    val title: String,
    val participantIds: List<String>,
)

data class SendMessageRequest(
    val contentType: String,
    val content: String?,
    val mediaIds: List<String> = emptyList(),
)

data class ConversationResponse(
    val id: String,
    val type: ConversationType,
    val participants: List<String>,
    val title: String?,
    val createdAt: Instant,
    val updatedAt: Instant,
)

data class MessageResponse(
    val id: String,
    val conversationId: String,
    val senderId: String,
    val contentType: String,
    val content: String?,
    val mediaIds: List<String>,
    val createdAt: Instant,
)

private fun Conversation.toResponse() = ConversationResponse(
    id = id ?: "",
    type = type,
    participants = participants,
    title = title,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

private fun Message.toResponse() = MessageResponse(
    id = id ?: "",
    conversationId = conversationId,
    senderId = senderId,
    contentType = contentType,
    content = content,
    mediaIds = mediaIds,
    createdAt = createdAt,
)
