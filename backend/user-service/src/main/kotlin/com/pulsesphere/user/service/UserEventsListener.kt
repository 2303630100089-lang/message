package com.pulsesphere.user.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.pulsesphere.common.events.EventEnvelope
import com.pulsesphere.common.events.UserRegisteredEvent
import kotlinx.coroutines.runBlocking
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class UserEventsListener(
    private val objectMapper: ObjectMapper,
    private val userProfileService: UserProfileService,
) {
    @KafkaListener(topics = [com.pulsesphere.common.events.Topics.USER_EVENTS], groupId = "user-service")
    fun onUserRegistered(message: String) = runBlocking {
        val type = objectMapper.typeFactory.constructParametricType(EventEnvelope::class.java, UserRegisteredEvent::class.java)
        val event: EventEnvelope<UserRegisteredEvent> = objectMapper.readValue(message, type)
        userProfileService.createProfile(event)
    }
}
