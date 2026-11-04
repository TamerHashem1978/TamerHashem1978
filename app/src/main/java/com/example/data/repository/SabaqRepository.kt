package com.example.data.repository

import com.example.data.db.SabaqDao
import com.example.data.model.AgentProduct
import com.example.data.model.AgentSkill
import com.example.data.model.TestRunResult
import com.example.data.model.TransactionRecord
import com.example.data.model.UserSubscriptionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import java.util.UUID
import kotlin.random.Random

class SabaqRepository(private val dao: SabaqDao) {

  val allProducts: Flow<List<AgentProduct>> = dao.getAllProducts()
  val allSkills: Flow<List<AgentSkill>> = dao.getAllSkills()
  val allTestRuns: Flow<List<TestRunResult>> = dao.getAllTestRuns()
  val subscriptionState: Flow<UserSubscriptionState?> = dao.getSubscriptionState()
  val allTransactions: Flow<List<TransactionRecord>> = dao.getAllTransactions()

  suspend fun initializeDefaultDataIfEmpty() = withContext(Dispatchers.IO) {
    val existing = dao.getAllProducts().firstOrNull()
    if (existing.isNullOrEmpty()) {
      dao.insertProducts(getInitialProducts())
      dao.insertSkill(
        AgentSkill(
          id = "skill-legal-sa",
          title = "مستشار العقود والامتثال التجاري السعودي",
          description = "مهارة متقدمة تصيغ العقود التجارية وتتحقق من مطابقتها لنظام المعاملات المدنية ونظام العمل السعودي مع تنبيهات قانونية.",
          category = "Legal & Compliance",
          triggerKeywords = "عقد، صياغة، تدقيق نظامي، وزارة التجارة",
          systemInstructions = "أنت وكيل ذكاء اصطناعي قانوني خبير بالأنظمة السعودية. قم بتحليل العقد وبنوده وتحديد الثغرات والشروط الجزائية بدقة فائقة.",
          toolsEnabled = "DOCUMENT_PARSER,LEGAL_CODE_SEARCH,CLAUSE_VALIDATOR",
          inputSchema = "{ \"contractType\": \"String\", \"parties\": \"Array\", \"jurisdiction\": \"SA\" }",
          outputFormat = "Markdown with Legal Clauses & Risk Analysis",
          benchmarkScore = 98,
          testCount = 42,
          isPublished = true,
          shareSlug = "saudi-legal-advisor-v2",
          priceType = "SUBSCRIPTION",
          priceSar = 0.0
        )
      )
      dao.insertSkill(
        AgentSkill(
          id = "skill-ecom-support",
          title = "أتمتة خدمة عملاء متجر سلة وزد 24/7",
          description = "مهارة ذكاء اصطناعي تتصل بـ API المتجر للرد الفوري على تتبع الشحنات، الإرجاع، واستفسارات المخزون بلهجة سعودية ودية.",
          category = "Customer Support",
          triggerKeywords = "شحنتي، استرجاع، طلب، سلة، زد",
          systemInstructions = "تجاوب بلهجة سعودية مهنية وودودة. استعلم عن رقم الشحنة وحالة الدفع وقدم حلولاً فورية دون انتظار المشرف البشري.",
          toolsEnabled = "ORDER_TRACKER_API,WHATSAPP_CONNECTOR,INVENTORY_LOOKUP",
          inputSchema = "{ \"customerId\": \"String\", \"query\": \"String\", \"platform\": \"Salla/Zid\" }",
          outputFormat = "Interactive WhatsApp Chat Reply + Action Trigger",
          benchmarkScore = 96,
          testCount = 118,
          isPublished = true,
          shareSlug = "ecom-saudi-support-bot",
          priceType = "PAID",
          priceSar = 89.0
        )
      )
      dao.insertSkill(
        AgentSkill(
          id = "skill-fin-vat",
          title = "مدقق ضريبة القيمة المضافة والفواتير ZATCA",
          description = "فحص الفواتير الإلكترونية ومطابقة أرقام ضريبة القيمة المضافة 15% وحساب الإقرار الضريبي ربع السنوي بدقة محاسبية.",
          category = "Finance & Tax",
          triggerKeywords = "فاتورة، زكاة وضريبة، ZATCA، إقرار ضريبي",
          systemInstructions = "استخرج بنود الفاتورة والرمز المشفر QR، وتأكد من الامتثال لمتطلبات هيئة الزكاة والضريبة والجمارك وحساب الإجماليات.",
          toolsEnabled = "QR_DECODER,VAT_CALCULATOR,XML_ZATCA_PARSER",
          inputSchema = "{ \"invoiceData\": \"JSON/PDF\", \"vatNumber\": \"String\" }",
          outputFormat = "JSON Audit Report + Compliance Status",
          benchmarkScore = 99,
          testCount = 65,
          isPublished = true,
          shareSlug = "zatca-vat-auditor-pro",
          priceType = "SUBSCRIPTION",
          priceSar = 0.0
        )
      )

      dao.saveSubscriptionState(
        UserSubscriptionState(
          id = 1,
          currentTierId = "FREE",
          subscribedDate = System.currentTimeMillis(),
          renewalDate = System.currentTimeMillis() + 30L * 86400000L,
          isActive = true,
          supportedDevelopersCount = 4,
          totalSavedSar = 150.0,
          executionsUsedThisMonth = 28
        )
      )

      dao.insertTransaction(
        TransactionRecord(
          id = "tx-init-01",
          type = "SUBSCRIPTION",
          titleAr = "اشتراك تجريبي - منصة سبق gosabaq.com",
          amountSar = 0.0,
          paymentMethod = "MADA",
          timestamp = System.currentTimeMillis() - 86400000L * 5,
          status = "COMPLETED",
          receiptNumber = "SBQ-2026-90412"
        )
      )
    }
  }

