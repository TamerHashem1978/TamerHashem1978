package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.R

// Amiri Quran font - used for decorative background Arabic characters with diacritics
val AmiriQuranFontFamily = FontFamily(
  Font(R.font.amiri_quran, FontWeight.Normal)
)

// Aref Ruqaa font - used for titles, headings, and Sabaq brand identity
val ArefRuqaaFontFamily = FontFamily(
  Font(R.font.aref_ruqaa, FontWeight.Normal)
)

val SabaqTypography = Typography(
  displayLarge = TextStyle(
    fontFamily = ArefRuqaaFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 36.sp,
    lineHeight = 44.sp,
    color = SabaqGoldPrimary
  ),
  displayMedium = TextStyle(
    fontFamily = ArefRuqaaFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 30.sp,
    lineHeight = 38.sp,
    color = SabaqGoldPrimary
  ),
  displaySmall = TextStyle(
    fontFamily = ArefRuqaaFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 24.sp,
    lineHeight = 32.sp,
    color = SabaqGoldPrimary
  ),
  headlineLarge = TextStyle(
    fontFamily = ArefRuqaaFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 24.sp,
    lineHeight = 32.sp,
    color = SabaqGoldLight
  ),
  headlineMedium = TextStyle(
    fontFamily = ArefRuqaaFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 20.sp,
    lineHeight = 28.sp,
    color = SabaqGoldLight
  ),
  headlineSmall = TextStyle(
    fontFamily = ArefRuqaaFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 18.sp,
    lineHeight = 26.sp,
    color = SabaqGoldLight
  ),
  titleLarge = TextStyle(
    fontFamily = ArefRuqaaFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 22.sp,
    lineHeight = 28.sp,
    color = SabaqTextPrimary
  ),
  titleMedium = TextStyle(
    fontFamily = ArefRuqaaFontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 17.sp,
    lineHeight = 24.sp,
    color = SabaqTextPrimary
  ),
  titleSmall = TextStyle(
    fontFamily = ArefRuqaaFontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 15.sp,
    lineHeight = 22.sp,
    color = SabaqTextSecondary
  ),
  bodyLarge = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp,
    lineHeight = 24.sp,
    color = SabaqTextPrimary
  ),
  bodyMedium = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
    lineHeight = 20.sp,
    color = SabaqTextPrimary
  ),
  bodySmall = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Normal,
    fontSize = 12.sp,
    lineHeight = 16.sp,
    color = SabaqTextSecondary
  ),
  labelLarge = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.SemiBold,
    fontSize = 14.sp,
    lineHeight = 20.sp,
    color = SabaqGoldPrimary
  ),
  labelMedium = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Medium,
    fontSize = 12.sp,
    lineHeight = 16.sp,
    color = SabaqTextSecondary
  ),
  labelSmall = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Medium,
    fontSize = 11.sp,
    lineHeight = 14.sp,
    color = SabaqTextMuted
  )
)

val Typography = SabaqTypography

