package com.example.ui.dialogs

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.*
import com.example.ui.viewmodel.CheckoutUiState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CheckoutDialog(
  state: CheckoutUiState,
  country: com.example.data.model.ArabCountry = com.example.data.model.ArabCountriesData.EGYPT,
  onDismiss: () -> Unit,
  onSelectPaymentMethod: (String) -> Unit,
  onUpdateCardField: (number: String?, holder: String?, expiry: String?, cvv: String?) -> Unit,
  onSubmitPayment: () -> Unit,
  modifier: Modifier = Modifier
) {
  if (!state.isOpen) return

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
        // Dialog Header with Official Logo
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            // الشعار الرسمي لصقر سبق الذهبي
            com.example.ui.components.SabaqOfficialLogoView(
              size = 40.dp,
              showGlow = true,
              is3dFloating = false
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = "بوابة الدفع الآمنة - سبق",
                  fontFamily = ArefRuqaaFontFamily,
                  style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = SabaqGoldPrimary
                  )
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = country.flagEmoji,
                  fontSize = 15.sp
                )
              }
              Text(
                text = "دفع آمن بالعملة المحلية (${country.currencySymbolAr} - ${country.currencyNameAr})",
                style = MaterialTheme.typography.labelSmall.copy(
                  color = SabaqGreenBright,
                  fontSize = 10.sp
                )
              )
            }
          }

          IconButton(
            onClick = onDismiss,
            modifier = Modifier.testTag("close_checkout_btn")
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "إغلاق",
              tint = SabaqTextSecondary
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Success Receipt View
        if (state.completedTransaction != null) {
          SuccessReceiptView(
            transaction = state.completedTransaction,
            onClose = onDismiss
          )
        } else {
          // 3D Virtual Card Preview with Official Falcon Logo
          com.example.ui.components.Sabaq3DPerspectiveContainer(tiltAngleX = 3.5f, tiltAngleY = -4f) {
            Card(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
              shape = RoundedCornerShape(18.dp),
              colors = CardDefaults.cardColors(
                containerColor = SabaqGreenCard
              ),
              border = androidx.compose.foundation.BorderStroke(
                1.5.dp,
                Brush.linearGradient(
                  listOf(
                    SabaqGoldHighlight,
                    SabaqGoldPrimary,
                    SabaqGreenBright
                  )
                )
              )
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
                    com.example.ui.components.SabaqOfficialLogoView(
                      size = 32.dp,
                      showGlow = false,
                      is3dFloating = false
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                      text = "سَبَق AI",
                      fontFamily = ArefRuqaaFontFamily,
                      fontWeight = FontWeight.Bold,
                      color = SabaqGoldPrimary,
                      fontSize = 16.sp
                    )
                  }
                  Text(
                    text = "${country.flagEmoji} ${country.currencyCode}",
                    style = MaterialTheme.typography.labelSmall.copy(
                      fontWeight = FontWeight.Bold,
                      color = SabaqGoldHighlight,
                      fontSize = 12.sp
                    )
                  )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                  text = state.cardNumber,
                  style = MaterialTheme.typography.titleMedium.copy(
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                    letterSpacing = 2.sp,
                    color = SabaqTextPrimary
                  )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Text(
                    text = state.cardHolder,
                    style = MaterialTheme.typography.bodySmall.copy(
                      color = SabaqTextSecondary,
                      fontWeight = FontWeight.SemiBold
                    )
                  )
                  Text(
                    text = state.cardExpiry,
                    style = MaterialTheme.typography.bodySmall.copy(
                      color = SabaqTextSecondary
                    )
                  )
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          // Checkout Flow
          OrderSummaryCard(state = state, country = country)

          Spacer(modifier = Modifier.height(16.dp))

          Text(
            text = "وسائل الدفع المعتمدة في ${country.nameAr}:",
            style = MaterialTheme.typography.bodyMedium.copy(
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
          )

          Spacer(modifier = Modifier.height(10.dp))

          PaymentMethodsSelector(
            selectedMethod = state.paymentMethod,
            country = country,
            onSelect = onSelectPaymentMethod
          )

          Spacer(modifier = Modifier.height(16.dp))

          // Specific Country Method inputs
          when (state.paymentMethod) {
            "INSTAPAY" -> {
              Surface(
                modifier = Modifier.fillMaxWidth(),
                color = SabaqGreenCard,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SabaqGreenBright.copy(alpha = 0.4f))
              ) {
                Column(modifier = Modifier.padding(12.dp)) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "⚡", fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                      text = "الدفع الفوري عبر إنستاباي (InstaPay)",
                      fontWeight = FontWeight.Bold,
                      color = SabaqGreenBright,
                      fontSize = 13.sp
                    )
                  }
                  Spacer(modifier = Modifier.height(6.dp))
                  Text(
                    text = "سيتم تزويدك بعنوان الدفع اللحظي IPA: sabaq@instapay لتحويل المبلغ (${com.example.data.model.ArabCountriesData.formatPrice(state.amountSar, country)}) فورياً بدون أي عمولة.",
                    style = MaterialTheme.typography.bodySmall.copy(
                      color = SabaqTextSecondary,
                      fontSize = 11.sp
                    )
                  )
                }
              }
            }
            "FAWRY" -> {
              Surface(
                modifier = Modifier.fillMaxWidth(),
                color = SabaqGreenCard,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SabaqGoldPrimary.copy(alpha = 0.4f))
              ) {
                Column(modifier = Modifier.padding(12.dp)) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🏪", fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                      text = "الدفع عبر فوري (Fawry Pay)",
                      fontWeight = FontWeight.Bold,
                      color = SabaqGoldPrimary,
                      fontSize = 13.sp
                    )
                  }
                  Spacer(modifier = Modifier.height(6.dp))
                  Text(
                    text = "رقم الدفع المرجعي لفوري: 789 421 90 (صالح لمدة 24 ساعة في أي فرع أو تطبيق فوري).",
                    style = MaterialTheme.typography.bodySmall.copy(
                      color = SabaqTextSecondary,
                      fontSize = 11.sp
                    )
                  )
                }
              }
            }
            "VODAFONE_CASH" -> {
              Surface(
                modifier = Modifier.fillMaxWidth(),
                color = SabaqGreenCard,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SabaqAmber.copy(alpha = 0.4f))
              ) {
                Column(modifier = Modifier.padding(12.dp)) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "📱", fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                      text = "محافظ المحمول الذكية (فودافون/إتصالات/وي كاش)",
                      fontWeight = FontWeight.Bold,
                      color = SabaqAmber,
                      fontSize = 13.sp
                    )
                  }
                  Spacer(modifier = Modifier.height(6.dp))
                  Text(
                    text = "أدخل رقم المحفظة لتأكيد سحب المبلغ (${com.example.data.model.ArabCountriesData.formatPrice(state.amountSar, country)}) عبر إشعار USSD الفوري.",
                    style = MaterialTheme.typography.bodySmall.copy(
                      color = SabaqTextSecondary,
                      fontSize = 11.sp
                    )
                  )
                }
              }
            }
            "STC_PAY" -> {
              StcPayInput()
            }
            "APPLE_PAY" -> {
              ApplePayFastCheckout(amountSar = state.amountSar)
            }
            else -> {
              CardDetailsInputs(
                state = state,
                onUpdate = onUpdateCardField
              )
            }
          }

          Spacer(modifier = Modifier.height(18.dp))

          // Security & compliance badges
          SecurityBadgesRow()

          Spacer(modifier = Modifier.height(20.dp))

          // Pay Button
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(54.dp)
              .clip(RoundedCornerShape(14.dp))
              .background(if (state.isProcessing) Brush.linearGradient(listOf(SabaqGoldWarm, SabaqGoldBronze)) else SabaqButtonGradient)
              .clickable(enabled = !state.isProcessing) { onSubmitPayment() }
              .testTag("confirm_payment_btn"),
            contentAlignment = Alignment.Center
          ) {
            if (state.isProcessing) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                CircularProgressIndicator(
                  modifier = Modifier.size(24.dp),
                  color = SabaqBackground,
                  strokeWidth = 2.5.dp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                  text = "جاري معالجة الدفع والتوثيق البنكي...",
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
                  imageVector = Icons.Default.VerifiedUser,
                  contentDescription = null,
                  tint = SabaqBackground,
                  modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = "إتمام الدفع الآمن (${com.example.data.model.ArabCountriesData.formatPrice(state.amountSar, country)})",
                  fontFamily = ArefRuqaaFontFamily,
                  style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = SabaqBackground
                  )
                )
              }
            }
          }
        }
      }
    }
  }
}

