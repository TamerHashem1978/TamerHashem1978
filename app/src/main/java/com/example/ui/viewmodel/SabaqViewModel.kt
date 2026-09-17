package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.model.AgentProduct
import com.example.data.model.AgentSkill
import com.example.data.model.TestRunResult
import com.example.data.model.TransactionRecord
import com.example.data.model.UserSubscriptionState
import com.example.data.repository.SabaqRepository
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
import java.util.UUID

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

  // Testing Sandbox State
  private val _testingSelectedSkill = MutableStateFlow<AgentSkill?>(null)
  val testingSelectedSkill: StateFlow<AgentSkill?> = _testingSelectedSkill.asStateFlow()

  val testingInputPrompt = MutableStateFlow("هل يمكن للوكيل فحص مطابقة العقد التجاري للبند العاشر وتحديد المسؤوليات؟")

  private val _isTestingRunning = MutableStateFlow(false)
  val isTestingRunning: StateFlow<Boolean> = _isTestingRunning.asStateFlow()

  private val _lastTestResult = MutableStateFlow<TestRunResult?>(null)
  val lastTestResult: StateFlow<TestRunResult?> = _lastTestResult.asStateFlow()

  // Checkout & Payment State
  private val _checkoutState = MutableStateFlow(CheckoutUiState())
  val checkoutState: StateFlow<CheckoutUiState> = _checkoutState.asStateFlow()

  // Share Dialog State
  private val _shareDialogSkill = MutableStateFlow<AgentSkill?>(null)
  val shareDialogSkill: StateFlow<AgentSkill?> = _shareDialogSkill.asStateFlow()

  private val _toastEvent = MutableSharedFlow<String>()
  val toastEvent: SharedFlow<String> = _toastEvent.asSharedFlow()

  init {
    viewModelScope.launch {
      repository.initializeDefaultDataIfEmpty()
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
    if (testingInputPrompt.value.isBlank()) {
      testingInputPrompt.value = "اختبار تشغيلي لوكيل: ${skill.title}"
    }
    _currentTab.value = SabaqNavTab.TESTER
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
      try {
        val result = repository.executeSkillTest(skill, prompt)
        _lastTestResult.value = result
        emitToast("اكتمل الاختبار بنجاح! تم تسجيل التقييم في gosabaq.com")
      } catch (e: Exception) {
        emitToast("حدث خطأ أثناء فحص المهارة: ${e.message}")
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
    _checkoutState.value = CheckoutUiState(
      isOpen = true,
      isSubscription = false,
      itemTitle = product.titleAr,
      targetId = product.id,
      amountSar = product.priceSar,
      paymentMethod = "MADA"
    )
  }

  fun openCheckoutForSubscription(tierId: String, costSar: Double, tierNameAr: String) {
    _checkoutState.value = CheckoutUiState(
      isOpen = true,
      isSubscription = true,
      itemTitle = tierNameAr,
      targetId = tierId,
      amountSar = costSar,
      paymentMethod = "APPLE_PAY"
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
