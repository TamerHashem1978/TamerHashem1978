package com.example.ui.dialogs

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.AgentSkill
import com.example.ui.theme.SabaqEmerald

@Composable
fun ShareDialog(
  skill: AgentSkill?,
  onDismiss: () -> Unit,
  onPublishToMarketplace: (AgentSkill) -> Unit,
  modifier: Modifier = Modifier
) {
  if (skill == null) return

  val context = LocalContext.current
  var isCopied by remember { mutableStateOf(false) }
  val shareUrl = "https://gosabaq.com/skills/${skill.shareSlug}"

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = modifier
        .fillMaxWidth(0.94f)
        .clip(RoundedCornerShape(24.dp)),
      color = MaterialTheme.colorScheme.surface,
      tonalElevation = 8.dp
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp)
      ) {
        // Top row
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
                .background(SabaqEmerald.copy(alpha = 0.15f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Share,
                contentDescription = null,
                tint = SabaqEmerald,
                modifier = Modifier.size(20.dp)
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(
              text = "مشاركة ونشر المهارة",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
          }

          IconButton(
            onClick = onDismiss,
            modifier = Modifier.testTag("close_share_dialog_btn")
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "إغلاق",
              tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
          text = skill.title,
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Share URL Box
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(14.dp)
          ) {
            Text(
              text = "الرابط العام للمهارة على gosabaq.com:",
              style = MaterialTheme.typography.labelSmall.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .background(
                  color = MaterialTheme.colorScheme.surface,
                  shape = RoundedCornerShape(10.dp)
                )
                .border(
                  width = 1.dp,
                  color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                  shape = RoundedCornerShape(10.dp)
                )
                .padding(horizontal = 10.dp, vertical = 8.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(
                text = shareUrl,
                style = MaterialTheme.typography.bodySmall.copy(
                  fontFamily = FontFamily.Monospace,
                  color = MaterialTheme.colorScheme.primary,
                  fontSize = 12.sp
                ),
                maxLines = 1
              )

              IconButton(
                onClick = {
                  val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                  val clip = ClipData.newPlainText("Sabaq Skill Link", shareUrl)
                  clipboard.setPrimaryClip(clip)
                  isCopied = true
                },
                modifier = Modifier.size(28.dp).testTag("copy_share_url_btn")
              ) {
                Icon(
                  imageVector = Icons.Default.ContentCopy,
                  contentDescription = "نسخ",
                  tint = if (isCopied) SabaqEmerald else MaterialTheme.colorScheme.primary,
                  modifier = Modifier.size(16.dp)
                )
              }
            }

            if (isCopied) {
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "✓ تم نسخ الرابط إلى الحافظة بنجاح!",
                style = MaterialTheme.typography.labelSmall.copy(
                  color = SabaqEmerald,
                  fontWeight = FontWeight.Bold
                )
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Community status
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Public,
              contentDescription = null,
              tint = if (skill.isPublished) SabaqEmerald else MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = if (skill.isPublished) "المهارة منشورة في سوق سبق العام" else "المهارة مسودة خاصة بك",
              style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Medium
              )
            )
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Action buttons
        Column(
          modifier = Modifier.fillMaxWidth(),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          if (!skill.isPublished) {
            Button(
              onClick = { onPublishToMarketplace(skill) },
              modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("publish_market_btn"),
              shape = RoundedCornerShape(12.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Public,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text("نشر المهارة في سوق سبق (Marketplace)")
            }
          }

          OutlinedButton(
            onClick = {
              val jsonExport = """
                {
                  "sabaq_version": "2.0",
                  "platform": "gosabaq.com",
                  "skill_id": "${skill.id}",
                  "title": "${skill.title}",
                  "category": "${skill.category}",
                  "tools": "${skill.toolsEnabled}",
                  "system_instructions": "${skill.systemInstructions.replace("\"", "\\\"")}"
                }
              """.trimIndent()
              val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
              val clip = ClipData.newPlainText("Sabaq Skill JSON", jsonExport)
              clipboard.setPrimaryClip(clip)
              isCopied = true
            },
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp)
              .testTag("export_agent_json_btn"),
            shape = RoundedCornerShape(12.dp)
          ) {
            Icon(
              imageVector = Icons.Default.FileDownload,
              contentDescription = null,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("تصدير حزمة الوكيل (JSON/Python Agent Schema)")
          }
        }
      }
    }
  }
}