@Composable
private fun OrderSummaryCard(
  state: CheckoutUiState,
  country: com.example.data.model.ArabCountry
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = SabaqGreenCard
    )
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(
          text = if (state.isSubscription) "الاشتراك الشهري لدعم المطورين:" else "المنتج المطلوب:",
          style = MaterialTheme.typography.bodySmall.copy(
            color = SabaqTextSecondary
          )
        )
        Text(
          text = if (state.isSubscription) "تجديد شهري" else "شراء لمرة واحدة",
          style = MaterialTheme.typography.labelSmall.copy(
            color = SabaqGreenBright,
            fontWeight = FontWeight.Bold
          )
        )
      }

      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = state.itemTitle,
        fontFamily = ArefRuqaaFontFamily,
        style = MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.Bold,
          fontSize = 17.sp,
          color = SabaqGoldPrimary
        )
      )

      Spacer(modifier = Modifier.height(8.dp))
      HorizontalDivider(color = Color(0xFF134529))
      Spacer(modifier = Modifier.height(8.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "الإجمالي المستحق بالعملة المحلية:",
            style = MaterialTheme.typography.bodySmall.copy(
              color = SabaqTextSecondary
            )
          )
          Text(
            text = "الدولة: ${country.flagEmoji} ${country.nameAr}",
            style = MaterialTheme.typography.labelSmall.copy(
              color = SabaqGreenBright,
              fontSize = 10.sp
            )
          )
        }
        Text(
          text = com.example.data.model.ArabCountriesData.formatPrice(state.amountSar, country),
          fontFamily = ArefRuqaaFontFamily,
          style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            color = SabaqGoldPrimary
          )
        )
      }
    }
  }
}

