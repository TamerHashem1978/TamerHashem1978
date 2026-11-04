package com.example.data.model

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

data class CountryPaymentMethod(
  val id: String,
  val nameAr: String,
  val iconEmoji: String,
  val badgeText: String? = null
)

data class ArabCountry(
  val code: String,
  val nameAr: String,
  val flagEmoji: String,
  val currencyCode: String,
  val currencySymbolAr: String,
  val currencyNameAr: String,
  val rateFromSar: Double,
  val defaultPaymentMethod: String,
  val paymentMethods: List<CountryPaymentMethod>
)

object ArabCountriesData {

  val EGYPT = ArabCountry(
    code = "EG",
    nameAr = "جمهورية مصر العربية",
    flagEmoji = "🇪🇬",
    currencyCode = "EGP",
    currencySymbolAr = "ج.م",
    currencyNameAr = "الجنيه المصري",
    rateFromSar = 13.08,
    defaultPaymentMethod = "INSTAPAY",
    paymentMethods = listOf(
      CountryPaymentMethod("INSTAPAY", "إنستاباي (InstaPay)", "⚡", "فوري بدون رسوم"),
      CountryPaymentMethod("FAWRY", "فوري (Fawry Pay)", "🏪", "أكثر من 300 ألف نقطة"),
      CountryPaymentMethod("VODAFONE_CASH", "محافظ المحمول (فودافون/إتصالات/وي كاش)", "📱", "كاش لحظي"),
      CountryPaymentMethod("MEEZA", "بطاقة ميزة الوطنية", "💳", "دعم الدفع المحلي"),
      CountryPaymentMethod("VISA_MASTERCARD", "فيزا / ماستركارد", "🌐", "دولي ومحلي")
    )
  )

  val SAUDI_ARABIA = ArabCountry(
    code = "SA",
    nameAr = "المملكة العربية السعودية",
    flagEmoji = "🇸🇦",
    currencyCode = "SAR",
    currencySymbolAr = "ر.س",
    currencyNameAr = "الريال السعودي",
    rateFromSar = 1.0,
    defaultPaymentMethod = "MADA",
    paymentMethods = listOf(
      CountryPaymentMethod("MADA", "مدى (Mada)", "💳", "البطاقة الوطنية"),
      CountryPaymentMethod("APPLE_PAY", "Apple Pay", "🍏", "بلمسة واحدة"),
      CountryPaymentMethod("STC_PAY", "STC Pay", "📲", "المحفظة الرقمية"),
      CountryPaymentMethod("VISA_MASTERCARD", "Visa / MasterCard", "🌐", "دولي")
    )
  )

  val UAE = ArabCountry(
    code = "AE",
    nameAr = "الإمارات العربية المتحدة",
    flagEmoji = "🇦🇪",
    currencyCode = "AED",
    currencySymbolAr = "د.إ",
    currencyNameAr = "الدرهم الإماراتي",
    rateFromSar = 0.98,
    defaultPaymentMethod = "APPLE_PAY",
    paymentMethods = listOf(
      CountryPaymentMethod("APPLE_PAY", "Apple Pay", "🍏", "فوري"),
      CountryPaymentMethod("CARD", "بطاقات الائتمان البنكية", "💳", "Visa / Master"),
      CountryPaymentMethod("DUBAI_PAY", "دبي باي (DubaiPay)", "🇦🇪", "حكومي وآمن")
    )
  )

  val KUWAIT = ArabCountry(
    code = "KW",
    nameAr = "دولة الكويت",
    flagEmoji = "🇰🇼",
    currencyCode = "KWD",
    currencySymbolAr = "د.ك",
    currencyNameAr = "الدينار الكويتي",
    rateFromSar = 0.082,
    defaultPaymentMethod = "KNET",
    paymentMethods = listOf(
      CountryPaymentMethod("KNET", "كي نت (KNET)", "💳", "شبكة الدفع الوطنية"),
      CountryPaymentMethod("APPLE_PAY", "Apple Pay", "🍏", "فوري"),
      CountryPaymentMethod("CARD", "Visa / MasterCard", "🌐", "ائتماني")
    )
  )

