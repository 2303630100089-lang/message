package com.pulsesphere.media.config

import jakarta.validation.constraints.NotBlank
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated

@Validated
@ConfigurationProperties("media.storage")
data class MediaStorageProperties(
    @field:NotBlank val path: String,
)
