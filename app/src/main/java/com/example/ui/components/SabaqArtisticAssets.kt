package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmiriQuranFontFamily
import com.example.ui.theme.ArefRuqaaFontFamily
import com.example.ui.theme.SabaqBackground
import com.example.ui.theme.SabaqButtonGradient
import com.example.ui.theme.SabaqGoldBronze
import com.example.ui.theme.SabaqGoldButton
import com.example.ui.theme.SabaqGoldDeep
import com.example.ui.theme.SabaqGoldHeart
import com.example.ui.theme.SabaqGoldHighlight
import com.example.ui.theme.SabaqGoldLight
import com.example.ui.theme.SabaqGoldPrimary
import com.example.ui.theme.SabaqGoldShadow
import com.example.ui.theme.SabaqGoldWarm
import com.example.ui.theme.SabaqGreenBright
import com.example.ui.theme.SabaqGreenCard
import com.example.ui.theme.SabaqGreenPrimary
import com.example.ui.theme.SabaqGreenSection
import com.example.ui.theme.SabaqHeadingGradient
import com.example.ui.theme.SabaqTextPrimary

/**
 * خلفية فنية تراثية حديثة تستخدم خط Amiri Quran للأحرف العربية المشكولة
 * والعبارات القرآنية والفصيحة كعلامات مائية خافتة في الخلفية
 */
@Composable
fun AmiriQuranBackgroundWatermark(
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .fillMaxSize()
      .background(SabaqBackground)
  ) {
    // شبكة الحروف والتشكيلات بخط Amiri Quran كعلامة مائية خفيفة جداً
    Text(
      text = "فَاسْتَبِقُوا الْخَيْرَاتِ",
      fontFamily = AmiriQuranFontFamily,
      fontSize = 58.sp,
      color = SabaqGoldPrimary.copy(alpha = 0.038f),
      modifier = Modifier
        .align(Alignment.TopEnd)
        .offset(x = 40.dp, y = 30.dp)
        .rotate(-14f)
    )

    Text(
      text = "سَبْقٌ ۝",
      fontFamily = AmiriQuranFontFamily,
      fontSize = 82.sp,
      color = SabaqGreenBright.copy(alpha = 0.032f),
      modifier = Modifier
        .align(Alignment.CenterStart)
        .offset(x = (-30).dp, y = (-60).dp)
        .rotate(12f)
    )

    Text(
      text = "ن ۚ وَالْقَلَمِ وَمَا يَسْطُرُونَ",
      fontFamily = AmiriQuranFontFamily,
      fontSize = 46.sp,
      color = SabaqGoldWarm.copy(alpha = 0.035f),
      modifier = Modifier
        .align(Alignment.CenterEnd)
        .offset(x = 50.dp, y = 140.dp)
        .rotate(-8f)
    )

    Text(
      text = "سَابِقُونَ بِالْخَيْرَاتِ",
      fontFamily = AmiriQuranFontFamily,
      fontSize = 52.sp,
      color = SabaqGoldPrimary.copy(alpha = 0.03f),
      modifier = Modifier
        .align(Alignment.BottomStart)
        .offset(x = (-20).dp, y = (-80).dp)
        .rotate(6f)
    )

    // تشكيلات مفردة متناثرة
    Text(
      text = "ـَّ ـٰ ـۢ ـۡ",
      fontFamily = AmiriQuranFontFamily,
      fontSize = 44.sp,
      color = SabaqGoldLight.copy(alpha = 0.035f),
      modifier = Modifier
        .align(Alignment.TopStart)
        .offset(x = 20.dp, y = 180.dp)
    )
  }
}

/**
 * روبوت سبق الذكي (Sabaq AI Mascot Robot)
 * مُمثل ثلاثي الأبعاد برسم متقن متناغم مع لوحة ألوان سَبَق (الذهبي والقلب الحي والأخضر الساطع)
 */
