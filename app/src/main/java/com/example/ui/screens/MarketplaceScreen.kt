package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AgentProduct
import com.example.ui.components.SabaqMascotRobotView
import com.example.ui.theme.*
import java.util.Locale

@Composable
fun MarketplaceScreen(
  products: List<AgentProduct>,
  searchQuery: String,
  selectedCategory: String,
  onSearchQueryChanged: (String) -> Unit,
  onCategorySelected: (String) -> Unit,
  onProductClicked: (AgentProduct) -> Unit,
  onTestClicked: (AgentProduct) -> Unit,
  onBuyClicked: (AgentProduct) -> Unit,
  selectedCountry: com.example.data.model.ArabCountry = com.example.data.model.ArabCountriesData.EGYPT,
  onOpenCountryPicker: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding = PaddingValues(bottom = 90.dp)
  ) {
    // Hero Banner with Official Logo & 3D Perspective
    item {
      MarketplaceHeroBanner(
        selectedCountry = selectedCountry,
        onOpenCountryPicker = onOpenCountryPicker
      )
    }

    // Search and Filter Bar
    item {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 8.dp)
      ) {
        OutlinedTextField(
          value = searchQuery,
          onValueChange = onSearchQueryChanged,
          placeholder = { Text("ابحث في وكلاء الذكاء الاصطناعي، المهارات، أو التصنيف...") },
          leadingIcon = {
            Icon(
              imageVector = Icons.Default.Search,
              contentDescription = "بحث",
              tint = MaterialTheme.colorScheme.primary
            )
          },
          trailingIcon = {
            if (searchQuery.isNotEmpty()) {
              IconButton(onClick = { onSearchQueryChanged("") }) {
                Icon(
                  imageVector = Icons.Default.Clear,
                  contentDescription = "مسح",
                  tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("search_agents_input"),
          shape = RoundedCornerShape(16.dp),
          singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Categories Chips
        CategoriesRow(
          selectedCategory = selectedCategory,
          onCategorySelected = onCategorySelected
        )
      }
    }

    // Product Cards List
    if (products.isEmpty()) {
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(40.dp),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "لا توجد نتائج تطابق بحثك الحالي.",
            style = MaterialTheme.typography.bodyMedium.copy(
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          )
        }
      }
    } else {
      items(products, key = { it.id }) { product ->
        AgentProductCard(
          product = product,
          country = selectedCountry,
          onClick = { onProductClicked(product) },
          onTestClick = { onTestClicked(product) },
          onBuyClick = { onBuyClicked(product) },
          modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
        )
      }
    }
  }
}

@Composable
private fun MarketplaceHeroBanner(
  selectedCountry: com.example.data.model.ArabCountry,
  onOpenCountryPicker: () -> Unit
) {
  com.example.ui.components.Sabaq3DPerspectiveContainer(
    tiltAngleX = 2.0f,
    tiltAngleY = -3.0f,
    modifier = Modifier.padding(16.dp)
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(22.dp))
        .background(
          Brush.horizontalGradient(
            listOf(
              SabaqGreenSection,
              SabaqGreenCard
            )
          )
        )
        .border(
          width = 1.2.dp,
          brush = Brush.linearGradient(
            listOf(SabaqGoldHighlight, SabaqGoldPrimary, SabaqGreenBright.copy(alpha = 0.4f))
          ),
          shape = RoundedCornerShape(22.dp)
        )
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(SabaqGoldPrimary.copy(alpha = 0.2f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Bolt,
                contentDescription = null,
                tint = SabaqGoldPrimary,
                modifier = Modifier.size(14.dp)
              )
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "سَبَق AI • لكل العرب ومصر",
              fontFamily = ArefRuqaaFontFamily,
              style = MaterialTheme.typography.labelMedium.copy(
                color = SabaqGoldHighlight,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
              )
            )
          }

          Spacer(modifier = Modifier.height(6.dp))

          Text(
            text = "معرض وكلاء الذكاء الاصطناعي 3D",
            fontFamily = ArefRuqaaFontFamily,
            style = MaterialTheme.typography.titleLarge.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 20.sp,
              color = SabaqGoldPrimary
            )
          )

          Spacer(modifier = Modifier.height(4.dp))

          Text(
            text = "تصفح واشترِ وكلاء جاهزين للأعمال بالعملة المحلية، أو اختبر مهاراتهم في المختبر الذكي.",
            style = MaterialTheme.typography.bodySmall.copy(
              color = SabaqTextSecondary,
              lineHeight = 17.sp,
              fontSize = 11.sp
            )
          )

          Spacer(modifier = Modifier.height(10.dp))

          // Country & Currency Switcher Pill
          Surface(
            modifier = Modifier
              .clip(RoundedCornerShape(20.dp))
              .clickable { onOpenCountryPicker() }
              .border(1.dp, SabaqGoldPrimary.copy(alpha = 0.6f), RoundedCornerShape(20.dp)),
            color = SabaqGreenSection.copy(alpha = 0.9f)
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(text = selectedCountry.flagEmoji, fontSize = 14.sp)
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "العملة: ${selectedCountry.currencyNameAr} (${selectedCountry.currencySymbolAr})",
                fontFamily = ArefRuqaaFontFamily,
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 11.sp,
                  color = SabaqGoldHighlight
                )
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "• تغيير",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontSize = 10.sp,
                  color = SabaqGreenBright
                )
              )
            }
          }
        }

        Spacer(modifier = Modifier.width(10.dp))

        // الشعار الرسمي المعتمد لصقر سبق في البانر ثلاثي الأبعاد
        com.example.ui.components.SabaqOfficialLogoView(
          size = 78.dp,
          showGlow = true,
          is3dFloating = true
        )
      }
    }
  }
}

