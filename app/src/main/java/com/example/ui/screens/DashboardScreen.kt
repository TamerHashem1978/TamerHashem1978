package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TransactionRecord
import com.example.data.model.UserSubscriptionState
import com.example.ui.components.SabaqMascotRobotView
import com.example.ui.components.StatMetricCard
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DashboardScreen(
  subscriptionState: UserSubscriptionState?,
  transactions: List<TransactionRecord>,
  onSelectSubscription: (tierId: String, costSar: Double, tierNameAr: String) -> Unit,
  onRequestPayout: () -> Unit,
  onTriggerSubscriptionAlert: () -> Unit = {},
  onTriggerSkillUpdateAlert: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  val currentTier = subscriptionState?.currentTierId ?: "FREE"

  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 100.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Top Overview Card
    item {
      DeveloperHeroCard(
        currentTier = currentTier,
        subscriptionState = subscriptionState,
        onRequestPayout = onRequestPayout
      )
    }

    // Smart Notifications Card
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("notification_center_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
          containerColor = SabaqGreenCard
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, SabaqGoldPrimary.copy(alpha = 0.35f))
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.NotificationsActive,
                contentDescription = null,
                tint = SabaqGoldPrimary,
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "نظام التنبيهات وإشعارات الوكلاء:",
                fontFamily = ArefRuqaaFontFamily,
                style = MaterialTheme.typography.titleSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = SabaqGoldHighlight,
                  fontSize = 16.sp
                )
              )
            }

            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(SabaqGreenBright.copy(alpha = 0.15f))
                .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Text(
                text = "نشط ومفعّل",
                style = MaterialTheme.typography.labelSmall.copy(
                  color = SabaqGreenBright,
                  fontWeight = FontWeight.Bold
                )
              )
            }
          }

          Spacer(modifier = Modifier.height(6.dp))

          Text(
            text = "تنبيهات فورية عند اقتراب انتهاء اشتراكات الوكلاء، أو عند إصدار نسخ وتحديثات جديدة للمهارات المشتراة مع إمكانية التحديث أو التجديد الفوري.",
            style = MaterialTheme.typography.bodySmall.copy(
              color = SabaqTextSecondary,
              lineHeight = 18.sp
            )
          )

          Spacer(modifier = Modifier.height(12.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(10.dp))
                .background(SabaqAmber.copy(alpha = 0.15f))
                .border(1.dp, SabaqAmber.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                .clickable { onTriggerSubscriptionAlert() }
                .padding(vertical = 10.dp, horizontal = 8.dp)
                .testTag("test_sub_alert_btn"),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "⏳ فحص انتهاء الاشتراك",
                fontFamily = ArefRuqaaFontFamily,
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = SabaqAmber,
                  fontSize = 12.sp
                )
              )
            }

            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(10.dp))
                .background(SabaqCyan.copy(alpha = 0.15f))
                .border(1.dp, SabaqCyan.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                .clickable { onTriggerSkillUpdateAlert() }
                .padding(vertical = 10.dp, horizontal = 8.dp)
                .testTag("test_skill_update_btn"),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "🚀 فحص تحديث المهارات",
                fontFamily = ArefRuqaaFontFamily,
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = SabaqCyan,
                  fontSize = 12.sp
                )
              )
            }
          }
        }
      }
    }

    // Analytics Metrics Grid
    item {
      Text(
        text = "إحصائيات الأداء واستخدام الوكلاء:",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
      )
      Spacer(modifier = Modifier.height(8.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        StatMetricCard(
          title = "إجمالي الأرباح",
          value = "4,820 ر.س",
          subtitle = "+18% نمو شهري",
          icon = Icons.Default.Paid,
          accentColor = SabaqEmerald,
          modifier = Modifier.weight(1f)
        )
        StatMetricCard(
          title = "استدعاءات الوكلاء",
          value = "142.5K",
          subtitle = "تنفيذ ذكي",
          icon = Icons.Default.Bolt,
          accentColor = SabaqCyan,
          modifier = Modifier.weight(1f)
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        StatMetricCard(
          title = "المشتركون الداعمون",
          value = "68 مطور ومستخدم",
          subtitle = "عوائد مستمرة",
          icon = Icons.Default.Group,
          accentColor = SabaqBlue,
          modifier = Modifier.weight(1f)
        )
        StatMetricCard(
          title = "معدل التقييم",
          value = "4.92 / 5",
          subtitle = "جودة الاستدلال",
          icon = Icons.Default.Star,
          accentColor = SabaqAmber,
          modifier = Modifier.weight(1f)
        )
      }
    }

    // Subscription System Header
    item {
      Column(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.AutoAwesome,
            contentDescription = null,
            tint = SabaqAmber,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "نظام الاشتراكات الشهري لدعم المطورين",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
          )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = "اشترك لدعم مطوري وكلاء الذكاء الاصطناعي والحصول على وصول كامل لجميع المهارات الحصرية على gosabaq.com.",
          style = MaterialTheme.typography.bodySmall.copy(
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        )
      }
    }

    // Subscription Tiers
    item {
      SubscriptionTierCard(
        tierId = "FREE",
        tierNameAr = "باقة المستكشف (Starter)",
        costSar = 0.0,
        description = "لتجربة الوكلاء واختبار المهارات العامة مجاناً.",
        features = listOf(
          "50 استدعاء مجاني لوكلاء الذكاء الاصطناعي شهرياً",
          "بناء واختبار مهارات خاصة في المختبر",
          "تصدير كود المهارة بصيغة JSON",
          "دعم مجتمعي عبر منصة سبق"
        ),
        isCurrent = currentTier == "FREE",
        onSelect = { onSelectSubscription("FREE", 0.0, "باقة المستكشف") }
      )
    }

    item {
      SubscriptionTierCard(
        tierId = "PRO",
        tierNameAr = "باقة المحترف (Pro Creator)",
        costSar = 49.0,
        description = "الباقة الأكثر شعبية لدعم المطورين واستخدام الوكلاء بدون حدود.",
        features = listOf(
          "استخدام غير محدود لجميع وكلاء ومهارات المتجر",
          "توجيه 80% من اشتراكك مباشرة لدعم المطورين المفضلين لديك",
          "شعار المطور الموثق (Verified Pro) في المجتمع",
          "500 اختبار وكيل يومياً مع تفاصيل التقييم والاستدلال",
          "تنزيل تقارير الفحص والتدقيق القانوني والمالي بصيغة PDF"
        ),
        isPopular = true,
        isCurrent = currentTier == "PRO",
        onSelect = { onSelectSubscription("PRO", 49.0, "باقة المحترف Pro") }
      )
    }

    item {
      SubscriptionTierCard(
        tierId = "ENTERPRISE",
        tierNameAr = "باقة المؤسسات ومطوري الوكلاء (Enterprise AI)",
        costSar = 149.0,
        description = "للشركات والفرق البرمجية الراغبة في أتمتة كاملة واستضافة الوكلاء.",
        features = listOf(
          "نشر واستضافة غير محدودة للوكلاء على خوادم فائقة السرعة",
          "توزيع أرباح أولوية بنسبة 85% على مبيعات مهاراتك الخاصة",
          "واجهات برمجة مباشرة (REST API & Webhooks) لربط الأنظمة",
          "دعم فني مخصص 24/7 مع مدير حساب تقني",
          "اتفاقية مستوى الخدمة (SLA 99.9%) وحماية البيانات"
        ),
        isCurrent = currentTier == "ENTERPRISE",
        onSelect = { onSelectSubscription("ENTERPRISE", 149.0, "باقة المؤسسات Enterprise") }
      )
    }

    // Revenue Sharing Pool Explanation
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f)
        )
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Verified,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "كيف تعمل آلية توزيع أرباح سبق لدعم المطورين؟",
              style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
              )
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = "تعتمد منصة سبق (gosabaq.com) نموذجاً عادلاً ومبتكراً: يتم جمع رسوم الاشتراكات الشهرية وتوزيع 80% إلى 85% منها شهرياً على المطورين بحسب معدل استهلاك مهاراتهم ورضا المستخدمين وتقييمات الاستدلال، مما يخلق بيئة مستدامة ومحفزة للابتكار العربي في الذكاء الاصطناعي.",
            style = MaterialTheme.typography.bodySmall.copy(
              lineHeight = 19.sp,
              color = MaterialTheme.colorScheme.onSurface
            )
          )
        }
      }
    }

    // Transaction History
    if (transactions.isNotEmpty()) {
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "سجل المدفوعات والعمليات:",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
          )
          Text(
            text = "${transactions.size} عملية موثقة",
            style = MaterialTheme.typography.labelSmall.copy(color = SabaqEmerald)
          )
        }
      }

      items(transactions, key = { it.id }) { tx ->
        TransactionItemCard(tx = tx)
      }
    }
  }
}

