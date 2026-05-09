package com.pulsesphere.chat.controller

import com.pulsesphere.chat.service.ChatService
import com.pulsesphere.chat.service.ConversationResponse
import com.pulsesphere.chat.service.CreateGroupConversationRequest
import com.pulsesphere.chat.service.MessageResponse
import com.pulsesphere.chat.service.SendMessageRequest
import com.pulsesphere.common.security.JwtPrincipal
import jakarta.validation.Valid
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/conversations")
class ChatController(
    private val chatService: ChatService,
) {
    @PostMapping("/direct")
    suspend fun createDirect(
        @AuthenticationPrincipal principal: JwtPrincipal,
        @Valid @RequestBody request: CreateDirectConversationRequest,
    ): ConversationResponse = chatService.createDirectConversation(principal.userId, request.participantId)

    @PostMapping("/group")
    suspend fun createGroup(
        @AuthenticationPrincipal principal: JwtPrincipal,
        @Valid @RequestBody request: CreateGroupConversationRequest,
    ): ConversationResponse = chatService.createGroupConversation(principal.userId, request)

    @GetMapping
    suspend fun list(@AuthenticationPrincipal principal: JwtPrincipal): List<ConversationResponse> =
        chatService.listConversations(principal.userId)

    @GetMapping("/{conversationId}/messages")
    suspend fun messages(
        @AuthenticationPrincipal principal: JwtPrincipal,
        @PathVariable conversationId: String,
        @RequestParam(name = "limit", defaultValue = "50") limit: Int,
        @RequestParam(name = "offset", defaultValue = "0") offset: Int,
    ): List<MessageResponse> {
        return chatService.listMessages(principal.userId, conversationId, limit, offset)
    }

    @PostMapping("/{conversationId}/messages")
    suspend fun sendMessage(
        @AuthenticationPrincipal principal: JwtPrincipal,
        @PathVariable conversationId: String,
        @Valid @RequestBody request: SendMessageRequest,
    ): MessageResponse = chatService.sendMessage(principal.userId, conversationId, request)
}

data class CreateDirectConversationRequest(
    val participantId: String,
)