@Composable
fun SabaqMascotRobotView(
  modifier: Modifier = Modifier,
  size: Dp = 110.dp,
  subtitle: String? = "وكيلك الذكي"
) {
  val infiniteTransition = rememberInfiniteTransition(label = "RobotFloat")
  val floatOffset by infiniteTransition.animateFloat(
    initialValue = -5f,
    targetValue = 5f,
    animationSpec = infiniteRepeatable(
      animation = tween(2200, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "MascotBobbing"
  )
  val eyeGlow by infiniteTransition.animateFloat(
    initialValue = 0.75f,
    targetValue = 1.0f,
    animationSpec = infiniteRepeatable(
      animation = tween(1500, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "MascotEyeGlow"
  )

  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Box(
      modifier = Modifier
        .size(size)
        .offset(y = floatOffset.dp),
      contentAlignment = Alignment.Center
    ) {
      Canvas(modifier = Modifier.fillMaxSize()) {
        val w = this.size.width
        val h = this.size.height

        // 1. هالة النور الذهبية المحيطة بالروبوت
        drawCircle(
          brush = Brush.radialGradient(
            colors = listOf(
              SabaqGoldPrimary.copy(alpha = 0.25f),
              SabaqGreenBright.copy(alpha = 0.10f),
              Color.Transparent
            ),
            center = Offset(w / 2, h / 2),
            radius = w * 0.48f
          ),
          radius = w * 0.48f,
          center = Offset(w / 2, h / 2)
        )

        // 2. الهوائي العلوي (Golden Antenna)
        drawLine(
          color = SabaqGoldPrimary,
          start = Offset(w / 2, h * 0.22f),
          end = Offset(w / 2, h * 0.08f),
          strokeWidth = w * 0.035f,
          cap = StrokeCap.Round
        )
        // جوهرة الهوائي المضيئة
        drawCircle(
          color = SabaqGoldHighlight,
          radius = w * 0.055f,
          center = Offset(w / 2, h * 0.08f)
        )
        drawCircle(
          color = SabaqGoldHeart,
          radius = w * 0.03f,
          center = Offset(w / 2, h * 0.08f)
        )

        // 3. أذنان جانبيتان ذهبيتان (Golden Ear Pods)
        drawRoundRect(
          color = SabaqGoldButton,
          topLeft = Offset(w * 0.12f, h * 0.36f),
          size = Size(w * 0.09f, h * 0.20f),
          cornerRadius = CornerRadius(w * 0.04f, w * 0.04f)
        )
        drawRoundRect(
          color = SabaqGoldButton,
          topLeft = Offset(w * 0.79f, h * 0.36f),
          size = Size(w * 0.09f, h * 0.20f),
          cornerRadius = CornerRadius(w * 0.04f, w * 0.04f)
        )

        // 4. رأس الروبوت الخارجي (درع ذهبي ناعم)
        drawRoundRect(
          brush = Brush.linearGradient(
            colors = listOf(SabaqGoldBronze, SabaqGoldPrimary, SabaqGoldHighlight, SabaqGoldWarm),
            start = Offset(0f, 0f),
            end = Offset(w, h)
          ),
          topLeft = Offset(w * 0.18f, h * 0.22f),
          size = Size(w * 0.64f, h * 0.52f),
          cornerRadius = CornerRadius(w * 0.22f, w * 0.22f)
        )

        // 5. شاشة الوجه الداخلية (شاشة خضراء عميقة - Dark Visor)
        drawRoundRect(
          brush = Brush.verticalGradient(
            colors = listOf(SabaqGreenSection, SabaqGreenCard, Color(0xFF03160D))
          ),
          topLeft = Offset(w * 0.23f, h * 0.27f),
          size = Size(w * 0.54f, h * 0.42f),
          cornerRadius = CornerRadius(w * 0.16f, w * 0.16f)
        )

        // إطار الشاشة الدقيق
        drawRoundRect(
          color = SabaqGreenBright.copy(alpha = 0.5f),
          topLeft = Offset(w * 0.23f, h * 0.27f),
          size = Size(w * 0.54f, h * 0.42f),
          cornerRadius = CornerRadius(w * 0.16f, w * 0.16f),
          style = Stroke(width = w * 0.015f)
        )

        // 6. عينا الروبوت المضيئتان (Digital Glowing Eyes)
        val eyeColor = SabaqGoldHighlight.copy(alpha = eyeGlow)
        // العين اليسرى (قوس مبتسم رقمي)
        val leftEyePath = Path().apply {
          moveTo(w * 0.34f, h * 0.46f)
          quadraticBezierTo(w * 0.40f, h * 0.41f, w * 0.46f, h * 0.46f)
        }
        drawPath(
          path = leftEyePath,
          color = eyeColor,
          style = Stroke(width = w * 0.045f, cap = StrokeCap.Round)
        )

        // العين اليمنى (قوس مبتسم رقمي)
        val rightEyePath = Path().apply {
          moveTo(w * 0.54f, h * 0.46f)
          quadraticBezierTo(w * 0.60f, h * 0.41f, w * 0.66f, h * 0.46f)
        }
        drawPath(
          path = rightEyePath,
          color = eyeColor,
          style = Stroke(width = w * 0.045f, cap = StrokeCap.Round)
        )

        // 7. الابتسامة الرقمية الذكية (Smart Cyan/Green Smile)
        val smilePath = Path().apply {
          moveTo(w * 0.42f, h * 0.56f)
          quadraticBezierTo(w * 0.50f, h * 0.61f, w * 0.58f, h * 0.56f)
        }
        drawPath(
          path = smilePath,
          color = SabaqGreenBright,
          style = Stroke(width = w * 0.025f, cap = StrokeCap.Round)
        )

        // 8. لمسات دوائر الدارات في وجنتي الروبوت
        drawCircle(
          color = SabaqGreenBright.copy(alpha = 0.45f),
          radius = w * 0.025f,
          center = Offset(w * 0.30f, h * 0.54f)
        )
        drawCircle(
          color = SabaqGreenBright.copy(alpha = 0.45f),
          radius = w * 0.025f,
          center = Offset(w * 0.70f, h * 0.54f)
        )

        // 9. جسم الروبوت السفلي الصغير (Mini Chassis)
        drawRoundRect(
          brush = Brush.verticalGradient(
            colors = listOf(SabaqGoldButton, SabaqGoldDeep)
          ),
          topLeft = Offset(w * 0.32f, h * 0.76f),
          size = Size(w * 0.36f, h * 0.18f),
          cornerRadius = CornerRadius(w * 0.08f, w * 0.08f)
        )

        // مؤشر الطاقة في الصدر (Chest Power Arc)
        drawCircle(
          color = SabaqGreenBright,
          radius = w * 0.04f,
          center = Offset(w / 2, h * 0.85f)
        )
        drawCircle(
          color = SabaqGoldHighlight,
          radius = w * 0.02f,
          center = Offset(w / 2, h * 0.85f)
        )
      }
    }

    if (subtitle != null) {
      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text = subtitle,
        fontFamily = ArefRuqaaFontFamily,
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        color = SabaqGoldPrimary,
        textAlign = TextAlign.Center
      )
    }
  }
}

/**
 * نص ذهبي متدرج باستخدام خط Aref Ruqaa
 */
@Composable
fun SabaqGradientTitle(
  text: String,
  modifier: Modifier = Modifier,
  fontSize: androidx.compose.ui.unit.TextUnit = 24.sp,
  fontWeight: FontWeight = FontWeight.Bold,
  textAlign: TextAlign = TextAlign.Start
) {
  Text(
    text = text,
    fontFamily = ArefRuqaaFontFamily,
    fontSize = fontSize,
    fontWeight = fontWeight,
    textAlign = textAlign,
    style = TextStyle(
      brush = SabaqHeadingGradient
    ),
    modifier = modifier
  )
}

/**
 * زر ذهبي متدرج فخم وفق مواصفات سبق الرسمية
 * linear-gradient(135deg,#8A4B06 0%,#B4680D 14%,#E09314 30%,#F9AD10 44%, #FFF378 54%,#F7C443 64%,#E09314 78%,#8A4B06 100%)
 */
@Composable
fun SabaqGoldButtonView(
  text: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  icon: (@Composable () -> Unit)? = null,
  testTag: String = "sabaq_gold_btn"
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(12.dp))
      .background(
        if (enabled) SabaqButtonGradient
        else Brush.linearGradient(listOf(Color.Gray, Color.DarkGray))
      )
      .clickable(enabled = enabled) { onClick() }
      .testTag(testTag)
      .padding(horizontal = 20.dp, vertical = 12.dp),
    contentAlignment = Alignment.Center
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center
    ) {
      if (icon != null) {
        icon()
        Spacer(modifier = Modifier.width(8.dp))
      }
      Text(
        text = text,
        fontFamily = ArefRuqaaFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        color = Color(0xFF070B07) // خلفية التطبيق للتباين العالي على الزر الذهبي الفاتح
      )
    }
  }
}