@Composable
private fun DeveloperHeroCard(
  currentTier: String,
  subscriptionState: UserSubscriptionState?,
  onRequestPayout: () -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(20.dp))
      .background(
        Brush.linearGradient(
          listOf(
            SabaqGreenSection,
            SabaqGreenCard
          )
        )
      )
      .border(
        width = 1.dp,
        brush = Brush.linearGradient(listOf(SabaqGoldPrimary.copy(alpha = 0.5f), SabaqGreenBright.copy(alpha = 0.3f))),
        shape = RoundedCornerShape(20.dp)
      )
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(18.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "لوحة تحكم سبق (Sabaq Hub)",
            fontFamily = ArefRuqaaFontFamily,
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 20.sp,
              color = SabaqGoldPrimary
            )
          )
          Text(
            text = "حساب مطور ومستخدم ذكاء اصطناعي",
            style = MaterialTheme.typography.labelSmall.copy(color = SabaqTextSecondary)
          )
        }

        Surface(
          color = if (currentTier != "FREE") SabaqGoldPrimary.copy(alpha = 0.18f) else SabaqGreenBright.copy(alpha = 0.15f),
          shape = RoundedCornerShape(10.dp)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = if (currentTier != "FREE") Icons.Default.Verified else Icons.Default.Bolt,
              contentDescription = null,
              tint = if (currentTier != "FREE") SabaqGoldPrimary else SabaqGreenBright,
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = when (currentTier) {
                "PRO" -> "باقة المحترف Pro"
                "ENTERPRISE" -> "باقة المؤسسات AI"
                else -> "المستكشف (مجاناً)"
              },
              fontFamily = ArefRuqaaFontFamily,
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = if (currentTier != "FREE") SabaqGoldPrimary else SabaqGreenBright
              )
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "الرصيد القابل للسحب:",
            style = MaterialTheme.typography.labelSmall.copy(color = SabaqTextSecondary)
          )
          Text(
            text = "3,250.00 ر.س",
            fontFamily = ArefRuqaaFontFamily,
            style = MaterialTheme.typography.titleLarge.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 24.sp,
              color = SabaqGreenBright
            )
          )
        }

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(SabaqButtonGradient)
            .clickable { onRequestPayout() }
            .padding(horizontal = 14.dp, vertical = 9.dp)
            .testTag("request_payout_btn"),
          contentAlignment = Alignment.Center
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.AccountBalanceWallet,
              contentDescription = null,
              tint = Color(0xFF070B07),
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "سحب إلى STC Pay / مدى",
              fontFamily = ArefRuqaaFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp,
              color = Color(0xFF070B07)
            )
          }
        }
      }
    }
  }
}