@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
private fun PaymentMethodsSelector(
  selectedMethod: String,
  country: com.example.data.model.ArabCountry,
  onSelect: (String) -> Unit
) {
  val methods = country.paymentMethods

  androidx.compose.foundation.layout.FlowRow(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    methods.forEach { method ->
      val isSelected = selectedMethod == method.id
      Surface(
        modifier = Modifier
          .clip(RoundedCornerShape(12.dp))
          .clickable { onSelect(method.id) }
          .border(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.35f),
            shape = RoundedCornerShape(12.dp)
          )
          .testTag("pay_method_${method.id}"),
        color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(12.dp)
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(text = method.iconEmoji, fontSize = 16.sp)
          Spacer(modifier = Modifier.width(6.dp))
          Column {
            Text(
              text = method.nameAr,
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
              )
            )
            if (method.badgeText != null) {
              Text(
                text = method.badgeText,
                style = MaterialTheme.typography.labelSmall.copy(
                  fontSize = 9.sp,
                  color = SabaqGoldHighlight
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
private fun CardDetailsInputs(
  state: CheckoutUiState,
  onUpdate: (number: String?, holder: String?, expiry: String?, cvv: String?) -> Unit
) {
  Column(modifier = Modifier.fillMaxWidth()) {
    OutlinedTextField(
      value = state.cardNumber,
      onValueChange = { onUpdate(it, null, null, null) },
      label = { Text("رقم البطاقة (Card Number)") },
      leadingIcon = {
        Icon(
          imageVector = Icons.Default.CreditCard,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.primary
        )
      },
      modifier = Modifier
        .fillMaxWidth()
        .testTag("card_number_input"),
      shape = RoundedCornerShape(12.dp),
      singleLine = true
    )

    Spacer(modifier = Modifier.height(10.dp))

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      OutlinedTextField(
        value = state.cardExpiry,
        onValueChange = { onUpdate(null, null, it, null) },
        label = { Text("الانتهاء MM/YY") },
        modifier = Modifier
          .weight(1f)
          .testTag("card_expiry_input"),
        shape = RoundedCornerShape(12.dp),
        singleLine = true
      )

      OutlinedTextField(
        value = state.cardCvv,
        onValueChange = { onUpdate(null, null, null, it) },
        label = { Text("رمز الأمان CVV") },
        modifier = Modifier
          .weight(1f)
          .testTag("card_cvv_input"),
        shape = RoundedCornerShape(12.dp),
        singleLine = true
      )
    }

    Spacer(modifier = Modifier.height(10.dp))

    OutlinedTextField(
      value = state.cardHolder,
      onValueChange = { onUpdate(null, it, null, null) },
      label = { Text("اسم حامل البطاقة (Cardholder Name)") },
      modifier = Modifier
        .fillMaxWidth()
        .testTag("card_holder_input"),
      shape = RoundedCornerShape(12.dp),
      singleLine = true
    )
  }
}

@Composable
private fun StcPayInput() {
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
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.PhoneAndroid,
          contentDescription = null,
          tint = SabaqBlue
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "الدفع الفوري عبر محفظة STC Pay",
          style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
        )
      }
      Spacer(modifier = Modifier.height(8.dp))
      Text(
        text = "سيصلك إشعار فوري على تطبيق STC Pay المسجل برقم جوالك لتأكيد عملية الدفع بلمسة واحدة.",
        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
      )
    }
  }
}

@Composable
private fun ApplePayFastCheckout(amountSar: Double) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .height(60.dp)
      .clip(RoundedCornerShape(14.dp))
      .background(Color.Black),
    contentAlignment = Alignment.Center
  ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      Text(
        text = " Pay",
        style = MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.Bold,
          color = Color.White,
          fontSize = 20.sp
        )
      )
      Spacer(modifier = Modifier.width(8.dp))
      Text(
        text = "• الضغط مرتين للمتابعة",
        style = MaterialTheme.typography.bodyMedium.copy(color = Color.White.copy(alpha = 0.8f))
      )
    }
  }
}

