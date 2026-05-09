package com.pulsesphere.chat

import com.pulsesphere.chat.config.JwtProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties::class)
class ChatServiceApplication

fun main(args: Array<String>) {
    runApplication<ChatServiceApplication>(*args)
}
