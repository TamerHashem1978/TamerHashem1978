package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// =========================================================================
// Sabaq Brand Palette - الذهبي (Gold / Amber)
// =========================================================================
val SabaqGoldPrimary = Color(0xFFF9AD10)      // الأساسي
val SabaqGoldHeart = Color(0xFFF4A401)        // القلب الحيّ
val SabaqGoldLight = Color(0xFFF8DB5F)        // الفاتح
val SabaqGoldHighlight = Color(0xFFFFF378)    // اللمعة
val SabaqGoldWarm = Color(0xFFF7C443)         // الدافئ
val SabaqGoldButton = Color(0xFFE09314)       // المتوسط (الأزرار)
val SabaqGoldBronze = Color(0xFFD8870E)       // البرونزي
val SabaqGoldDeep = Color(0xFFB4680D)         // العميق ⚠️ أسطح فقط (not for text)
val SabaqGoldShadow = Color(0xFF8A4B06)       // الظل ⚠️ أسطح فقط (not for text)

// =========================================================================
// Sabaq Brand Palette - الأخضر والخلفيات (Green & Backgrounds)
// =========================================================================
val SabaqGreenBright = Color(0xFF30A933)      // الأخضر الساطع
val SabaqGreenPrimary = Color(0xFF0D6421)     // الأخضر الأساسي
val SabaqGreenCard = Color(0xFF05311C)        // العميق (الكروت)
val SabaqGreenSection = Color(0xFF052316)     // الأغمق (الأقسام)
val SabaqBackground = Color(0xFF070B07)       // خلفية التطبيق

// =========================================================================
// Sabaq Text Colors (النصوص)
// =========================================================================
val SabaqTextPrimary = Color(0xFFD5E5D2)      // النصوص الأساسية
val SabaqTextSecondary = Color(0xFF8DA18F)    // النصوص الثانوية
val SabaqTextMuted = Color(0xFF6F8272)        // النصوص الباهتة / الملاحظات
val SabaqTextError = Color(0xFFE05C5C)        // الأخطاء والتحذيرات

// =========================================================================
// Sabaq Gradients
// =========================================================================
// تدرج العناوين والنصوص البارزة:
// linear-gradient(135deg,#D8870E 0%,#F4A401 18%,#F9AD10 30%,#F7C443 42%, #FFF378 52%,#F7C443 62%,#F9AD10 76%,#D8870E 100%)
val SabaqHeadingGradient = Brush.linearGradient(
  colorStops = arrayOf(
    0.00f to SabaqGoldBronze,
    0.18f to SabaqGoldHeart,
    0.30f to SabaqGoldPrimary,
    0.42f to SabaqGoldWarm,
    0.52f to SabaqGoldHighlight,
    0.62f to SabaqGoldWarm,
    0.76f to SabaqGoldPrimary,
    1.00f to SabaqGoldBronze
  )
)

// تدرج الأزرار والأسطح الذهبية:
// linear-gradient(135deg,#8A4B06 0%,#B4680D 14%,#E09314 30%,#F9AD10 44%, #FFF378 54%,#F7C443 64%,#E09314 78%,#8A4B06 100%)
val SabaqButtonGradient = Brush.linearGradient(
  colorStops = arrayOf(
    0.00f to SabaqGoldShadow,
    0.14f to SabaqGoldDeep,
    0.30f to SabaqGoldButton,
    0.44f to SabaqGoldPrimary,
    0.54f to SabaqGoldHighlight,
    0.64f to SabaqGoldWarm,
    0.78f to SabaqGoldButton,
    1.00f to SabaqGoldShadow
  )
)

// تدرج كروت الأقسام الخضراء
val SabaqCardGradient = Brush.verticalGradient(
  colors = listOf(SabaqGreenCard, SabaqGreenSection)
)

// تدرج خفيف للحواف والكروت
val SabaqBorderGradient = Brush.linearGradient(
  listOf(SabaqGoldPrimary.copy(alpha = 0.6f), SabaqGreenBright.copy(alpha = 0.4f))
)

// =========================================================================
// Backward-compatibility Aliases for Existing Views
// =========================================================================
val SabaqCyan = SabaqGoldPrimary
val SabaqBlue = SabaqGreenBright
val SabaqDarkNavy = SabaqBackground
val SabaqSurfaceDark = SabaqGreenSection
val SabaqCardDark = SabaqGreenCard
val SabaqBorderDark = Color(0xFF134529)

val SabaqEmerald = SabaqGreenBright
val SabaqPurple = SabaqGoldLight
val SabaqAmber = SabaqGoldPrimary
val SabaqRose = SabaqTextError

val SabaqTextPrimaryDark = SabaqTextPrimary
val SabaqTextSecondaryDark = SabaqTextSecondary
val SabaqTextMutedDark = SabaqTextMuted

val SabaqBackgroundLight = SabaqBackground
val SabaqSurfaceLight = SabaqGreenSection
val SabaqCardLight = SabaqGreenCard
val SabaqBorderLight = Color(0xFF134529)
val SabaqTextPrimaryLight = SabaqTextPrimary
val SabaqTextSecondaryLight = SabaqTextSecondary