@Composable
private fun CategoriesRow(
  selectedCategory: String,
  onCategorySelected: (String) -> Unit
) {
  val categories = listOf(
    Pair("ALL", "الكل (All)"),
    Pair("Legal", "قانون وامتثال"),
    Pair("CustomerSupport", "خدمة العملاء"),
    Pair("Finance", "المالية والضرائب"),
    Pair("Marketing", "التسويق والمحتوى"),
    Pair("Development", "البرمجة والأمن")
  )

  Row(
    modifier = Modifier
      .fillMaxWidth()
      .horizontalScroll(rememberScrollState()),
    horizontalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    categories.forEach { (catId, label) ->
      val isSelected = selectedCategory == catId
      Surface(
        modifier = Modifier
          .clip(RoundedCornerShape(12.dp))
          .clickable { onCategorySelected(catId) }
          .border(
            width = if (isSelected) 1.5.dp else 0.5.dp,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
            shape = RoundedCornerShape(12.dp)
          )
          .testTag("category_chip_$catId"),
        color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(12.dp)
      ) {
        Text(
          text = label,
          style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
          ),
          modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        )
      }
    }
  }
}

@Composable
fun AgentProductCard(
  product: AgentProduct,
  country: com.example.data.model.ArabCountry = com.example.data.model.ArabCountriesData.EGYPT,
  onClick: () -> Unit,
  onTestClick: () -> Unit,
  onBuyClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .clickable { onClick() }
      .testTag("agent_card_${product.id}"),
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = SabaqGreenCard),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      // Top row: Category, version, rating, executions
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Surface(
          color = SabaqGoldPrimary.copy(alpha = 0.15f),
          shape = RoundedCornerShape(6.dp)
        ) {
          Text(
            text = product.categoryLabelAr,
            fontFamily = ArefRuqaaFontFamily,
            style = MaterialTheme.typography.labelSmall.copy(
              color = SabaqGoldPrimary,
              fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
          )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = SabaqGoldPrimary,
            modifier = Modifier.size(15.dp)
          )
          Spacer(modifier = Modifier.width(3.dp))
          Text(
            text = "${product.developerRating}",
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              color = SabaqGoldHighlight
            )
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "• ${product.executionsCount} تشغيل",
            style = MaterialTheme.typography.labelSmall.copy(
              color = SabaqTextSecondary
            )
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Title & English Subtitle
      Text(
        text = product.titleAr,
        fontFamily = ArefRuqaaFontFamily,
        style = MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.Bold,
          fontSize = 18.sp,
          color = SabaqGoldLight
        )
      )
      Text(
        text = product.titleEn,
        style = MaterialTheme.typography.labelSmall.copy(
          color = SabaqTextSecondary
        )
      )

      Spacer(modifier = Modifier.height(6.dp))

      // Description
      Text(
        text = product.descriptionAr,
        style = MaterialTheme.typography.bodySmall.copy(
          color = SabaqTextPrimary,
          lineHeight = 18.sp
        ),
        maxLines = 2
      )

      Spacer(modifier = Modifier.height(12.dp))

      // Capabilities snippet
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.AutoAwesome,
          contentDescription = null,
          tint = SabaqGreenBright,
          modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = product.capabilities.split("،", ",").take(3).joinToString(" • ") { it.trim() },
          style = MaterialTheme.typography.labelSmall.copy(
            color = SabaqGreenBright,
            fontSize = 11.sp
          ),
          maxLines = 1
        )
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Bottom Row: Price & Action Buttons
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = com.example.data.model.ArabCountriesData.formatPrice(product.priceSar, country),
              fontFamily = ArefRuqaaFontFamily,
              style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = SabaqGoldPrimary
              )
            )
          }

          if (product.isSubscriptionIncluded) {
            Text(
              text = "✓ مشمول باشتراك Pro",
              style = MaterialTheme.typography.labelSmall.copy(
                color = SabaqGreenBright,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
              )
            )
          }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedButton(
            onClick = onTestClick,
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.outlinedButtonColors(
              contentColor = SabaqGreenBright
            ),
            border = androidx.compose.foundation.BorderStroke(1.dp, SabaqGreenBright.copy(alpha = 0.5f)),
            modifier = Modifier.testTag("test_btn_${product.id}")
          ) {
            Icon(
              imageVector = Icons.Default.PlayArrow,
              contentDescription = null,
              tint = SabaqGreenBright,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("اختبار", fontFamily = ArefRuqaaFontFamily, fontWeight = FontWeight.Bold, fontSize = 13.sp)
          }

          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(10.dp))
              .background(
                if (product.isPurchased) Brush.linearGradient(listOf(SabaqGreenBright, SabaqGreenPrimary))
                else SabaqButtonGradient
              )
              .clickable { onBuyClick() }
              .padding(horizontal = 14.dp, vertical = 9.dp)
              .testTag("buy_btn_${product.id}"),
            contentAlignment = Alignment.Center
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = if (product.isPurchased) Icons.Default.Check else Icons.Default.ShoppingBag,
                contentDescription = null,
                tint = if (product.isPurchased) Color.White else SabaqBackground,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = if (product.isPurchased) "مفعل" else "شراء",
                fontFamily = ArefRuqaaFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = if (product.isPurchased) Color.White else SabaqBackground
              )
            }
          }
        }
      }
    }
  }
}