@Composable
private fun SecurityBadgesRow() {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceEvenly,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      Icon(
        imageVector = Icons.Default.Security,
        contentDescription = null,
        tint = SabaqEmerald,
        modifier = Modifier.size(16.dp)
      )
      Spacer(modifier = Modifier.width(4.dp))
      Text(
        text = "تشفير بنكي 256-bit",
        style = MaterialTheme.typography.labelSmall.copy(
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontSize = 11.sp
        )
      )
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
      Icon(
        imageVector = Icons.Default.VerifiedUser,
        contentDescription = null,
        tint = SabaqBlue,
        modifier = Modifier.size(16.dp)
      )
      Spacer(modifier = Modifier.width(4.dp))
      Text(
        text = "ضمان استرداد 14 يوم",
        style = MaterialTheme.typography.labelSmall.copy(
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontSize = 11.sp
        )
      )
    }
  }
}

@Composable
private fun SuccessReceiptView(
  transaction: com.example.data.model.TransactionRecord,
  onClose: () -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 12.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Box(
      modifier = Modifier
        .size(64.dp)
        .clip(CircleShape)
        .background(SabaqEmerald.copy(alpha = 0.15f)),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = Icons.Default.CheckCircle,
        contentDescription = "Success",
        tint = SabaqEmerald,
        modifier = Modifier.size(44.dp)
      )
    }

    Spacer(modifier = Modifier.height(12.dp))

    Text(
      text = "تمت العملية بنجاح!",
      style = MaterialTheme.typography.titleLarge.copy(
        fontWeight = FontWeight.Bold,
        color = SabaqEmerald
      )
    )

    Spacer(modifier = Modifier.height(6.dp))

    Text(
      text = "شكراً لدعمك مطوري الذكاء الاصطناعي عبر منصة سبق",
      style = MaterialTheme.typography.bodyMedium.copy(
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center
      )
    )

    Spacer(modifier = Modifier.height(18.dp))

    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp)
      ) {
        ReceiptRow(label = "رقم الفاتورة:", value = transaction.receiptNumber)
        ReceiptRow(label = "نوع العملية:", value = transaction.titleAr)
        ReceiptRow(
          label = "المبلغ المدفوع:",
          value = "${String.format(Locale.US, "%.2f", transaction.amountSar)} ر.س"
        )
        ReceiptRow(label = "وسيلة الدفع:", value = transaction.paymentMethod)
        val dateStr = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date(transaction.timestamp))
        ReceiptRow(label = "التاريخ والوقت:", value = dateStr)
        ReceiptRow(label = "حالة العملية:", value = "مكتملة وموثقة (SABAQ-AUTH)")
      }
    }

    Spacer(modifier = Modifier.height(20.dp))

    Button(
      onClick = onClose,
      modifier = Modifier
        .fillMaxWidth()
        .height(50.dp)
        .testTag("receipt_done_btn"),
      shape = RoundedCornerShape(14.dp)
    ) {
      Text(
        text = "متابعة الاستخدام",
        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
      )
    }
  }
}

@Composable
private fun ReceiptRow(label: String, value: String) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp),
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Text(
      text = label,
      style = MaterialTheme.typography.labelSmall.copy(
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    )
    Text(
      text = value,
      style = MaterialTheme.typography.labelSmall.copy(
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )
    )
  }
}