  val QATAR = ArabCountry(
    code = "QA",
    nameAr = "دولة قطر",
    flagEmoji = "🇶🇦",
    currencyCode = "QAR",
    currencySymbolAr = "ر.ق",
    currencyNameAr = "الريال القطري",
    rateFromSar = 0.97,
    defaultPaymentMethod = "NAPS",
    paymentMethods = listOf(
      CountryPaymentMethod("NAPS", "نظام قطر للدفع (NAPS)", "💳", "وطني"),
      CountryPaymentMethod("APPLE_PAY", "Apple Pay", "🍏", "فوري"),
      CountryPaymentMethod("CARD", "Visa / Master", "🌐", "ائتماني")
    )
  )

  val JORDAN = ArabCountry(
    code = "JO",
    nameAr = "المملكة الأردنية الهاشمية",
    flagEmoji = "🇯🇴",
    currencyCode = "JOD",
    currencySymbolAr = "د.أ",
    currencyNameAr = "الدينار الأردني",
    rateFromSar = 0.189,
    defaultPaymentMethod = "CLIQ",
    paymentMethods = listOf(
      CountryPaymentMethod("CLIQ", "كليك (CliQ)", "⚡", "التحويل الفوري"),
      CountryPaymentMethod("ZAIN_CASH", "زين كاش / أورنج موني", "📱", "محافظ ذكية"),
      CountryPaymentMethod("CARD", "بطاقات الدفع البنكية", "💳", "Visa / Master")
    )
  )

  val MOROCCO = ArabCountry(
    code = "MA",
    nameAr = "المملكة المغربية",
    flagEmoji = "🇲🇦",
    currencyCode = "MAD",
    currencySymbolAr = "د.م",
    currencyNameAr = "الدرهم المغربي",
    rateFromSar = 2.66,
    defaultPaymentMethod = "CMI",
    paymentMethods = listOf(
      CountryPaymentMethod("CMI", "مركز النقديات (CMI)", "💳", "بطاقة بنكية"),
      CountryPaymentMethod("WAFACASH", "وفاكاش / كاش بلس", "🏪", "دفع محلي"),
      CountryPaymentMethod("CARD", "Visa / MasterCard", "🌐", "دولي")
    )
  )

  val ALGERIA = ArabCountry(
    code = "DZ",
    nameAr = "الجمهورية الجزائرية",
    flagEmoji = "🇩🇿",
    currencyCode = "DZD",
    currencySymbolAr = "د.ج",
    currencyNameAr = "الدينار الجزائري",
    rateFromSar = 35.8,
    defaultPaymentMethod = "EDAHABIA",
    paymentMethods = listOf(
      CountryPaymentMethod("EDAHABIA", "البطاقة الذهبية / CIB", "💳", "بريد الجزائر"),
      CountryPaymentMethod("BARIDIMOB", "بريدي موب (BaridiMob)", "📱", "تطبيق بريدي"),
      CountryPaymentMethod("CARD", "Visa / Master", "🌐", "دولي")
    )
  )

  val TUNISIA = ArabCountry(
    code = "TN",
    nameAr = "الجمهورية التونسية",
    flagEmoji = "🇹🇳",
    currencyCode = "TND",
    currencySymbolAr = "د.ت",
    currencyNameAr = "الدينار التونسي",
    rateFromSar = 0.83,
    defaultPaymentMethod = "D17",
    paymentMethods = listOf(
      CountryPaymentMethod("D17", "تطبيق D17 البريدي", "📱", "البريد التونسي"),
      CountryPaymentMethod("SOBFLOUS", "صب فلوس (Sobflous)", "⚡", "محفظة رقمية"),
      CountryPaymentMethod("CARD", "Visa / MasterCard", "💳", "بنكي")
    )
  )

