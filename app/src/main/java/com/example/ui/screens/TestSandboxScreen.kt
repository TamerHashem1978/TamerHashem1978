package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CopyAll
import androidx.compose.material.icons.filled.DataObject
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AgentSkill
import com.example.data.model.TestRunResult
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TestSandboxScreen(
  skills: List<AgentSkill>,
  selectedSkill: AgentSkill?,
  inputPrompt: String,
  isTestingRunning: Boolean,
  lastResult: TestRunResult?,
  testHistory: List<TestRunResult>,
  onSkillSelected: (AgentSkill) -> Unit,
  onPromptChanged: (String) -> Unit,
  onRunTest: () -> Unit,
  modifier: Modifier = Modifier
) {
  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 100.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Top banner
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = SabaqGreenCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, SabaqGoldPrimary.copy(alpha = 0.35f))
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(42.dp)
              .clip(CircleShape)
              .background(SabaqGoldPrimary.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Psychology,
              contentDescription = null,
              tint = SabaqGoldPrimary,
              modifier = Modifier.size(24.dp)
            )
          }

          Spacer(modifier = Modifier.width(12.dp))

          Column {
            Text(
              text = "مختبر تقييم واختبار مهارات الوكلاء",
              fontFamily = ArefRuqaaFontFamily,
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = SabaqGoldPrimary
              )
            )
            Text(
              text = "قم بمحاكاة تشغيل الوكيل وتتبع مسار الاستدلال (Reasoning) واستهلاك الرموز.",
              style = MaterialTheme.typography.bodySmall.copy(
                color = SabaqTextSecondary
              )
            )
          }
        }
      }
    }

    // Skills Selector Chips
    item {
      Text(
        text = "اختر المهارة المراد اختبارها:",
        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
      )
      Spacer(modifier = Modifier.height(6.dp))

      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        skills.forEach { skill ->
          val isSelected = selectedSkill?.id == skill.id
          Surface(
            modifier = Modifier
              .clip(RoundedCornerShape(12.dp))
              .clickable { onSkillSelected(skill) }
              .border(
                width = if (isSelected) 1.5.dp else 0.5.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                shape = RoundedCornerShape(12.dp)
              )
              .testTag("select_skill_${skill.id}"),
            color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant,
            shape = RoundedCornerShape(12.dp)
          ) {
            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
              Text(
                text = skill.title,
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                ),
                maxLines = 1
              )
              Text(
                text = "تقييم: ${skill.benchmarkScore}% • ${skill.testCount} اختبار",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontSize = 10.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              )
            }
          }
        }
      }
    }

    // Selected Skill Meta Card
    if (selectedSkill != null) {
      item {
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
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(
                text = "الأدوات المفعلة للوكيل:",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
              )
              Text(
                text = selectedSkill.category,
                style = MaterialTheme.typography.labelSmall.copy(
                  color = MaterialTheme.colorScheme.primary,
                  fontWeight = FontWeight.Bold
                )
              )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = selectedSkill.toolsEnabled.replace(",", " • "),
              style = MaterialTheme.typography.bodySmall.copy(
                color = SabaqBlue,
                fontSize = 11.sp
              )
            )
          }
        }
      }
    }

    // Input Prompt Field
    item {
      Text(
        text = "سيناريو ومدخلات الاختبار (Prompt / Test Payload):",
        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
      )
      Spacer(modifier = Modifier.height(6.dp))

      OutlinedTextField(
        value = inputPrompt,
        onValueChange = onPromptChanged,
        placeholder = { Text("أدخل نص السؤال أو المعطيات التي سيعالجها الوكيل...") },
        modifier = Modifier
          .fillMaxWidth()
          .height(110.dp)
          .testTag("test_prompt_input"),
        shape = RoundedCornerShape(14.dp)
      )

      Spacer(modifier = Modifier.height(6.dp))

      // Quick example chips
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        val quickPrompts = listOf(
          "فحص عقد تجاري مع شرط جزائي",
          "استفسار شحنة متجر سلة مع رقم الطلب #8912",
          "مراجعة فاتورة ضريبة القيمة المضافة 15% ZATCA"
        )
        quickPrompts.forEach { q ->
          Surface(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .clickable { onPromptChanged(q) },
            color = MaterialTheme.colorScheme.surfaceVariant
          ) {
            Text(
              text = "💡 $q",
              style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              ),
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }
        }
      }
    }

    // Run Test Button
    item {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(52.dp)
          .clip(RoundedCornerShape(14.dp))
          .background(if (isTestingRunning) Brush.linearGradient(listOf(SabaqGoldWarm, SabaqGoldBronze)) else SabaqButtonGradient)
          .clickable(enabled = !isTestingRunning) { onRunTest() }
          .testTag("run_test_btn"),
        contentAlignment = Alignment.Center
      ) {
        if (isTestingRunning) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            CircularProgressIndicator(
              modifier = Modifier.size(22.dp),
              color = SabaqBackground,
              strokeWidth = 2.5.dp
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
              text = "جاري استدعاء أدوات الوكيل وتنفيذ الاستدلال...",
              fontFamily = ArefRuqaaFontFamily,
              style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                color = SabaqBackground
              )
            )
          }
        } else {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.PlayArrow,
              contentDescription = null,
              tint = SabaqBackground,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "تشغيل مهارة الوكيل (Run Test)",
              fontFamily = ArefRuqaaFontFamily,
              style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = SabaqBackground
              )
            )
          }
        }
      }
    }

    // Live Execution Results & Telemetry
    if (lastResult != null) {
      item {
        ExecutionResultCard(result = lastResult)
      }
    }

    // Historical Test Runs
    if (testHistory.isNotEmpty()) {
      item {
        Spacer(modifier = Modifier.height(10.dp))
        Text(
          text = "سجل الاختبارات السابقة:",
          style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
        )
      }

      items(testHistory.take(4), key = { it.id }) { historyItem ->
        TestHistoryItemCard(item = historyItem)
      }
    }
  }
}

