package com.example.ui.components

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
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ArefRuqaaFontFamily
import com.example.ui.theme.SabaqAmber
import com.example.ui.theme.SabaqGoldBronze
import com.example.ui.theme.SabaqGoldHighlight
import com.example.ui.theme.SabaqGoldPrimary
import com.example.ui.theme.SabaqGoldWarm
import com.example.ui.theme.SabaqGreenBright
import com.example.ui.theme.SabaqGreenCard
import com.example.ui.theme.SabaqGreenPrimary
import com.example.ui.theme.SabaqGreenSection
import com.example.ui.theme.SabaqHeadingGradient
import com.example.ui.theme.SabaqTextPrimary
import com.example.ui.theme.SabaqTextSecondary
import com.example.ui.viewmodel.SabaqNavTab

@Composable
fun SabaqTopHeader(
  currentTier: String,
  selectedCountry: com.example.data.model.ArabCountry? = null,
  onOpenCountryPicker: () -> Unit = {},
  hasActiveAlert: Boolean = false,
  onNotificationClick: () -> Unit = {},
  onNavigateToDashboard: () -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier.fillMaxWidth(),
    color = SabaqGreenSection,
    tonalElevation = 4.dp
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Brand & Official Sabaq Logo
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.clickable { onNavigateToDashboard() }
        ) {
          // الشعار الرسمي المعتمد لصقر سبق الذهبي
          SabaqOfficialLogoView(
            size = 40.dp,
            showGlow = true,
            is3dFloating = true
          )

          Spacer(modifier = Modifier.width(8.dp))

          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "سَبَق",
                fontFamily = ArefRuqaaFontFamily,
                style = TextStyle(
                  brush = SabaqHeadingGradient,
                  fontWeight = FontWeight.Bold,
                  fontSize = 22.sp
                )
              )
              Spacer(modifier = Modifier.width(5.dp))
              Text(
                text = "SABAQ AI",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  letterSpacing = 1.6.sp,
                  color = SabaqGoldHighlight,
                  fontSize = 10.sp
                )
              )
            }

            // Domain Badge
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(6.dp)
                  .clip(CircleShape)
                  .background(SabaqGreenBright)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "gosabaq.com",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontSize = 10.sp,
                  color = SabaqGreenBright,
                  fontWeight = FontWeight.SemiBold
                )
              )
            }
          }
        }

        // Country & Currency Selector + Subscription Tier + Notification Bell
        Row(verticalAlignment = Alignment.CenterVertically) {
          // Country / Currency Chip
          if (selectedCountry != null) {
            Surface(
              modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .clickable { onOpenCountryPicker() }
                .border(
                  1.dp,
                  SabaqGoldPrimary.copy(alpha = 0.5f),
                  RoundedCornerShape(16.dp)
                )
                .testTag("top_country_currency_btn"),
              color = SabaqGreenCard,
              shape = RoundedCornerShape(16.dp)
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = selectedCountry.flagEmoji,
                  fontSize = 15.sp
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = selectedCountry.currencySymbolAr,
                  fontFamily = ArefRuqaaFontFamily,
                  style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = SabaqGoldPrimary
                  )
                )
              }
            }
            Spacer(modifier = Modifier.width(6.dp))
          }

          // Notification Bell
          Box(
            modifier = Modifier
              .size(34.dp)
              .clip(CircleShape)
              .background(SabaqGreenCard)
              .border(
                1.dp,
                if (hasActiveAlert) SabaqAmber else SabaqGoldPrimary.copy(alpha = 0.35f),
                CircleShape
              )
              .clickable { onNotificationClick() }
              .testTag("top_notifications_btn"),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Notifications,
              contentDescription = "التنبيهات والإشعارات",
              tint = if (hasActiveAlert) SabaqAmber else SabaqGoldPrimary,
              modifier = Modifier.size(17.dp)
            )
            if (hasActiveAlert) {
              Box(
                modifier = Modifier
                  .size(8.dp)
                  .align(Alignment.TopEnd)
                  .clip(CircleShape)
                  .background(SabaqAmber)
              )
            }
          }

          Spacer(modifier = Modifier.width(6.dp))

          Surface(
            modifier = Modifier
              .clip(RoundedCornerShape(16.dp))
              .clickable { onNavigateToDashboard() }
              .border(
                1.dp,
                if (currentTier == "PRO" || currentTier == "ENTERPRISE") SabaqGoldPrimary.copy(alpha = 0.5f)
                else SabaqGreenBright.copy(alpha = 0.3f),
                RoundedCornerShape(16.dp)
              )
              .testTag("top_tier_chip"),
            color = if (currentTier == "PRO" || currentTier == "ENTERPRISE") {
              SabaqGoldPrimary.copy(alpha = 0.16f)
            } else {
              SabaqGreenCard
            },
            shape = RoundedCornerShape(16.dp)
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = if (currentTier == "ENTERPRISE") Icons.Default.Verified else Icons.Default.Shield,
                contentDescription = "Subscription Tier",
                tint = if (currentTier == "PRO" || currentTier == "ENTERPRISE") SabaqGoldPrimary else SabaqGreenBright,
                modifier = Modifier.size(14.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = when (currentTier) {
                  "PRO" -> "PRO"
                  "ENTERPRISE" -> "مؤسسات"
                  else -> "المستكشف"
                },
                fontFamily = ArefRuqaaFontFamily,
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 11.sp,
                  color = if (currentTier == "PRO" || currentTier == "ENTERPRISE") SabaqGoldPrimary else SabaqTextPrimary
                )
              )
            }
          }
        }
      }
    }
  }
}

