package com.pulsesphere.domain

data class UserProfile(
  val id: String,
  val handle: String,
  val displayName: String,
  val avatarUrl: String?
)