  suspend fun saveSkill(skill: AgentSkill) = withContext(Dispatchers.IO) {
    dao.insertSkill(skill)
  }

  suspend fun deleteSkill(skillId: String) = withContext(Dispatchers.IO) {
    dao.deleteSkillById(skillId)
  }

  suspend fun executeSkillTest(
    skill: AgentSkill,
    testInputPrompt: String
  ): TestRunResult = withContext(Dispatchers.IO) {
    val startTime = System.currentTimeMillis()
    // Simulated deep reasoning latency
    delay(1200)

    val toolsList = skill.toolsEnabled.split(",").map { it.trim() }
    val latency = System.currentTimeMillis() - startTime + Random.nextLong(150, 450)
    val tokens = Random.nextInt(280, 720)
    val score = Random.nextInt(92, 100)

    val generatedTrace = buildString {
      appendLine("⚙️ [الخطوة 1]: تحليل معطيات الإدخال وقواعد الوكيل الذكي...")
      appendLine("🔍 [الخطوة 2]: استدعاء الأدوات المفعّلة: [${toolsList.joinToString(", ")}]")
      appendLine("🧠 [الخطوة 3]: تشغيل المنطق الاستدلالي ومطابقة الشروط: ${skill.triggerKeywords}")
      appendLine("✅ [الخطوة 4]: توليد المخرجات وفق النسق المحدد: ${skill.outputFormat}")
      appendLine("🎯 [التقييم الفوري]: نسبة الدقة والامتثال $score% - متوافق مع معايير gosabaq.com")
    }

    val generatedResponse = when {
      skill.category.contains("Legal", ignoreCase = true) || skill.title.contains("عقد") -> {
        """
        📄 تقرير الفحص القانوني لوكيل سبق:
        • الحالة: تمت المراجعة بنجاح وفق الأنظمة واللوائح السعودية.
        • الإدخال المعالج: "$testInputPrompt"
        • البنود المستنتجة: تم التحقق من سلامة الأركان العقدية وتحديد مسؤوليات الأطراف.
        • توصية الوكيل: ينصح بإضافة شرط التحكيم التجاري لدى المركز السعودي للتحكيم التجاري (SCCA).
        """.trimIndent()
      }
      skill.category.contains("Customer", ignoreCase = true) || skill.title.contains("عملاء") -> {
        """
        💬 رد خدمة العملاء الذكي (Sabaq Agent Output):
        "أهلاً بك! تم استلام طلبك بخصوص: $testInputPrompt.
        نظام المستودعات لدينا يُظهر معالجة الطلب في مستودع الرياض، وسيصلك إشعار المندوب مع كود التتبع خلال ساعتين بإذن الله. هل تود إضافة أي استفسار آخر؟"
        """.trimIndent()
      }
      skill.category.contains("Finance", ignoreCase = true) || skill.title.contains("ضريب") -> {
        """
        📊 تقرير التدقيق المالي الذكي:
        • العملية المدخلة: $testInputPrompt
        • ضريبة القيمة المضافة المحتسبة: 15.00%
        • مطابقة اشتراطات هيئة الزكاة والضريبة (ZATCA): 100% متوافق مع الفوترة المرحلة الثانية.
        • كود التوثيق المالي المشفر: UUID-${UUID.randomUUID().toString().take(8).uppercase()}
        """.trimIndent()
      }
      else -> {
        """
        🚀 نتيجة تشغيل مهارة الوكيل [${skill.title}]:
        • المدخلات: "$testInputPrompt"
        • النتيجة المعالجة: تم تنفيذ تسلسل المهام بنجاح عبر البنية التحتية لمنصة سبق (gosabaq.com).
        • الأداء: استجابة فائقة السرعة مع دقة تطابق متطلبات الوكيل.
        """.trimIndent()
      }
    }

    val testResult = TestRunResult(
      skillId = skill.id,
      skillTitle = skill.title,
      inputPrompt = testInputPrompt,
      outputResponse = generatedResponse,
      reasoningTrace = generatedTrace,
      latencyMs = latency,
      tokensUsed = tokens,
      scorePercentage = score,
      status = "SUCCESS"
    )

    dao.insertTestRun(testResult)

    // Update skill test count & benchmark
    val updatedSkill = skill.copy(
      testCount = skill.testCount + 1,
      benchmarkScore = ((skill.benchmarkScore * skill.testCount + score) / (skill.testCount + 1))
    )
    dao.updateSkill(updatedSkill)

    testResult
  }

