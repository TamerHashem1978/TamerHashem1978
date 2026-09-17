package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.db.AppDatabase
import com.example.data.model.AgentProduct
import com.example.data.model.AgentSkill
import com.example.data.repository.SabaqRepository
import com.example.ui.components.AmiriQuranBackgroundWatermark
import com.example.ui.components.SabaqBottomNavBar
import com.example.ui.components.SabaqTopHeader
import com.example.ui.dialogs.CheckoutDialog
import com.example.ui.dialogs.ProductDetailDialog
import com.example.ui.dialogs.ShareDialog
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.MarketplaceScreen
import com.example.ui.screens.SharePublishScreen
import com.example.ui.screens.SkillBuilderScreen
import com.example.ui.screens.TestSandboxScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.SabaqNavTab
import com.example.ui.viewmodel.SabaqViewModel
import com.example.ui.viewmodel.SabaqViewModelFactory
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    val database = AppDatabase.getDatabase(applicationContext)
    val repository = SabaqRepository(database.sabaqDao())
    val viewModelFactory = SabaqViewModelFactory(repository)

    setContent {
      MyApplicationTheme {
        val viewModel: SabaqViewModel = viewModel(factory = viewModelFactory)
        SabaqApp(viewModel = viewModel)
      }
    }
  }
}

