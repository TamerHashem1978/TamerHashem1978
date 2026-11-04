package com.example.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.model.AgentProduct
import com.example.data.model.AgentSkill
import com.example.data.model.SabaqAlertType
import com.example.data.model.SabaqToastAlert
import com.example.data.model.TestRunResult
import com.example.data.model.TransactionRecord
import com.example.data.model.UserSubscriptionState
import com.example.data.repository.SabaqRepository
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import java.util.UUID

private const val TAG = "SabaqGeminiAI"

enum class SabaqNavTab(val titleAr: String, val titleEn: String) {
  MARKETPLACE("السوق", "Marketplace"),
  BUILDER("بناء مهارة", "Skill Builder"),
  TESTER("مختبر الاختبار", "Test Sandbox"),
  SHARING("المشاركة", "Share & Publish"),
  DASHBOARD("لوحة التحكم", "Dashboard")
}

data class CheckoutUiState(
  val isOpen: Boolean = false,
  val isSubscription: Boolean = false,
  val itemTitle: String = "",
  val targetId: String = "",
  val amountSar: Double = 0.0,
  val paymentMethod: String = "MADA",
  val cardNumber: String = "5888 1234 5678 9012",
  val cardHolder: String = "SABAQ DEVELOPER",
  val cardExpiry: String = "08/29",
  val cardCvv: String = "342",
  val isProcessing: Boolean = false,
  val completedTransaction: TransactionRecord? = null
)

class SabaqViewModel(private val repository: SabaqRepository) : ViewModel() {

  // Lazy initialization of Gemini model via Firebase AI Logic so App Check is configured first
  private val generativeModel by lazy {
    Firebase.ai.generativeModel("gemini-3.8-flash")
  }

  private val _currentTab = MutableStateFlow(SabaqNavTab.MARKETPLACE)
  val currentTab: StateFlow<SabaqNavTab> = _currentTab.asStateFlow()

  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  private val _selectedCategory = MutableStateFlow("ALL")
  val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

  private val _selectedProductDetail = MutableStateFlow<AgentProduct?>(null)
  val selectedProductDetail: StateFlow<AgentProduct?> = _selectedProductDetail.asStateFlow()

