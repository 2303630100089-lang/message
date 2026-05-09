package com.pulsesphere.user.controller

import com.pulsesphere.common.security.JwtPrincipal
import com.pulsesphere.user.service.UpdateProfileRequest
import com.pulsesphere.user.service.UserProfileResponse
import com.pulsesphere.user.service.UserProfileService
import jakarta.validation.Valid
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/users")
class UserProfileController(
    private val userProfileService: UserProfileService,
) {
    @GetMapping("/me")
    suspend fun me(@AuthenticationPrincipal principal: JwtPrincipal): UserProfileResponse =
        userProfileService.getProfile(principal.userId)

    @PutMapping("/me")
    suspend fun update(
        @AuthenticationPrincipal principal: JwtPrincipal,
        @Valid @RequestBody request: UpdateProfileRequest,
    ): UserProfileResponse = userProfileService.updateProfile(principal.userId, request)

    @GetMapping("/{userId}")
    suspend fun getProfile(@PathVariable userId: String): UserProfileResponse =
        userProfileService.getProfile(userId)
}