@Composable
fun SabaqApp(viewModel: SabaqViewModel) {
  val context = LocalContext.current
  val snackbarHostState = remember { SnackbarHostState() }

  val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
  val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
  val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
  val filteredProducts by viewModel.filteredProducts.collectAsStateWithLifecycle()
  val allSkills by viewModel.allSkills.collectAsStateWithLifecycle()
  val allTestRuns by viewModel.allTestRuns.collectAsStateWithLifecycle()
  val subscriptionState by viewModel.subscriptionState.collectAsStateWithLifecycle()
  val allTransactions by viewModel.allTransactions.collectAsStateWithLifecycle()

  val selectedProductDetail by viewModel.selectedProductDetail.collectAsStateWithLifecycle()
  val checkoutState by viewModel.checkoutState.collectAsStateWithLifecycle()
  val shareDialogSkill by viewModel.shareDialogSkill.collectAsStateWithLifecycle()

  // Skill Builder Form State
  val builderTitle by viewModel.builderTitle.collectAsStateWithLifecycle()
  val builderDescription by viewModel.builderDescription.collectAsStateWithLifecycle()
  val builderCategory by viewModel.builderCategory.collectAsStateWithLifecycle()
  val builderKeywords by viewModel.builderKeywords.collectAsStateWithLifecycle()
  val builderInstructions by viewModel.builderInstructions.collectAsStateWithLifecycle()
  val builderEnabledTools by viewModel.builderEnabledTools.collectAsStateWithLifecycle()
  val builderInputSchema by viewModel.builderInputSchema.collectAsStateWithLifecycle()
  val builderOutputFormat by viewModel.builderOutputFormat.collectAsStateWithLifecycle()
  val builderPriceType by viewModel.builderPriceType.collectAsStateWithLifecycle()
  val builderPriceSar by viewModel.builderPriceSar.collectAsStateWithLifecycle()

  // Testing Sandbox State
  val testingSelectedSkill by viewModel.testingSelectedSkill.collectAsStateWithLifecycle()
  val testingInputPrompt by viewModel.testingInputPrompt.collectAsStateWithLifecycle()
  val isTestingRunning by viewModel.isTestingRunning.collectAsStateWithLifecycle()
  val lastTestResult by viewModel.lastTestResult.collectAsStateWithLifecycle()

  // Toast listener
  LaunchedEffect(Unit) {
    viewModel.toastEvent.collectLatest { message ->
      Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
      snackbarHostState.showSnackbar(message)
    }
  }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      SabaqTopHeader(
        currentTier = subscriptionState?.currentTierId ?: "FREE",
        onNavigateToDashboard = { viewModel.setTab(SabaqNavTab.DASHBOARD) }
      )
    },
    bottomBar = {
      SabaqBottomNavBar(
        currentTab = currentTab,
        onTabSelected = { viewModel.setTab(it) }
      )
    },
    snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      // خلفية العلامات المائية القرآنية والتشكيلات بخط Amiri Quran
      AmiriQuranBackgroundWatermark()

      Crossfade(targetState = currentTab, label = "ScreenTransition") { tab ->
        when (tab) {
          SabaqNavTab.MARKETPLACE -> {
            MarketplaceScreen(
              products = filteredProducts,
              searchQuery = searchQuery,
              selectedCategory = selectedCategory,
              onSearchQueryChanged = { viewModel.setSearchQuery(it) },
              onCategorySelected = { viewModel.setCategory(it) },
              onProductClicked = { viewModel.selectProductForDetail(it) },
              onTestClicked = { product ->
                // Create a temporary skill representation to test in sandbox
                val tempSkill = AgentSkill(
                  id = "test-" + product.id,
                  title = product.titleAr,
                  description = product.descriptionAr,
                  category = product.categoryLabelAr,
                  triggerKeywords = product.tags,
                  systemInstructions = product.systemPrompt,
                  toolsEnabled = "DOCUMENT_PARSER,LEGAL_CODE_SEARCH,WEB_SEARCH",
                  inputSchema = "{ \"query\": \"String\" }",
                  outputFormat = "Structured Analysis & Report",
                  benchmarkScore = (product.developerRating * 20).toInt(),
                  testCount = product.reviewsCount,
                  isPublished = true,
                  shareSlug = product.id,
                  priceType = "PAID",
                  priceSar = product.priceSar
                )
                viewModel.selectSkillForTesting(tempSkill)
              },
              onBuyClicked = { viewModel.openCheckoutForProduct(it) }
            )
          }

          SabaqNavTab.BUILDER -> {
            SkillBuilderScreen(
              viewModel = viewModel,
              title = builderTitle,
              description = builderDescription,
              category = builderCategory,
              keywords = builderKeywords,
              instructions = builderInstructions,
              enabledTools = builderEnabledTools,
              inputSchema = builderInputSchema,
              outputFormat = builderOutputFormat,
              priceType = builderPriceType,
              priceSar = builderPriceSar
            )
          }

          SabaqNavTab.TESTER -> {
            TestSandboxScreen(
              skills = allSkills,
              selectedSkill = testingSelectedSkill ?: allSkills.firstOrNull(),
              inputPrompt = testingInputPrompt,
              isTestingRunning = isTestingRunning,
              lastResult = lastTestResult,
              testHistory = allTestRuns,
              onSkillSelected = { viewModel.selectSkillForTesting(it) },
              onPromptChanged = { viewModel.testingInputPrompt.value = it },
              onRunTest = { viewModel.runSkillTest() }
            )
          }

          SabaqNavTab.SHARING -> {
            SharePublishScreen(
              skills = allSkills,
              onOpenShareModal = { viewModel.openShareDialog(it) },
              onPublishDirectly = { viewModel.publishSkillToMarketplace(it) },
              onNavigateToBuilder = { viewModel.setTab(SabaqNavTab.BUILDER) }
            )
          }

          SabaqNavTab.DASHBOARD -> {
            DashboardScreen(
              subscriptionState = subscriptionState,
              transactions = allTransactions,
              onSelectSubscription = { tierId, cost, nameAr ->
                viewModel.openCheckoutForSubscription(tierId, cost, nameAr)
              },
              onRequestPayout = {
                Toast.makeText(context, "تم إرسال طلب سحب الأرباح إلى حساب STC Pay المسجل بنجاح!", Toast.LENGTH_LONG).show()
              }
            )
          }
        }
      }

      // Product Details Modal
      if (selectedProductDetail != null) {
        ProductDetailDialog(
          product = selectedProductDetail,
          onDismiss = { viewModel.selectProductForDetail(null) },
          onBuyClicked = { product ->
            viewModel.selectProductForDetail(null)
            viewModel.openCheckoutForProduct(product)
          },
          onTestClicked = { product ->
            viewModel.selectProductForDetail(null)
            val tempSkill = AgentSkill(
              id = "test-" + product.id,
              title = product.titleAr,
              description = product.descriptionAr,
              category = product.categoryLabelAr,
              triggerKeywords = product.tags,
              systemInstructions = product.systemPrompt,
              toolsEnabled = "DOCUMENT_PARSER,WEB_SEARCH",
              inputSchema = "{}",
              outputFormat = "Report",
              benchmarkScore = 96,
              testCount = 10,
              isPublished = true,
              shareSlug = product.id,
              priceType = "PAID",
              priceSar = product.priceSar
            )
            viewModel.selectSkillForTesting(tempSkill)
          }
        )
      }

      // Secure Checkout Sheet / Modal
      if (checkoutState.isOpen) {
        CheckoutDialog(
          state = checkoutState,
          onDismiss = { viewModel.closeCheckout() },
          onSelectPaymentMethod = { viewModel.updatePaymentMethod(it) },
          onUpdateCardField = { num, holder, exp, cvv ->
            viewModel.updateCardField(num, holder, exp, cvv)
          },
          onSubmitPayment = { viewModel.executeSecurePayment() }
        )
      }

      // Share & Export Dialog
      if (shareDialogSkill != null) {
        ShareDialog(
          skill = shareDialogSkill,
          onDismiss = { viewModel.closeShareDialog() },
          onPublishToMarketplace = { viewModel.publishSkillToMarketplace(it) }
        )
      }
    }
  }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  Text(text = "مرحباً بك في منصة سبق $name", modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
  MyApplicationTheme { Greeting("Sabaq") }
}