  suspend fun recordSkillTestRun(
    skill: AgentSkill,
    testResult: TestRunResult
  ): TestRunResult = withContext(Dispatchers.IO) {
    dao.insertTestRun(testResult)
    val updatedSkill = skill.copy(
      testCount = skill.testCount + 1,
      benchmarkScore = ((skill.benchmarkScore * skill.testCount + testResult.scorePercentage) / (skill.testCount + 1))
    )
    dao.updateSkill(updatedSkill)
    testResult
  }

  suspend fun processPurchase(
    productId: String,
    paymentMethod: String
  ): Result<TransactionRecord> = withContext(Dispatchers.IO) {
    val product = dao.getProductById(productId)
      ?: return@withContext Result.failure(Exception("المنتج غير موجود"))

    val receipt = "SBQ-PAY-" + (10000..99999).random()
    val transaction = TransactionRecord(
      id = "tx-" + UUID.randomUUID().toString().take(8),
      type = "PURCHASE",
      titleAr = "شراء وكيل ذكي: ${product.titleAr}",
      amountSar = product.priceSar,
      paymentMethod = paymentMethod,
      timestamp = System.currentTimeMillis(),
      status = "COMPLETED",
      receiptNumber = receipt
    )

    dao.insertTransaction(transaction)
    dao.markProductPurchased(productId)
    Result.success(transaction)
  }

  suspend fun updateSubscription(
    newTierId: String,
    monthlyCost: Double,
    paymentMethod: String
  ): Result<TransactionRecord> = withContext(Dispatchers.IO) {
    val receipt = "SBQ-SUB-" + (10000..99999).random()
    val tierName = when (newTierId) {
      "PRO" -> "باقة المحترف (Pro Creator)"
      "ENTERPRISE" -> "باقة المؤسسات ومطوري الوكلاء (Enterprise AI)"
      else -> "باقة المستكشف (Starter)"
    }

    val transaction = TransactionRecord(
      id = "tx-" + UUID.randomUUID().toString().take(8),
      type = "SUBSCRIPTION",
      titleAr = "ترقية اشتراك شهري لدعم المطورين: $tierName",
      amountSar = monthlyCost,
      paymentMethod = paymentMethod,
      timestamp = System.currentTimeMillis(),
      status = "COMPLETED",
      receiptNumber = receipt
    )

    dao.insertTransaction(transaction)

    val current = dao.getSubscriptionState().firstOrNull() ?: UserSubscriptionState()
    dao.saveSubscriptionState(
      current.copy(
        currentTierId = newTierId,
        subscribedDate = System.currentTimeMillis(),
        renewalDate = System.currentTimeMillis() + 30L * 86400000L,
        isActive = true,
        supportedDevelopersCount = if (newTierId == "ENTERPRISE") 24 else if (newTierId == "PRO") 12 else 2,
        totalSavedSar = current.totalSavedSar + (monthlyCost * 0.4)
      )
    )

    Result.success(transaction)
  }

