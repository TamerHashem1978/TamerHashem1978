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
        // Dialog Header
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
                imageVector = Icons.Default.Lock,
                contentDescription = "Secure Checkout",
                tint = SabaqGoldPrimary,
                modifier = Modifier.size(20.dp)
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "بوابة الدفع الآمنة - سبق",
                fontFamily = ArefRuqaaFontFamily,
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 18.sp,
                  color = SabaqGoldPrimary
                )
              )
              Text(
                text = "gosabaq.com/checkout • مشفر 256-bit",
                style = MaterialTheme.typography.labelSmall.copy(
                  color = SabaqGreenBright
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
          // Checkout Flow
          OrderSummaryCard(state = state)

          Spacer(modifier = Modifier.height(18.dp))

          Text(
            text = "اختر وسيلة الدفع المعتمدة:",
            style = MaterialTheme.typography.bodyMedium.copy(
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
          )

          Spacer(modifier = Modifier.height(10.dp))

          PaymentMethodsSelector(
            selectedMethod = state.paymentMethod,
            onSelect = onSelectPaymentMethod
          )

          Spacer(modifier = Modifier.height(16.dp))

          // Card details when MADA, VISA or Apple Pay selected
          if (state.paymentMethod == "MADA" || state.paymentMethod == "VISA") {
            CardDetailsInputs(
              state = state,
              onUpdate = onUpdateCardField
            )
          } else if (state.paymentMethod == "STC_PAY") {
            StcPayInput()
          } else {
            ApplePayFastCheckout(amountSar = state.amountSar)
          }

          Spacer(modifier = Modifier.height(18.dp))

          // Security & SAMA compliance badges
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
                  text = "إتمام الدفع الآمن (${String.format(Locale.US, "%.2f", state.amountSar)} ر.س)",
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
private fun OrderSummaryCard(state: CheckoutUiState) {
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
        Text(
          text = "الإجمالي المستحق (شامل ضريبة 15%):",
          style = MaterialTheme.typography.bodySmall.copy(
            color = SabaqTextSecondary
          )
        )
        Text(
          text = "${String.format(Locale.US, "%.2f", state.amountSar)} ر.س",
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

@Composable
private fun PaymentMethodsSelector(
  selectedMethod: String,
  onSelect: (String) -> Unit
) {
  val methods = listOf(
    Pair("MADA", "مدى Mada"),
    Pair("APPLE_PAY", "Apple Pay"),
    Pair("STC_PAY", "STC Pay"),
    Pair("VISA", "Visa/Master")
  )

  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    methods.forEach { (code, label) ->
      val isSelected = selectedMethod == code
      Surface(
        modifier = Modifier
          .weight(1f)
          .height(48.dp)
          .clip(RoundedCornerShape(12.dp))
          .clickable { onSelect(code) }
          .border(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
            shape = RoundedCornerShape(12.dp)
          )
          .testTag("pay_method_$code"),
        color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(12.dp)
      ) {
        Box(
          contentAlignment = Alignment.Center,
          modifier = Modifier.padding(4.dp)
        ) {
          Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
              color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
              textAlign = TextAlign.Center
            )
          )
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
