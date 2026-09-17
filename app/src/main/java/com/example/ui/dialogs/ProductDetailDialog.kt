package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.AgentProduct
import com.example.ui.theme.*
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProductDetailDialog(
  product: AgentProduct?,
  onDismiss: () -> Unit,
  onBuyClicked: (AgentProduct) -> Unit,
  onTestClicked: (AgentProduct) -> Unit,
  modifier: Modifier = Modifier
) {
  if (product == null) return

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = modifier
        .fillMaxWidth(0.94f)
        .clip(RoundedCornerShape(24.dp))
        .border(
          width = 1.dp,
          brush = Brush.linearGradient(listOf(SabaqGoldPrimary.copy(alpha = 0.5f), SabaqGreenBright.copy(alpha = 0.3f))),
          shape = RoundedCornerShape(24.dp)
        ),
      color = SabaqGreenSection,
      tonalElevation = 8.dp
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp)
          .verticalScroll(rememberScrollState())
      ) {
        // Top Bar
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(SabaqGoldPrimary.copy(alpha = 0.15f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = SabaqGoldPrimary,
                modifier = Modifier.size(20.dp)
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = product.categoryLabelAr,
                fontFamily = ArefRuqaaFontFamily,
                style = MaterialTheme.typography.labelSmall.copy(
                  color = SabaqGoldPrimary,
                  fontWeight = FontWeight.Bold
                )
              )
              Text(
                text = product.version,
                style = MaterialTheme.typography.labelSmall.copy(
                  color = SabaqTextSecondary
                )
              )
            }
          }

          IconButton(
            onClick = onDismiss,
            modifier = Modifier.testTag("close_product_detail_btn")
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "إغلاق",
              tint = SabaqTextSecondary
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Titles
        Text(
          text = product.titleAr,
          fontFamily = ArefRuqaaFontFamily,
          style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            color = SabaqGoldPrimary
          )
        )
        Text(
          text = product.titleEn,
          style = MaterialTheme.typography.bodySmall.copy(
            color = SabaqTextSecondary
          )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Developer info & Rating row
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "المطور: ${product.developerName}",
            style = MaterialTheme.typography.bodySmall.copy(
              color = SabaqBlue,
              fontWeight = FontWeight.Medium
            )
          )

          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Star,
              contentDescription = null,
              tint = SabaqAmber,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "${product.developerRating} (${product.reviewsCount} تقييم)",
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold
              )
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Description
        Text(
          text = product.descriptionAr,
          style = MaterialTheme.typography.bodyMedium.copy(
            lineHeight = 22.sp,
            color = MaterialTheme.colorScheme.onSurface
          )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Capabilities Pills
        Text(
          text = "قدرات ومهارات الوكيل:",
          style = MaterialTheme.typography.labelMedium.copy(
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
        )
        Spacer(modifier = Modifier.height(6.dp))
        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          product.capabilities.split("،", ",").forEach { cap ->
            Surface(
              color = MaterialTheme.colorScheme.surfaceVariant,
              shape = RoundedCornerShape(8.dp)
            ) {
              Text(
                text = cap.trim(),
                style = MaterialTheme.typography.labelSmall.copy(
                  color = MaterialTheme.colorScheme.primary,
                  fontWeight = FontWeight.Medium
                ),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sample Scenario Card
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp)
          ) {
            Text(
              text = "مثال لسيناريو الاستخدام:",
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = SabaqBlue
              )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "📥 المدخل: \"${product.sampleInput}\"",
              style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "📤 المخرج: \"${product.sampleOutput}\"",
              style = MaterialTheme.typography.bodySmall.copy(
                color = SabaqEmerald
              )
            )
          }
        }

        Spacer(modifier = Modifier.height(20.dp))
        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
        Spacer(modifier = Modifier.height(16.dp))

        // Price & Actions
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "سعر الوكيل:",
              style = MaterialTheme.typography.labelSmall.copy(
                color = SabaqTextSecondary
              )
            )
            Text(
              text = "${String.format(Locale.US, "%.0f", product.priceSar)} ر.س",
              fontFamily = ArefRuqaaFontFamily,
              style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = SabaqGoldPrimary
              )
            )
            if (product.isSubscriptionIncluded) {
              Text(
                text = "متاح أيضاً ضمن اشتراك Pro الشهري",
                fontFamily = ArefRuqaaFontFamily,
                style = MaterialTheme.typography.labelSmall.copy(
                  color = SabaqGreenBright,
                  fontSize = 11.sp
                )
              )
            }
          }

          Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, SabaqGreenBright, RoundedCornerShape(12.dp))
                .clickable { onTestClicked(product) }
                .padding(horizontal = 14.dp, vertical = 9.dp)
                .testTag("test_agent_modal_btn"),
              contentAlignment = Alignment.Center
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.PlayArrow,
                  contentDescription = null,
                  tint = SabaqGreenBright,
                  modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = "تجربة",
                  fontFamily = ArefRuqaaFontFamily,
                  fontWeight = FontWeight.Bold,
                  color = SabaqGreenBright
                )
              }
            }

            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(SabaqButtonGradient)
                .clickable { onBuyClicked(product) }
                .padding(horizontal = 14.dp, vertical = 9.dp)
                .testTag("buy_agent_modal_btn"),
              contentAlignment = Alignment.Center
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.ShoppingBag,
                  contentDescription = null,
                  tint = SabaqBackground,
                  modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = if (product.isPurchased) "تم الشراء" else "شراء آمن",
                  fontFamily = ArefRuqaaFontFamily,
                  fontWeight = FontWeight.Bold,
                  color = SabaqBackground
                )
              }
            }
          }
        }
      }
    }
  }
}