  suspend fun updateProductVersion(productId: String, newVersion: String) = withContext(Dispatchers.IO) {
    dao.updateProductVersion(productId, newVersion)
  }

  suspend fun simulateSubscriptionExpiringSoon() = withContext(Dispatchers.IO) {
    val twoDaysFromNow = System.currentTimeMillis() + (2L * 86400000L)
    dao.updateSubscriptionRenewalDate(twoDaysFromNow)
  }

  private fun getInitialProducts(): List<AgentProduct> {
    return listOf(
      AgentProduct(
        id = "prod-agent-lawyer",
        titleAr = "وكيل المحامي الذكي لصياغة العقود التجارية",
        titleEn = "Saudi Commercial Legal Agent Pro",
        descriptionAr = "وكيل ذكاء اصطناعي متخصص في صياغة وتدقيق العقود والاتفاقيات وفق الأنظمة التجارية السعودية ولوائح وزارة التجارة وهيئة الزكاة.",
        descriptionEn = "Autonomous legal agent specialized in drafting & auditing commercial contracts under Saudi regulations.",
        category = "Legal",
        categoryLabelAr = "قانون وامتثال",
        developerName = "استوديو مدار القانوني",
        developerRating = 4.9,
        reviewsCount = 184,
        executionsCount = 14200,
        priceSar = 199.0,
        isSubscriptionIncluded = true,
        isFeatured = true,
        capabilities = "صياغة عقود، تدقيق نظامي، كشف الثغرات القانونية، توافق مع نظام المعاملات المدنية",
        systemPrompt = "أنت مستشار قانوني رقمي متخصص بالأنظمة التجارية في المملكة العربية السعودية.",
        sampleInput = "أريد صياغة عقد تقديم خدمات برمجية بين شركتين سعوديتين مع شرط جزائي وحفظ حقوق الملكية الفكرية.",
        sampleOutput = "تم توليد مسودة العقد المكونة من 14 بنداً تشمل نطاق العمل، الدفعات، التراخيص، والسرية.",
        tags = "قانون، عقود، شركات، استشارات",
        version = "v2.4",
        isPurchased = false
      ),
      AgentProduct(
        id = "prod-agent-salla-ecom",
        titleAr = "وكيل مبيعات ودعم عملاء منصات سلة وزد وShopify",
        titleEn = "Omnichannel E-commerce Sales Agent",
        descriptionAr = "وكيل ذكاء اصطناعي متكامل يربط متجرك بواتساب وتليجرام للرد على العملاء، اقتراح المنتجات البديلة، وإتمام عمليات الشراء آلياً.",
        descriptionEn = "Autonomous conversational sales agent integrated with WhatsApp & Saudi ecommerce platforms.",
        category = "CustomerSupport",
        categoryLabelAr = "خدمة العملاء والمبيعات",
        developerName = "فريق نماء للذكاء الاصطناعي",
        developerRating = 4.8,
        reviewsCount = 312,
        executionsCount = 89000,
        priceSar = 149.0,
        isSubscriptionIncluded = true,
        isFeatured = true,
        capabilities = "تكامل واتساب، ربط مخزون سلة وزد، اقتراح مبيعات إضافية Up-selling، معالجة شكاوى العملاء",
        systemPrompt = "أنت مسؤول المبيعات وخدمة العملاء الذكي للمتجر الإلكتروني، تحدث بأسلوب ترحيبي وجذاب.",
        sampleInput = "العميل يسأل: هل مقاس L متوفر من الحذاء الرياضي ومتى يوصل للدمام؟",
        sampleOutput = "مرحباً! نعم متوفر مقاس L بلونين، والتوصيل للدمام خلال 48 ساعة مع شحن مجاني!",
        tags = "تجارة إلكترونية، مبيعات، واتساب، سلة",
        version = "v3.1",
        isPurchased = false
      ),
      AgentProduct(
        id = "prod-agent-financial-cfo",
        titleAr = "وكيل المدير المالي الافتراضي (Virtual CFO Agent)",
        titleEn = "Autonomous Virtual CFO & Tax Agent",
        descriptionAr = "تحليل القوائم المالية، التدفقات النقدية، التدقيق الضريبي، ومحاكاة الميزانيات التقديرية مع تقارير لوحة القيادة التنفيذية.",
        descriptionEn = "Comprehensive financial intelligence agent analyzing cash flows, budgets, and tax optimization.",
        category = "Finance",
        categoryLabelAr = "المالية والضرائب",
        developerName = "حلول مسار المالية",
        developerRating = 4.95,
        reviewsCount = 98,
        executionsCount = 6700,
        priceSar = 249.0,
        isSubscriptionIncluded = false,
        isFeatured = true,
        capabilities = "تحليل التدفق النقدي، فحص القوائم المالية، محاكاة السيناريوهات الاقتصادية، تقارير PDF تنفيذية",
        systemPrompt = "أنت خبير مالي معتمد ومدير مالي تنفيذي (CFO) للشركات الناشئة والمتوسطة.",
        sampleInput = "حلل هامش الربح التشغيلي لشركتنا في الربع الماضي وقارنه بقطاع التقنية المالية.",
        sampleOutput = "هامش الربح التشغيلي الحالي 22% وهو أعلى من متوسط القطاع بنسبة 3.4% مع استقرار رأس المال العامل.",
        tags = "مالية، ضرائب، استثمار، تخطيط مالي",
        version = "v1.8",
        isPurchased = false
      ),
      AgentProduct(
        id = "prod-agent-seo-arabic",
        titleAr = "وكيل صناعة المحتوى وSEO للأسواق الخليجية",
        titleEn = "Arabic SEO & Content Engine Agent",
        descriptionAr = "وكيل ذكي يولد مقالات متصدرة في Google ومحتوى إعلاني مخصص للجمهور العربي مع تضمين الكلمات المفتاحية الأكثر بحثاً.",
        descriptionEn = "Engineered AI agent creating high-ranking Arabic SEO articles, copy, and localized social campaigns.",
        category = "Marketing",
        categoryLabelAr = "التسويق والمحتوى",
        developerName = "بصمة ديجيتال",
        developerRating = 4.7,
        reviewsCount = 205,
        executionsCount = 34500,
        priceSar = 99.0,
        isSubscriptionIncluded = true,
        isFeatured = false,
        capabilities = "بحث كلمات مفتاحية خليجية، صياغة مقالات متوافقة مع Google، استخراج وسوم تيك توك وتويتر",
        systemPrompt = "أنت مسوق محتوى وخبير تحسين محركات بحث عربي محترف.",
        sampleInput = "اكتب مقالاً متوافقاً مع السيو بعنوان 'أفضل حلول الطاقة الشمسية في الرياض 2026'.",
        sampleOutput = "تم إنشاء مقال بطول 1200 كلمة مع العناوين الفرعية H2 وH3 وروابط داخلية مقترحة وميتا ديسكريبشن.",
        tags = "تسويق، محتوى، سيو، إعلانات",
        version = "v2.0",
        isPurchased = false
      ),
      AgentProduct(
        id = "prod-agent-code-reviewer",
        titleAr = "وكيل هندسة البرمجيات وفحص الثغرات الأمنية",
        titleEn = "Autonomous Code Auditor & Security Agent",
        descriptionAr = "فحص الشيفرات البرمجية واكتشاف الثغرات الأمنية (OWASP) واقتراح إعادة الهيكلة والتوليد الآلي للاختبارات الوحدوية.",
        descriptionEn = "Code review & vulnerability scanner agent generating unit tests and security fixes.",
        category = "Development",
        categoryLabelAr = "البرمجة والأمن السيبراني",
        developerName = "مختبر كود سيكيور",
        developerRating = 4.9,
        reviewsCount = 140,
        executionsCount = 21000,
        priceSar = 179.0,
        isSubscriptionIncluded = true,
        isFeatured = false,
        capabilities = "فحص أمني، مراجعة Kotlin/Python/TypeScript، توليد اختبارات، تحليل الأداء والذاكرة",
        systemPrompt = "أنت مهندس أمن سيبراني ومعماري برمجيات أول.",
        sampleInput = "فحص كود استعلام قاعدة بيانات والتحقق من عدم وجود ثغرات SQL Injection.",
        sampleOutput = "تم رصد استعلام مباشر غير محمي، وتم استبداله بـ Parameterized Query الآمن مع إضافة اختبار.",
        tags = "برمجة، أمان، كود، اختبارات",
        version = "v1.5",
        isPurchased = false
      )
    )
  }
}
