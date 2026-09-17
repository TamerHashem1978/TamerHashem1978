package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme =
  darkColorScheme(
    primary = SabaqGoldPrimary,
    onPrimary = SabaqBackground,
    primaryContainer = SabaqGoldButton,
    onPrimaryContainer = Color.White,
    secondary = SabaqGreenBright,
    onSecondary = Color.White,
    secondaryContainer = SabaqGreenPrimary,
    onSecondaryContainer = SabaqTextPrimary,
    tertiary = SabaqGoldWarm,
    onTertiary = Color.Black,
    background = SabaqBackground,
    onBackground = SabaqTextPrimary,
    surface = SabaqGreenSection,
    onSurface = SabaqTextPrimary,
    surfaceVariant = SabaqGreenCard,
    onSurfaceVariant = SabaqTextSecondary,
    outline = Color(0xFF134529),
    error = SabaqTextError,
    onError = Color.White
  )

private val LightColorScheme =
  lightColorScheme(
    primary = SabaqGoldBronze,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFF0B3),
    onPrimaryContainer = SabaqGoldShadow,
    secondary = SabaqGreenPrimary,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD4EED8),
    onSecondaryContainer = Color(0xFF03220A),
    tertiary = SabaqGoldPrimary,
    onTertiary = Color.Black,
    background = Color(0xFFF9FAF8),
    onBackground = Color(0xFF141F16),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF141F16),
    surfaceVariant = Color(0xFFEEF4EE),
    onSurfaceVariant = Color(0xFF425646),
    outline = Color(0xFFBDD0BF),
    error = SabaqTextError,
    onError = Color.White
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true, // Sabaq is designed for dark neon/gold aesthetic by default
  dynamicColor: Boolean = false, // Preserve brand identity for Sabaq
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
  MaterialTheme(
    colorScheme = colorScheme,
    typography = SabaqTypography,
    content = content
  )
}