@Composable
private fun SubscriptionTierCard(
  tierId: String,
  tierNameAr: String,
  costSar: Double,
  description: String,
  features: List<String>,
  isCurrent: Boolean,
  isPopular: Boolean = false,
  onSelect: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("sub_card_$tierId"),
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(
      containerColor = SabaqGreenCard
    ),
    border = if (isPopular) {
      androidx.compose.foundation.BorderStroke(1.5.dp, SabaqGoldPrimary)
    } else if (isCurrent) {
      androidx.compose.foundation.BorderStroke(1.5.dp, SabaqGreenBright)
    } else {
      androidx.compose.foundation.BorderStroke(0.5.dp, Color(0xFF134529))
    }
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(18.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = tierNameAr,
            fontFamily = ArefRuqaaFontFamily,
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 18.sp,
              color = if (isPopular) SabaqGoldPrimary else SabaqTextPrimary
            )
          )
          Text(
            text = description,
            style = MaterialTheme.typography.bodySmall.copy(color = SabaqTextSecondary)
          )
        }

        if (isPopular) {
          Surface(
            color = SabaqGoldPrimary.copy(alpha = 0.15f),
            shape = RoundedCornerShape(8.dp)
          ) {
            Text(
              text = "الأكثر طلباً",
              fontFamily = ArefRuqaaFontFamily,
              style = MaterialTheme.typography.labelSmall.copy(
                color = SabaqGoldPrimary,
                fontWeight = FontWeight.Bold
              ),
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
          text = if (costSar == 0.0) "مجاناً" else "${String.format(Locale.US, "%.0f", costSar)}",
          fontFamily = ArefRuqaaFontFamily,
          style = MaterialTheme.typography.headlineMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp,
            color = SabaqGoldPrimary
          )
        )
        if (costSar > 0.0) {
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "ر.س / شهرياً",
            fontFamily = ArefRuqaaFontFamily,
            style = MaterialTheme.typography.labelMedium.copy(color = SabaqGoldWarm)
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))
      HorizontalDivider(color = Color(0xFF134529))
      Spacer(modifier = Modifier.height(10.dp))

      // Features list
      features.forEach { feature ->
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
          verticalAlignment = Alignment.Top
        ) {
          Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            tint = SabaqGreenBright,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = feature,
            style = MaterialTheme.typography.bodySmall.copy(
              color = SabaqTextPrimary,
              lineHeight = 17.sp
            )
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      if (isCurrent) {
        Surface(
          modifier = Modifier
            .fillMaxWidth()
            .height(44.dp),
          color = SabaqGreenBright.copy(alpha = 0.15f),
          shape = RoundedCornerShape(12.dp)
        ) {
          Box(contentAlignment = Alignment.Center) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = SabaqGreenBright, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "باقتك الحالية المفعلة",
                fontFamily = ArefRuqaaFontFamily,
                style = MaterialTheme.typography.bodyMedium.copy(color = SabaqGreenBright, fontWeight = FontWeight.Bold)
              )
            }
          }
        }
      } else {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(46.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
              if (isPopular) SabaqButtonGradient
              else Brush.linearGradient(listOf(SabaqGreenBright, SabaqGreenPrimary))
            )
            .clickable { onSelect() }
            .testTag("subscribe_btn_$tierId"),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = if (costSar == 0.0) "اختيار الباقة المجانية" else "اشتراك ودعم المطورين (${String.format(Locale.US, "%.0f", costSar)} ر.س)",
            fontFamily = ArefRuqaaFontFamily,
            style = MaterialTheme.typography.bodyMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
              color = if (isPopular) SabaqBackground else Color.White
            )
          )
        }
      }
    }
  }
}

@Composable
private fun TransactionItemCard(tx: TransactionRecord) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = SabaqGreenCard)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = tx.titleAr,
          fontFamily = ArefRuqaaFontFamily,
          style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = SabaqTextPrimary),
          maxLines = 1
        )
        Spacer(modifier = Modifier.height(2.dp))
        val dateStr = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date(tx.timestamp))
        Text(
          text = "$dateStr • ${tx.paymentMethod} • ${tx.receiptNumber}",
          style = MaterialTheme.typography.labelSmall.copy(
            color = SabaqTextSecondary,
            fontSize = 11.sp
          )
        )
      }

      Spacer(modifier = Modifier.width(8.dp))

      Text(
        text = "${String.format(Locale.US, "%.2f", tx.amountSar)} ر.س",
        fontFamily = ArefRuqaaFontFamily,
        style = MaterialTheme.typography.bodyMedium.copy(
          fontWeight = FontWeight.Bold,
          fontSize = 15.sp,
          color = SabaqGoldPrimary
        )
      )
    }
  }
}
