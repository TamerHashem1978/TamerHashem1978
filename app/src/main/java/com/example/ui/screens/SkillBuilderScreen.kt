package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Http
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.SabaqViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SkillBuilderScreen(
  viewModel: SabaqViewModel,
  title: String,
  description: String,
  category: String,
  keywords: String,
  instructions: String,
  enabledTools: Set<String>,
  inputSchema: String,
  outputFormat: String,
  priceType: String,
  priceSar: String,
  isGeneratingAi: Boolean = false,
  aiErrorMessage: String? = null,
  modifier: Modifier = Modifier
) {
  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 100.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Header Banner
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
              .size(44.dp)
              .clip(CircleShape)
              .background(SabaqGoldPrimary.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Build,
              contentDescription = null,
              tint = SabaqGoldPrimary,
              modifier = Modifier.size(24.dp)
            )
          }

          Spacer(modifier = Modifier.width(12.dp))

          Column {
            Text(
              text = "استوديو بناء مهارات وكلاء الذكاء الاصطناعي",
              fontFamily = ArefRuqaaFontFamily,
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = SabaqGoldPrimary
              )
            )
            Text(
              text = "حدد سلوك الوكيل، الأدوات المسموحة، أو استخدم Gemini AI لتوليد التعليمات تلقائياً.",
              style = MaterialTheme.typography.bodySmall.copy(
                color = SabaqTextSecondary
              )
            )
          }
        }
      }
    }

    // Skill Name & Category
    item {
      Text(
        text = "المعلومات الأساسية للمهارة:",
        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
      )
      Spacer(modifier = Modifier.height(6.dp))

      OutlinedTextField(
        value = title,
        onValueChange = { viewModel.builderTitle.value = it },
        label = { Text("اسم المهارة (مثال: مراجع العقود والأنظمة التجارية)") },
        placeholder = { Text("أدخل اسماً معبراً للمهارة") },
        modifier = Modifier
          .fillMaxWidth()
          .testTag("builder_skill_name_input"),
        shape = RoundedCornerShape(12.dp),
        singleLine = true
      )

      Spacer(modifier = Modifier.height(10.dp))

      OutlinedTextField(
        value = description,
        onValueChange = { viewModel.builderDescription.value = it },
        label = { Text("الوصف المختصر للمهارة") },
        modifier = Modifier
          .fillMaxWidth()
          .testTag("builder_description_input"),
        shape = RoundedCornerShape(12.dp),
        maxLines = 2
      )
    }

    // Trigger Keywords
    item {
      OutlinedTextField(
        value = keywords,
        onValueChange = { viewModel.builderKeywords.value = it },
        label = { Text("كلمات التحفيز الذاتي للوكيل (Trigger Keywords)") },
        placeholder = { Text("عقد، تدقيق، فاتورة، سؤال") },
        modifier = Modifier
          .fillMaxWidth()
          .testTag("builder_keywords_input"),
        shape = RoundedCornerShape(12.dp),
        singleLine = true
      )
    }

    // System Instructions / Prompt + Gemini Smart Generator
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "التعليمات البرمجية وسلوك الوكيل:",
          style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
        )

        Surface(
          modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable(enabled = !isGeneratingAi) { viewModel.generateSkillConfigWithGemini() }
            .border(1.dp, SabaqGoldPrimary.copy(alpha = 0.6f), RoundedCornerShape(10.dp))
            .testTag("gemini_generate_skill_btn"),
          color = SabaqGoldPrimary.copy(alpha = 0.15f),
          shape = RoundedCornerShape(10.dp)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            if (isGeneratingAi) {
              CircularProgressIndicator(
                modifier = Modifier.size(14.dp),
                color = SabaqGoldPrimary,
                strokeWidth = 2.dp
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "جاري التوليد عبر Gemini...",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = SabaqGoldPrimary
                )
              )
            } else {
              Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = "توليد عبر Gemini AI",
                tint = SabaqGoldPrimary,
                modifier = Modifier.size(15.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "توليد ذكي عبر Gemini AI",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = SabaqGoldPrimary
                )
              )
            }
          }
        }
      }

      if (!aiErrorMessage.isNullOrBlank()) {
        Spacer(modifier = Modifier.height(6.dp))
        Surface(
          modifier = Modifier.fillMaxWidth(),
          color = MaterialTheme.colorScheme.errorContainer,
          shape = RoundedCornerShape(10.dp)
        ) {
          Text(
            text = aiErrorMessage,
            style = MaterialTheme.typography.labelSmall.copy(
              color = MaterialTheme.colorScheme.onErrorContainer
            ),
            modifier = Modifier.padding(8.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(6.dp))

      OutlinedTextField(
        value = instructions,
        onValueChange = { viewModel.builderInstructions.value = it },
        placeholder = { Text("أنت وكيل ذكاء اصطناعي خبير في... قم بتحليل المدخلات واستدعاء الأدوات اللازمة...") },
        modifier = Modifier
          .fillMaxWidth()
          .height(130.dp)
          .testTag("builder_instructions_input"),
        shape = RoundedCornerShape(12.dp)
      )
    }

    // Tool Integrations Selection
    item {
      Text(
        text = "الأدوات البرمجية المسموح للوكيل باستدعائها (Tool Calling):",
        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
      )
      Spacer(modifier = Modifier.height(8.dp))

      val availableTools = listOf(
        Triple("WEB_SEARCH", "بحث الويب المباشر (Web Search)", Icons.Default.Language),
        Triple("CODE_RUNNER", "تشغيل الأكواد (Code Interpreter)", Icons.Default.Code),
        Triple("DATABASE_QUERY", "استعلام قواعد البيانات (SQL)", Icons.Default.Storage),
        Triple("DOCUMENT_PARSER", "تحليل المستندات وOCR", Icons.Default.Description),
        Triple("WHATSAPP_CONNECTOR", "ربط واجهات API والتطبيقات", Icons.Default.Http)
      )

      FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        availableTools.forEach { (toolId, label, icon) ->
          val isEnabled = enabledTools.contains(toolId)
          ToolToggleChip(
            toolId = toolId,
            label = label,
            icon = icon,
            isSelected = isEnabled,
            onToggle = { viewModel.toggleBuilderTool(toolId) }
          )
        }
      }
    }

    // Pricing & License Model
    item {
      Text(
        text = "نموذج المشاركة والترخيص (Pricing):",
        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
      )
      Spacer(modifier = Modifier.height(8.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        val types = listOf(
          Pair("FREE", "مجاني للجميع"),
          Pair("SUBSCRIPTION", "متاح لمشتركي Pro"),
          Pair("PAID", "مدفوع لمرة واحدة")
        )

        types.forEach { (typeId, label) ->
          val isSelected = priceType == typeId
          Surface(
            modifier = Modifier
              .weight(1f)
              .height(44.dp)
              .clip(RoundedCornerShape(10.dp))
              .clickable { viewModel.builderPriceType.value = typeId }
              .border(
                width = if (isSelected) 1.5.dp else 0.5.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                shape = RoundedCornerShape(10.dp)
              )
              .testTag("price_type_$typeId"),
            color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant
          ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(4.dp)) {
              Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                  color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                ),
                fontSize = 11.sp
              )
            }
          }
        }
      }

      if (priceType == "PAID") {
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
          value = priceSar,
          onValueChange = { viewModel.builderPriceSar.value = it },
          label = { Text("السعر بالريال السعودي (SAR)") },
          modifier = Modifier.fillMaxWidth().testTag("builder_price_input"),
          shape = RoundedCornerShape(12.dp),
          singleLine = true
        )
      }
    }

    // Submit CTA
    item {
      Spacer(modifier = Modifier.height(6.dp))
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(52.dp)
          .clip(RoundedCornerShape(14.dp))
          .background(SabaqButtonGradient)
          .clickable { viewModel.createSkillFromBuilder() }
          .testTag("create_skill_submit_btn"),
        contentAlignment = Alignment.Center
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = null,
            tint = SabaqBackground,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "حفظ واختبار المهارة في المختبر الفوري",
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
}

@Composable
private fun ToolToggleChip(
  toolId: String,
  label: String,
  icon: ImageVector,
  isSelected: Boolean,
  onToggle: () -> Unit
) {
  Surface(
    modifier = Modifier
      .clip(RoundedCornerShape(12.dp))
      .clickable { onToggle() }
      .border(
        width = if (isSelected) 1.5.dp else 1.dp,
        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
        shape = RoundedCornerShape(12.dp)
      )
      .testTag("tool_chip_$toolId"),
    color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant,
    shape = RoundedCornerShape(12.dp)
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.size(16.dp)
      )
      Spacer(modifier = Modifier.width(6.dp))
      Text(
        text = label,
        style = MaterialTheme.typography.labelSmall.copy(
          fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
          color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
      )
      if (isSelected) {
        Spacer(modifier = Modifier.width(4.dp))
        Icon(
          imageVector = Icons.Default.Check,
          contentDescription = null,
          tint = SabaqEmerald,
          modifier = Modifier.size(14.dp)
        )
      }
    }
  }
}