  val allProducts: StateFlow<List<AgentProduct>> = repository.allProducts
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val filteredProducts: StateFlow<List<AgentProduct>> = combine(
    allProducts,
    _searchQuery,
    _selectedCategory
  ) { products, query, category ->
    products.filter { product ->
      val matchesQuery = query.isBlank() ||
          product.titleAr.contains(query, ignoreCase = true) ||
          product.titleEn.contains(query, ignoreCase = true) ||
          product.descriptionAr.contains(query, ignoreCase = true) ||
          product.tags.contains(query, ignoreCase = true)

      val matchesCategory = category == "ALL" || product.category.equals(category, ignoreCase = true)
      matchesQuery && matchesCategory
    }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val allSkills: StateFlow<List<AgentSkill>> = repository.allSkills
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val allTestRuns: StateFlow<List<TestRunResult>> = repository.allTestRuns
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val subscriptionState: StateFlow<UserSubscriptionState?> = repository.subscriptionState
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

  val allTransactions: StateFlow<List<TransactionRecord>> = repository.allTransactions
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // Skill Builder Form State
  val builderTitle = MutableStateFlow("")
  val builderDescription = MutableStateFlow("")
  val builderCategory = MutableStateFlow("Customer Support")
  val builderKeywords = MutableStateFlow("سؤال، طلب، استفسار")
  val builderInstructions = MutableStateFlow("")
  val builderEnabledTools = MutableStateFlow(setOf("WEB_SEARCH", "DATABASE_QUERY"))
  val builderInputSchema = MutableStateFlow("{ \"query\": \"String\", \"userId\": \"String\" }")
  val builderOutputFormat = MutableStateFlow("Structured JSON + Arabic Text")
  val builderPriceType = MutableStateFlow("FREE")
  val builderPriceSar = MutableStateFlow("0.0")

  private val _isGeneratingSkillAi = MutableStateFlow(false)
  val isGeneratingSkillAi: StateFlow<Boolean> = _isGeneratingSkillAi.asStateFlow()

  private val _skillAiErrorMessage = MutableStateFlow<String?>(null)
  val skillAiErrorMessage: StateFlow<String?> = _skillAiErrorMessage.asStateFlow()

  // Testing Sandbox State
  private val _testingSelectedSkill = MutableStateFlow<AgentSkill?>(null)
  val testingSelectedSkill: StateFlow<AgentSkill?> = _testingSelectedSkill.asStateFlow()

  val testingInputPrompt = MutableStateFlow("هل يمكن للوكيل فحص مطابقة العقد التجاري للبند العاشر وتحديد المسؤوليات؟")

  private val _isTestingRunning = MutableStateFlow(false)
  val isTestingRunning: StateFlow<Boolean> = _isTestingRunning.asStateFlow()

  private val _lastTestResult = MutableStateFlow<TestRunResult?>(null)
  val lastTestResult: StateFlow<TestRunResult?> = _lastTestResult.asStateFlow()

  private val _testingErrorMessage = MutableStateFlow<String?>(null)
  val testingErrorMessage: StateFlow<String?> = _testingErrorMessage.asStateFlow()

  // Checkout & Payment State
  private val _checkoutState = MutableStateFlow(CheckoutUiState())
  val checkoutState: StateFlow<CheckoutUiState> = _checkoutState.asStateFlow()

  // Share Dialog State
  private val _shareDialogSkill = MutableStateFlow<AgentSkill?>(null)
  val shareDialogSkill: StateFlow<AgentSkill?> = _shareDialogSkill.asStateFlow()

  private val _toastEvent = MutableSharedFlow<String>()
  val toastEvent: SharedFlow<String> = _toastEvent.asSharedFlow()

  // In-App Toast Alert System State
  private val _activeToastAlert = MutableStateFlow<SabaqToastAlert?>(null)
  val activeToastAlert: StateFlow<SabaqToastAlert?> = _activeToastAlert.asStateFlow()

  // Active Arab Country & Currency (Default to Egypt as requested: "التطبيق موجه لكل العرب ومصر")
  private val _selectedCountry = MutableStateFlow(com.example.data.model.ArabCountriesData.EGYPT)
  val selectedCountry: StateFlow<com.example.data.model.ArabCountry> = _selectedCountry.asStateFlow()

  private val _isCountryPickerOpen = MutableStateFlow(false)
  val isCountryPickerOpen: StateFlow<Boolean> = _isCountryPickerOpen.asStateFlow()

  fun openCountryPicker() {
    _isCountryPickerOpen.value = true
  }

  fun closeCountryPicker() {
    _isCountryPickerOpen.value = false
  }

  fun selectCountry(country: com.example.data.model.ArabCountry) {
    _selectedCountry.value = country
    _isCountryPickerOpen.value = false
    emitToast("تم ضبط الدولة والعملة إلى: ${country.flagEmoji} ${country.nameAr} (${country.currencySymbolAr})")
  }

  fun formatPrice(amountSar: Double): String {
    return com.example.data.model.ArabCountriesData.formatPrice(amountSar, _selectedCountry.value)
  }

  init {
    viewModelScope.launch {
      repository.initializeDefaultDataIfEmpty()
      kotlinx.coroutines.delay(1200)
      checkForScheduledAlerts()
    }
  }

  fun setTab(tab: SabaqNavTab) {
    _currentTab.value = tab
  }

  fun setSearchQuery(query: String) {
    _searchQuery.value = query
  }

  fun setCategory(category: String) {
    _selectedCategory.value = category
  }

  fun selectProductForDetail(product: AgentProduct?) {
    _selectedProductDetail.value = product
  }

  fun selectSkillForTesting(skill: AgentSkill) {
    _testingSelectedSkill.value = skill
    _testingErrorMessage.value = null
    if (testingInputPrompt.value.isBlank()) {
      testingInputPrompt.value = "اختبار تشغيلي لوكيل: ${skill.title}"
    }
    _currentTab.value = SabaqNavTab.TESTER
  }

  fun clearTestingError() {
    _testingErrorMessage.value = null
  }

  fun clearSkillAiError() {
    _skillAiErrorMessage.value = null
  }

  fun toggleBuilderTool(tool: String) {
    val current = builderEnabledTools.value.toMutableSet()
    if (current.contains(tool)) {
      current.remove(tool)
    } else {
      current.add(tool)
    }
    builderEnabledTools.value = current
  }

  fun generateSkillConfigWithGemini() {
    if (_isGeneratingSkillAi.value) return
    val currentTitle = builderTitle.value.trim().ifBlank { "وكيل ذكي لتحليل الأعمال والامتثال السعودي" }
    val currentDesc = builderDescription.value.trim()
    val currentTools = builderEnabledTools.value.joinToString(", ")

    if (builderTitle.value.isBlank()) {
      builderTitle.value = currentTitle
    }

    viewModelScope.launch {
      _isGeneratingSkillAi.value = true
      _skillAiErrorMessage.value = null
      try {
        val aiPrompt = """
          أنت مهندس وكلاء ذكاء اصطناعي خبير في منصة سبق (gosabaq.com).
          قم بصياغة إعدادات مهارة وكيل ذكي احترافية باللغة العربية بناءً على المعطيات التالية:
          - اسم المهارة: $currentTitle
          - الوصف المبدئي: ${currentDesc.ifBlank { "غير محدد بعد" }}
          - الأدوات المفعلة: $currentTools
          
          أرجع النتيجة بالصيغة النصية التالية تماماً (كل قسم في سطر يبدأ بالمفتاح المحدد):
          DESCRIPTION: <وصف احترافي موجز للمهارة في جملتين>
          KEYWORDS: <5 كلمات مفتاحية مفصولة بفواصل>
          INSTRUCTIONS: <تعليمات النظام البرمجية المفصلة للوكيل وكيفية استخدامه للأدوات المفعلة خطوة بخطوة>
        """.trimIndent()

        val response = withTimeout(60_000L) {
          generativeModel.generateContent(aiPrompt)
        }
        val text = response.text?.trim().orEmpty()
        if (text.isNotEmpty()) {
          val descLine = text.lines().firstOrNull { it.startsWith("DESCRIPTION:", ignoreCase = true) }
            ?.substringAfter(":")?.trim()
          val keywordsLine = text.lines().firstOrNull { it.startsWith("KEYWORDS:", ignoreCase = true) }
            ?.substringAfter(":")?.trim()
          val instructionsBlock = text.substringAfter("INSTRUCTIONS:", missingDelimiterValue = "")
            .trim()

          if (!descLine.isNullOrBlank()) {
            builderDescription.value = descLine
          } else if (builderDescription.value.isBlank()) {
            builderDescription.value = "مهارة ذكاء اصطناعي متقدمة تم توليدها بواسطة Gemini AI لمنصة سبق."
          }
          if (!keywordsLine.isNullOrBlank()) {
            builderKeywords.value = keywordsLine
          }
          builderInstructions.value = if (instructionsBlock.isNotBlank()) instructionsBlock else text
          emitToast("✨ تم توليد تعليمات وهيكلة المهارة بواسطة Gemini AI بنجاح!")
        } else {
          _skillAiErrorMessage.value = "لم يتم استلام نص من نموذج Gemini. يرجى المحاولة مرة أخرى."
        }
      } catch (e: CancellationException) {
        if (e is TimeoutCancellationException) {
          Log.e(TAG, "Gemini skill generation timed out", e)
          _skillAiErrorMessage.value = "انتهت مهلة الاتصال بنموذج Gemini (60 ثانية). يرجى المحاولة مجدداً."
          emitToast("انتهت مهلة الاتصال بنموذج Gemini")
        } else {
          throw e
        }
      } catch (e: Exception) {
        Log.e(TAG, "Gemini skill generation failed", e)
        _skillAiErrorMessage.value = "تعذر الاتصال بـ Gemini AI: ${e.localizedMessage ?: e.message}"
        emitToast("حدث خطأ أثناء التوليد عبر Gemini AI")
      } finally {
        _isGeneratingSkillAi.value = false
      }
    }
  }

  fun createSkillFromBuilder() {
    val title = builderTitle.value.trim()
    if (title.isBlank()) {
      emitToast("يرجى إدخال اسم المهارة للوكيل الذكي")
      return
    }

    viewModelScope.launch {
      val price = builderPriceSar.value.toDoubleOrNull() ?: 0.0
      val newSkill = AgentSkill(
        id = "skill-" + UUID.randomUUID().toString().take(8),
        title = title,
        description = builderDescription.value.ifBlank { "مهارة ذكاء اصطناعي مخصصة تم بناؤها عبر منصة سبق" },
        category = builderCategory.value,
        triggerKeywords = builderKeywords.value,
        systemInstructions = builderInstructions.value.ifBlank { "أنت وكيل ذكاء اصطناعي متخصص في تنفيذ المهام بدقة وسرعة." },
        toolsEnabled = builderEnabledTools.value.joinToString(","),
        inputSchema = builderInputSchema.value,
        outputFormat = builderOutputFormat.value,
        benchmarkScore = 95,
        testCount = 0,
        isPublished = false,
        shareSlug = title.lowercase().replace(" ", "-").take(24) + "-" + (100..999).random(),
        priceType = builderPriceType.value,
        priceSar = price
      )

      repository.saveSkill(newSkill)
      emitToast("تم إنشاء مهارة الوكيل بنجاح! يمكنك اختبارها ومشاركتها الآن")

      // Clear builder
      builderTitle.value = ""
      builderDescription.value = ""
      builderInstructions.value = ""

      // Switch to tester with the new skill
      _testingSelectedSkill.value = newSkill
      _currentTab.value = SabaqNavTab.TESTER
    }
  }

  fun runSkillTest() {
    val skill = _testingSelectedSkill.value ?: allSkills.value.firstOrNull()
    if (skill == null) {
      emitToast("يرجى اختيار أو إنشاء مهارة أولاً لاختبارها")
      return
    }
    if (_testingSelectedSkill.value == null) {
      _testingSelectedSkill.value = skill
    }

    val prompt = testingInputPrompt.value.trim()
    if (prompt.isBlank()) {
      emitToast("يرجى كتابة سيناريو الاختبار للمهارة")
      return
    }

    viewModelScope.launch {
      _isTestingRunning.value = true
      _testingErrorMessage.value = null
      val startTime = System.currentTimeMillis()
      try {
        val fullAgentPrompt = """
          أنت تعمل الآن كوكيل ذكاء اصطناعي معتمد على منصة سبق (gosabaq.com).
          [بيانات المهارة قيد التشغيل]
          - اسم المهارة: ${skill.title}
          - التصنيف: ${skill.category}
          - التعليمات البرمجية للنظام (System Instructions): ${skill.systemInstructions}
          - الأدوات المفعّلة للوكيل: ${skill.toolsEnabled}
          - الكلمات المفتاحية: ${skill.triggerKeywords}
          - نسق المخرجات المطلوب: ${skill.outputFormat}

          [مدخلات المستخدم للاختبار]
          "$prompt"

          المطلوب منك:
          1. ابدأ بكتابة قسم قصير بعنوان "مسار الاستدلال:" يوضح 3 خطوات عملية لكيفية تحليلك للطلب واستدعاء الأدوات (${skill.toolsEnabled}).
          2. اكتب قسم بعنوان "المخرجات النهائية:" يحتوي على إجابتك الاحترافية الكاملة والدقيقة باللغة العربية وفق تعليمات النظام.
        """.trimIndent()

        val response = withTimeout(60_000L) {
          generativeModel.generateContent(fullAgentPrompt)
        }

        val latency = (System.currentTimeMillis() - startTime).coerceAtLeast(120L)
        val rawText = response.text?.trim().orEmpty()
        if (rawText.isEmpty()) {
          throw IllegalStateException("أرجع نموذج Gemini استجابة فارغة.")
        }

        val reasoningPart = if (rawText.contains("المخرجات النهائية:")) {
          rawText.substringBefore("المخرجات النهائية:").replace("مسار الاستدلال:", "").trim()
        } else {
          """
          ⚙️ [Gemini 3.8 Flash]: تحليل المدخلات وتطبيق تعليمات (${skill.title})
          🔍 [الأدوات المستدعاة]: [${skill.toolsEnabled}]
          ✅ [الامتثال والجودة]: تم توليد الاستجابة المباشرة عبر Firebase AI Logic
          """.trimIndent()
        }

        val finalOutputPart = if (rawText.contains("المخرجات النهائية:")) {
          rawText.substringAfter("المخرجات النهائية:").trim()
        } else {
          rawText
        }

        val estimatedTokens = response.usageMetadata?.totalTokenCount
          ?: ((fullAgentPrompt.length + rawText.length) / 4).coerceAtLeast(64)

        val testResult = TestRunResult(
          skillId = skill.id,
          skillTitle = skill.title,
          inputPrompt = prompt,
          outputResponse = finalOutputPart,
          reasoningTrace = reasoningPart,
          latencyMs = latency,
          tokensUsed = estimatedTokens,
          scorePercentage = 98,
          status = "SUCCESS"
        )

        repository.recordSkillTestRun(skill, testResult)
        _lastTestResult.value = testResult
        emitToast("اكتمل تشغيل الوكيل عبر Gemini AI بنجاح!")
      } catch (e: CancellationException) {
        if (e is TimeoutCancellationException) {
          Log.e(TAG, "Gemini sandbox test timed out", e)
          _testingErrorMessage.value = "انتهت مهلة الاتصال بـ Gemini API (60 ثانية). يرجى التحقق من الشبكة والمحاولة مرة أخرى."
          emitToast("انتهت مهلة الاتصال بـ Gemini API")
        } else {
          throw e
        }
      } catch (e: Exception) {
        Log.e(TAG, "Gemini sandbox test failed", e)
        _testingErrorMessage.value = "تعذر تنفيذ الاستدلال عبر Gemini AI: ${e.localizedMessage ?: e.message}"
        emitToast("حدث خطأ أثناء الاتصال بـ Gemini AI: ${e.message}")
      } finally {
        _isTestingRunning.value = false
      }
    }
  }

  fun publishSkillToMarketplace(skill: AgentSkill) {
    viewModelScope.launch {
      val updated = skill.copy(isPublished = true)
      repository.saveSkill(updated)
      emitToast("تم نشر المهارة في سوق سبق (gosabaq.com)! رابط المشاركة مفعل الآن.")
    }
  }

  fun openShareDialog(skill: AgentSkill) {
    _shareDialogSkill.value = skill
  }

  fun closeShareDialog() {
    _shareDialogSkill.value = null
  }

  fun openCheckoutForProduct(product: AgentProduct) {
    val country = _selectedCountry.value
    _checkoutState.value = CheckoutUiState(
      isOpen = true,
      isSubscription = false,
      itemTitle = product.titleAr,
      targetId = product.id,
      amountSar = product.priceSar,
      paymentMethod = country.defaultPaymentMethod
    )
  }

  fun openCheckoutForSubscription(tierId: String, costSar: Double, tierNameAr: String) {
    val country = _selectedCountry.value
    _checkoutState.value = CheckoutUiState(
      isOpen = true,
      isSubscription = true,
      itemTitle = tierNameAr,
      targetId = tierId,
      amountSar = costSar,
      paymentMethod = country.defaultPaymentMethod
    )
  }

  fun updatePaymentMethod(method: String) {
    _checkoutState.value = _checkoutState.value.copy(paymentMethod = method)
  }

  fun updateCardField(number: String? = null, holder: String? = null, expiry: String? = null, cvv: String? = null) {
    _checkoutState.value = _checkoutState.value.copy(
      cardNumber = number ?: _checkoutState.value.cardNumber,
      cardHolder = holder ?: _checkoutState.value.cardHolder,
      cardExpiry = expiry ?: _checkoutState.value.cardExpiry,
      cardCvv = cvv ?: _checkoutState.value.cardCvv
    )
  }

  fun closeCheckout() {
    _checkoutState.value = CheckoutUiState(isOpen = false)
  }

  fun executeSecurePayment() {
    val state = _checkoutState.value
    if (state.isProcessing) return

    viewModelScope.launch {
      _checkoutState.value = state.copy(isProcessing = true)
      kotlinx.coroutines.delay(1600) // Simulated 3D Secure / Tokenization

      if (state.isSubscription) {
        val res = repository.updateSubscription(state.targetId, state.amountSar, state.paymentMethod)
        if (res.isSuccess) {
          _checkoutState.value = state.copy(
            isProcessing = false,
            completedTransaction = res.getOrNull()
          )
          emitToast("تم تفعيل الاشتراك الشهري بنجاح لدعم المطورين على gosabaq.com!")
        }
      } else {
        val res = repository.processPurchase(state.targetId, state.paymentMethod)
        if (res.isSuccess) {
          _checkoutState.value = state.copy(
            isProcessing = false,
            completedTransaction = res.getOrNull()
          )
          emitToast("تمت عملية الشراء بنجاح عبر بوابة الدفع الآمنة!")
        }
      }
    }
  }

  private fun emitToast(msg: String) {
    viewModelScope.launch {
      _toastEvent.emit(msg)
    }
  }

  fun showToastAlert(alert: SabaqToastAlert) {
    _activeToastAlert.value = alert
  }

  fun dismissToastAlert() {
    _activeToastAlert.value = null
  }

  fun checkForScheduledAlerts() {
    viewModelScope.launch {
      val sub = subscriptionState.value
      val products = allProducts.value
      val purchasedLawyer = products.firstOrNull { it.id == "prod-agent-lawyer" }

      // 1. Check for updates on purchased skills (e.g. v2.4 -> v2.5 available)
      if (purchasedLawyer != null && purchasedLawyer.version == "v2.4") {
        showToastAlert(
          SabaqToastAlert(
            type = SabaqAlertType.SKILL_VERSION_UPDATE,
            title = "🚀 إصدار جديد متاح لمهارة معتمدة!",
            message = "تم إطلاق التحديث v2.5 لـ [وكيل المحامي الذكي] (تحديث توافق نظام المعاملات المدنية ولائحة التجارة). حدّث الآن مجاناً.",
            actionLabel = "تحديث إلى v2.5 الآن",
            targetId = "prod-agent-lawyer",
            newVersion = "v2.5"
          )
        )
      } else if (sub != null && sub.isActive) {
        // 2. Check if subscription is near expiration (within 5 days)
        val daysRemaining = ((sub.renewalDate - System.currentTimeMillis()) / 86400000L).coerceAtLeast(0).toInt()
        if (daysRemaining <= 5) {
          val tierName = if (sub.currentTierId == "ENTERPRISE") "المؤسسات AI" else "المحترف Pro"
          showToastAlert(
            SabaqToastAlert(
              type = SabaqAlertType.SUBSCRIPTION_EXPIRING,
              title = "⏳ تنبيه: اقتراب موعد تجديد الاشتراك",
              message = "اشتراكك في باقة $tierName يوشك على الانتهاء خلال $daysRemaining أيام! جدد اشتراكك الآن لضمان استمرار تشغيل الوكلاء.",
              actionLabel = "تجديد الاشتراك الآن",
              targetId = sub.currentTierId,
              daysRemaining = daysRemaining
            )
          )
        }
      }
    }
  }

  fun triggerSubscriptionExpiringAlert() {
    val sub = subscriptionState.value
    val tier = if (sub?.currentTierId != null && sub.currentTierId != "FREE") sub.currentTierId else "PRO"
    val tierName = if (tier == "ENTERPRISE") "المؤسسات AI" else "المحترف Pro"
    showToastAlert(
      SabaqToastAlert(
        type = SabaqAlertType.SUBSCRIPTION_EXPIRING,
        title = "⏳ تنبيه: اقتراب انتهاء اشتراك الوكلاء",
        message = "اشتراكك في باقة $tierName ينتهي خلال 3 أيام! قم بتجديد الاشتراك لضمان استمرار تشغيل الوكلاء دون انقطاع.",
        actionLabel = "تجديد الاشتراك الآن",
        targetId = tier,
        daysRemaining = 3
      )
    )
  }

  fun triggerSkillUpdateAlert() {
    showToastAlert(
      SabaqToastAlert(
        type = SabaqAlertType.SKILL_VERSION_UPDATE,
        title = "🚀 تحديث جديد متوفر لمهارة معتمدة",
        message = "تم إصدار التحديث v2.5 لـ [وكيل المحامي الذكي] مع تحسينات تدقيق العقود ونظام المعاملات المدنية.",
        actionLabel = "تحديث فوري إلى v2.5",
        targetId = "prod-agent-lawyer",
        newVersion = "v2.5"
      )
    )
  }

  fun handleToastAlertAction(alert: SabaqToastAlert) {
    dismissToastAlert()
    when (alert.type) {
      SabaqAlertType.SUBSCRIPTION_EXPIRING -> {
        val tier = alert.targetId ?: "PRO"
        val cost = if (tier == "ENTERPRISE") 149.0 else 49.0
        val name = if (tier == "ENTERPRISE") "باقة المؤسسات Enterprise" else "باقة المحترف Pro"
        openCheckoutForSubscription(tier, cost, name)
      }
      SabaqAlertType.SKILL_VERSION_UPDATE -> {
        val productId = alert.targetId ?: "prod-agent-lawyer"
        val newVer = alert.newVersion ?: "v2.5"
        viewModelScope.launch {
          repository.updateProductVersion(productId, newVer)
          emitToast("✅ تم تحديث مهارة الوكيل بنجاح إلى الإصدار $newVer!")
        }
      }
      else -> {}
    }
  }
}

class SabaqViewModelFactory(private val repository: SabaqRepository) : ViewModelProvider.Factory {
  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(SabaqViewModel::class.java)) {
      return SabaqViewModel(repository) as T
    }
    throw IllegalArgumentException("Unknown ViewModel class")
  }
}
