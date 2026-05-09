package com.pulsesphere.media

import com.pulsesphere.media.config.JwtProperties
import com.pulsesphere.media.config.MediaStorageProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties::class, MediaStorageProperties::class)
class MediaServiceApplication

fun main(args: Array<String>) {
    runApplication<MediaServiceApplication>(*args)
}
