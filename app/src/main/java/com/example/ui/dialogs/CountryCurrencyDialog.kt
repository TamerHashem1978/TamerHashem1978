package com.example.ui.dialogs

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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.ArabCountriesData
import com.example.data.model.ArabCountry
import com.example.ui.theme.*

@Composable
fun CountryCurrencyDialog(
  isOpen: Boolean,
  selectedCountry: ArabCountry,
  onDismiss: () -> Unit,
  onCountrySelected: (ArabCountry) -> Unit,
  modifier: Modifier = Modifier
) {
  if (!isOpen) return

  var searchQuery by remember { mutableStateOf("") }
  val filteredCountries = remember(searchQuery) {
    if (searchQuery.isBlank()) ArabCountriesData.list
    else {
      ArabCountriesData.list.filter {
        it.nameAr.contains(searchQuery, ignoreCase = true) ||
            it.currencyCode.contains(searchQuery, ignoreCase = true) ||
            it.currencyNameAr.contains(searchQuery, ignoreCase = true) ||
            it.currencySymbolAr.contains(searchQuery, ignoreCase = true)
      }
    }
  }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = modifier
        .fillMaxWidth(0.92f)
        .clip(RoundedCornerShape(24.dp))
        .border(
          width = 1.dp,
          brush = Brush.linearGradient(
            listOf(SabaqGoldPrimary.copy(alpha = 0.5f), SabaqGreenBright.copy(alpha = 0.3f))
          ),
          shape = RoundedCornerShape(24.dp)
        ),
      color = SabaqGreenSection,
      tonalElevation = 10.dp
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp)
      ) {
        // Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(SabaqGoldPrimary.copy(alpha = 0.15f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Public,
                contentDescription = null,
                tint = SabaqGoldPrimary,
                modifier = Modifier.size(24.dp)
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "اختر الدولة والعملة",
                fontFamily = ArefRuqaaFontFamily,
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 18.sp,
                  color = SabaqGoldPrimary
                )
              )
              Text(
                text = "منصة سبق موجهة لكل الدول العربية ومصر",
                style = MaterialTheme.typography.labelSmall.copy(
                  color = SabaqGreenBright,
                  fontSize = 11.sp
                )
              )
            }
          }

          IconButton(
            onClick = onDismiss,
            modifier = Modifier.testTag("close_country_dialog_btn")
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "إغلاق",
              tint = SabaqTextSecondary
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Search Bar
        OutlinedTextField(
          value = searchQuery,
          onValueChange = { searchQuery = it },
          placeholder = { Text("ابحث عن الدولة أو العملة (مصر، ريال، جنيه...)") },
          leadingIcon = {
            Icon(
              imageVector = Icons.Default.Search,
              contentDescription = null,
              tint = SabaqGoldPrimary
            )
          },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("country_search_input"),
          shape = RoundedCornerShape(14.dp),
          singleLine = true
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Country List
        LazyColumn(
          modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 380.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          items(filteredCountries, key = { it.code }) { country ->
            val isSelected = country.code == selectedCountry.code
            Card(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .clickable { onCountrySelected(country) }
                .border(
                  width = if (isSelected) 1.5.dp else 0.5.dp,
                  color = if (isSelected) SabaqGoldPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                  shape = RoundedCornerShape(14.dp)
                )
                .testTag("country_item_${country.code}"),
              shape = RoundedCornerShape(14.dp),
              colors = CardDefaults.cardColors(
                containerColor = if (isSelected) SabaqGoldPrimary.copy(alpha = 0.14f) else SabaqGreenCard
              )
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                  Text(
                    text = country.flagEmoji,
                    fontSize = 28.sp
                  )
                  Spacer(modifier = Modifier.width(12.dp))
                  Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                      Text(
                        text = country.nameAr,
                        style = MaterialTheme.typography.bodyMedium.copy(
                          fontWeight = FontWeight.Bold,
                          color = if (isSelected) SabaqGoldPrimary else SabaqTextPrimary
                        )
                      )
                      if (country.code == "EG") {
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                          color = SabaqGoldPrimary.copy(alpha = 0.2f),
                          shape = RoundedCornerShape(6.dp)
                        ) {
                          Text(
                            text = "مُوصى به",
                            style = MaterialTheme.typography.labelSmall.copy(
                              fontSize = 9.sp,
                              fontWeight = FontWeight.Bold,
                              color = SabaqGoldPrimary
                            ),
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                          )
                        }
                      }
                    }
                    Text(
                      text = "${country.currencyNameAr} (${country.currencySymbolAr} - ${country.currencyCode})",
                      style = MaterialTheme.typography.labelSmall.copy(
                        color = SabaqTextSecondary,
                        fontSize = 11.sp
                      )
                    )
                  }
                }

                if (isSelected) {
                  Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "تم الاختيار",
                    tint = SabaqGoldPrimary,
                    modifier = Modifier.size(22.dp)
                  )
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider(color = SabaqGoldPrimary.copy(alpha = 0.2f))
        Spacer(modifier = Modifier.height(10.dp))

        // Info footer
        Text(
          text = "يتم تحديث أسعار الصرف تلقائياً مع توفير طرق الدفع المعتمدة في دولتك (مثل فوري وإنستاباي في مصر، ومدى في السعودية).",
          style = MaterialTheme.typography.labelSmall.copy(
            color = SabaqTextSecondary,
            fontSize = 11.sp,
            lineHeight = 16.sp
          )
        )
      }
    }
  }
}