@Composable
fun SabaqBottomNavBar(
  currentTab: SabaqNavTab,
  onTabSelected: (SabaqNavTab) -> Unit,
  modifier: Modifier = Modifier
) {
  NavigationBar(
    modifier = modifier,
    containerColor = SabaqGreenSection,
    tonalElevation = 8.dp
  ) {
    val items = listOf(
      Triple(SabaqNavTab.MARKETPLACE, Icons.Default.ShoppingBag, "market_tab"),
      Triple(SabaqNavTab.BUILDER, Icons.Default.Code, "builder_tab"),
      Triple(SabaqNavTab.TESTER, Icons.Default.Psychology, "tester_tab"),
      Triple(SabaqNavTab.SHARING, Icons.Default.Share, "share_tab"),
      Triple(SabaqNavTab.DASHBOARD, Icons.Default.Dashboard, "dashboard_tab")
    )

    items.forEach { (tab, icon, testTag) ->
      val isSelected = currentTab == tab
      NavigationBarItem(
        selected = isSelected,
        onClick = { onTabSelected(tab) },
        icon = {
          Icon(
            imageVector = icon,
            contentDescription = tab.titleAr,
            modifier = Modifier.size(24.dp)
          )
        },
        label = {
          Text(
            text = tab.titleAr,
            fontFamily = ArefRuqaaFontFamily,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
          )
        },
        colors = NavigationBarItemDefaults.colors(
          selectedIconColor = SabaqGoldPrimary,
          selectedTextColor = SabaqGoldPrimary,
          indicatorColor = SabaqGoldPrimary.copy(alpha = 0.18f),
          unselectedIconColor = SabaqTextSecondary,
          unselectedTextColor = SabaqTextSecondary
        ),
        modifier = Modifier.testTag(testTag)
      )
    }
  }
}


@Composable
fun StatMetricCard(
  title: String,
  value: String,
  subtitle: String,
  icon: ImageVector,
  accentColor: Color,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier,
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surfaceVariant
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = title,
          style = MaterialTheme.typography.bodySmall.copy(
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        )
        Box(
          modifier = Modifier
            .size(32.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(accentColor.copy(alpha = 0.15f)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = icon,
            contentDescription = title,
            tint = accentColor,
            modifier = Modifier.size(18.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = value,
        style = MaterialTheme.typography.titleLarge.copy(
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
      )

      Spacer(modifier = Modifier.height(2.dp))

      Text(
        text = subtitle,
        style = MaterialTheme.typography.labelSmall.copy(
          color = accentColor,
          fontWeight = FontWeight.Medium
        )
      )
    }
  }
}