  val OMAN = ArabCountry(
    code = "OM",
    nameAr = "سلطنة عُمان",
    flagEmoji = "🇴🇲",
    currencyCode = "OMR",
    currencySymbolAr = "ر.ع",
    currencyNameAr = "الريال العماني",
    rateFromSar = 0.103,
    defaultPaymentMethod = "THAWANI",
    paymentMethods = listOf(
      CountryPaymentMethod("THAWANI", "ثواني (Thawani Pay)", "📱", "محفظة عُمان"),
      CountryPaymentMethod("OMANNET", "عُمان نت (OmanNet)", "💳", "بطاقة بنكية"),
      CountryPaymentMethod("CARD", "Visa / Master", "🌐", "دولي")
    )
  )

  val BAHRAIN = ArabCountry(
    code = "BH",
    nameAr = "مملكة البحرين",
    flagEmoji = "🇧🇭",
    currencyCode = "BHD",
    currencySymbolAr = "د.ب",
    currencyNameAr = "الدينار البحريني",
    rateFromSar = 0.100,
    defaultPaymentMethod = "BENEFIT_PAY",
    paymentMethods = listOf(
      CountryPaymentMethod("BENEFIT_PAY", "بنفت بي (BenefitPay)", "📱", "التحويل الفوري"),
      CountryPaymentMethod("APPLE_PAY", "Apple Pay", "🍏", "فوري"),
      CountryPaymentMethod("CARD", "Visa / Master", "💳", "ائتماني")
    )
  )

  val IRAQ = ArabCountry(
    code = "IQ",
    nameAr = "جمهورية العراق",
    flagEmoji = "🇮🇶",
    currencyCode = "IQD",
    currencySymbolAr = "د.ع",
    currencyNameAr = "الدينار العراقي",
    rateFromSar = 350.0,
    defaultPaymentMethod = "ZAIN_CASH",
    paymentMethods = listOf(
      CountryPaymentMethod("ZAIN_CASH", "زين كاش (ZainCash)", "📱", "محفظة رقمية"),
      CountryPaymentMethod("QI_CARD", "كي كارد (Qi Card)", "💳", "بطاقة وطنية"),
      CountryPaymentMethod("FIB", "المصرف العراقي الأول (FIB)", "⚡", "دفع سريع")
    )
  )

  val GLOBAL_USD = ArabCountry(
    code = "GLOBAL",
    nameAr = "جميع الدول (دولار أمريكي)",
    flagEmoji = "🌐",
    currencyCode = "USD",
    currencySymbolAr = "$",
    currencyNameAr = "الدولار الأمريكي",
    rateFromSar = 0.267,
    defaultPaymentMethod = "APPLE_PAY",
    paymentMethods = listOf(
      CountryPaymentMethod("APPLE_PAY", "Apple Pay", "🍏", "عالمي"),
      CountryPaymentMethod("GOOGLE_PAY", "Google Pay", "📱", "عالمي"),
      CountryPaymentMethod("CARD", "Credit / Debit Card (Stripe)", "💳", "Visa / Master / Amex")
    )
  )

  val list: List<ArabCountry> = listOf(
    EGYPT,          // مصر بالصدارة بناءً على طلب المستخدم
    SAUDI_ARABIA,
    UAE,
    KUWAIT,
    QATAR,
    JORDAN,
    MOROCCO,
    ALGERIA,
    TUNISIA,
    OMAN,
    BAHRAIN,
    IRAQ,
    GLOBAL_USD
  )

  fun findByCode(code: String): ArabCountry {
    return list.firstOrNull { it.code.equals(code, ignoreCase = true) } ?: EGYPT
  }

  fun formatPrice(amountSar: Double, country: ArabCountry): String {
    val converted = amountSar * country.rateFromSar
    val symbols = DecimalFormatSymbols(Locale.US)
    val formatter = when {
      country.currencyCode == "KWD" || country.currencyCode == "BHD" || country.currencyCode == "OMR" ->
        DecimalFormat("#,##0.000", symbols)
      country.currencyCode == "IQD" ->
        DecimalFormat("#,##0", symbols)
      converted >= 100 ->
        DecimalFormat("#,##0", symbols)
      else ->
        DecimalFormat("#,##0.00", symbols)
    }
    return "${formatter.format(converted)} ${country.currencySymbolAr}"
  }

  fun convertAmount(amountSar: Double, country: ArabCountry): Double {
    return amountSar * country.rateFromSar
  }
}
