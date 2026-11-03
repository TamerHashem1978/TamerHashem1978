package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Upgrade
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SabaqAlertType
import com.example.data.model.SabaqToastAlert
import com.example.ui.theme.ArefRuqaaFontFamily
import com.example.ui.theme.SabaqAmber
import com.example.ui.theme.SabaqButtonGradient
import com.example.ui.theme.SabaqCyan
import com.example.ui.theme.SabaqGoldHighlight
import com.example.ui.theme.SabaqGoldPrimary
import com.example.ui.theme.SabaqGreenBright
import com.example.ui.theme.SabaqGreenCard
import com.example.ui.theme.SabaqGreenSection
import com.example.ui.theme.SabaqTextPrimary
import com.example.ui.theme.SabaqTextSecondary
import kotlinx.coroutines.delay

@Composable
fun SabaqToastNotificationHost(
  alert: SabaqToastAlert?,
  onDismiss: () -> Unit,
  onActionClick: (SabaqToastAlert) -> Unit,
  modifier: Modifier = Modifier,
  autoDismissMs: Long = 7000L
) {
  // Auto-dismiss timer
  LaunchedEffect(alert?.id) {
    if (alert != null && autoDismissMs > 0) {
      delay(autoDismissMs)
      onDismiss()
    }
  }

  AnimatedVisibility(
    visible = alert != null,
    enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
    exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 8.dp)
  ) {
    if (alert != null) {
      SabaqToastBannerContent(
        alert = alert,
        onDismiss = onDismiss,
        onActionClick = { onActionClick(alert) }
      )
    }
  }
}

@Composable
private fun SabaqToastBannerContent(
  alert: SabaqToastAlert,
  onDismiss: () -> Unit,
  onActionClick: () -> Unit
) {
  val (borderColor, iconVector, badgeBg) = when (alert.type) {
    SabaqAlertType.SUBSCRIPTION_EXPIRING -> Triple(
      SabaqAmber,
      Icons.Default.Schedule,
      SabaqAmber.copy(alpha = 0.2f)
    )
    SabaqAlertType.SKILL_VERSION_UPDATE -> Triple(
      SabaqCyan,
      Icons.Default.Upgrade,
      SabaqCyan.copy(alpha = 0.2f)
    )
    SabaqAlertType.PAYMENT_COMPLETED -> Triple(
      SabaqGreenBright,
      Icons.Default.AutoAwesome,
      SabaqGreenBright.copy(alpha = 0.2f)
    )
    SabaqAlertType.GENERAL_INFO -> Triple(
      SabaqGoldPrimary,
      Icons.Default.NotificationsActive,
      SabaqGoldPrimary.copy(alpha = 0.2f)
    )
  }

  Surface(
    modifier = Modifier
      .fillMaxWidth()
      .shadow(16.dp, RoundedCornerShape(18.dp))
      .border(1.5.dp, borderColor, RoundedCornerShape(18.dp))
      .clip(RoundedCornerShape(18.dp))
      .testTag("toast_notification_banner"),
    color = SabaqGreenCard
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .background(
          Brush.verticalGradient(
            listOf(
              SabaqGreenSection,
              SabaqGreenCard
            )
          )
        )
        .padding(14.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        // Icon Badge
        Box(
          modifier = Modifier
            .size(42.dp)
            .clip(CircleShape)
            .background(badgeBg)
            .border(1.dp, borderColor.copy(alpha = 0.5f), CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = iconVector,
            contentDescription = null,
            tint = borderColor,
            modifier = Modifier.size(22.dp)
          )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Title and Body Message
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = alert.title,
            fontFamily = ArefRuqaaFontFamily,
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 17.sp,
              color = SabaqGoldHighlight
            )
          )

          Spacer(modifier = Modifier.height(2.dp))

          Text(
            text = alert.message,
            style = MaterialTheme.typography.bodySmall.copy(
              color = SabaqTextPrimary,
              lineHeight = 18.sp
            )
          )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Close Button
        IconButton(
          onClick = onDismiss,
          modifier = Modifier
            .size(32.dp)
            .testTag("toast_dismiss_btn")
        ) {
          Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "إغلاق التنبيه",
            tint = SabaqTextSecondary,
            modifier = Modifier.size(18.dp)
          )
        }
      }

      // Action Button (if label provided)
      if (!alert.actionLabel.isNullOrBlank()) {
        Spacer(modifier = Modifier.height(10.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.End,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(10.dp))
              .background(SabaqButtonGradient)
              .clickable { onActionClick() }
              .padding(horizontal = 16.dp, vertical = 8.dp)
              .testTag("toast_action_btn"),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = alert.actionLabel,
              fontFamily = ArefRuqaaFontFamily,
              style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = Color(0xFF070B07)
              )
            )
          }
        }
      }
    }
  }
}
