package com.pulsesphere.designsystem

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColors = darkColorScheme(
  primary = Color(0xFF38F6FF),
  secondary = Color(0xFF8B5CF6),
  background = Color(0xFF050508),
  surface = Color(0xFF0B0E16),
  onPrimary = Color(0xFF050508),
  onBackground = Color(0xFFF7F8FF)
)

@Composable
fun PulseSphereTheme(content: @Composable () -> Unit) {
  MaterialTheme(
    colorScheme = DarkColors,
    content = content
  )
}