@Composable
private fun ExecutionResultCard(result: TestRunResult) {
  Card(
    modifier = Modifier.fillMaxWidth().testTag("execution_result_card"),
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    border = androidx.compose.foundation.BorderStroke(1.dp, SabaqEmerald.copy(alpha = 0.5f))
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      // Header: Score & Telemetry
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = SabaqEmerald,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "اكتمل الاختبار بنجاح (Score: ${result.scorePercentage}%)",
            style = MaterialTheme.typography.titleSmall.copy(
              fontWeight = FontWeight.Bold,
              color = SabaqEmerald
            )
          )
        }

        Surface(
          color = SabaqEmerald.copy(alpha = 0.15f),
          shape = RoundedCornerShape(8.dp)
        ) {
          Text(
            text = "معتمد من منصة سبق",
            style = MaterialTheme.typography.labelSmall.copy(
              color = SabaqEmerald,
              fontWeight = FontWeight.Bold,
              fontSize = 10.sp
            ),
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Telemetry metrics
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        TelemetryBadge(
          icon = Icons.Default.Timer,
          label = "زمن الاستجابة",
          value = "${result.latencyMs} ms",
          color = SabaqBlue
        )
        TelemetryBadge(
          icon = Icons.Default.Speed,
          label = "الرموز المستهلكة",
          value = "${result.tokensUsed} Tokens",
          color = SabaqAmber
        )
        TelemetryBadge(
          icon = Icons.Default.AutoAwesome,
          label = "الدقة والامتثال",
          value = "${result.scorePercentage}%",
          color = SabaqEmerald
        )
      }

      Spacer(modifier = Modifier.height(14.dp))
      HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
      Spacer(modifier = Modifier.height(10.dp))

      // Reasoning Steps Trace
      Text(
        text = "مسار الاستدلال البرمجي للوكيل (Reasoning Trace):",
        style = MaterialTheme.typography.labelSmall.copy(
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.primary
        )
      )
      Spacer(modifier = Modifier.height(4.dp))

      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(10.dp))
          .background(MaterialTheme.colorScheme.surface)
          .padding(10.dp)
      ) {
        Text(
          text = result.reasoningTrace,
          style = MaterialTheme.typography.bodySmall.copy(
            fontFamily = FontFamily.Monospace,
            fontSize = 11.sp,
            lineHeight = 16.sp,
            color = MaterialTheme.colorScheme.onSurface
          )
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Formatted Output Response
      Text(
        text = "المخرجات النهائية للوكيل:",
        style = MaterialTheme.typography.labelSmall.copy(
          fontWeight = FontWeight.Bold,
          color = SabaqEmerald
        )
      )
      Spacer(modifier = Modifier.height(4.dp))

      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(10.dp))
          .background(MaterialTheme.colorScheme.surface)
          .padding(12.dp)
      ) {
        Text(
          text = result.outputResponse,
          style = MaterialTheme.typography.bodyMedium.copy(
            lineHeight = 20.sp,
            color = MaterialTheme.colorScheme.onSurface
          )
        )
      }
    }
  }
}

@Composable
private fun TelemetryBadge(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  label: String,
  value: String,
  color: Color
) {
  Surface(
    modifier = Modifier.clip(RoundedCornerShape(10.dp)),
    color = color.copy(alpha = 0.12f),
    shape = RoundedCornerShape(10.dp)
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = color,
        modifier = Modifier.size(14.dp)
      )
      Spacer(modifier = Modifier.width(4.dp))
      Column {
        Text(
          text = label,
          style = MaterialTheme.typography.labelSmall.copy(
            fontSize = 9.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        )
        Text(
          text = value,
          style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.Bold,
            color = color
          )
        )
      }
    }
  }
}

@Composable
private fun TestHistoryItemCard(item: TestRunResult) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = item.skillTitle,
          style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
          maxLines = 1
        )
        Text(
          text = item.inputPrompt,
          style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
          maxLines = 1
        )
      }

      Spacer(modifier = Modifier.width(8.dp))

      Column(horizontalAlignment = Alignment.End) {
        Text(
          text = "${item.scorePercentage}%",
          style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.Bold,
            color = SabaqEmerald
          )
        )
        Text(
          text = "${item.latencyMs}ms",
          style = MaterialTheme.typography.labelSmall.copy(
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 10.sp
          )
        )
      }
    }
  }
}
