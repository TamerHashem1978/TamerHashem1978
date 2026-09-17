package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "agent_products")
data class AgentProduct(
  @PrimaryKey val id: String,
  val titleAr: String,
  val titleEn: String,
  val descriptionAr: String,
  val descriptionEn: String,
  val category: String,
  val categoryLabelAr: String,
  val developerName: String,
  val developerRating: Double,
  val reviewsCount: Int,
  val executionsCount: Long,
  val priceSar: Double,
  val isSubscriptionIncluded: Boolean,
  val isFeatured: Boolean,
  val capabilities: String,
  val systemPrompt: String,
  val sampleInput: String,
  val sampleOutput: String,
  val tags: String,
  val version: String,
  val isPurchased: Boolean = false
)

@Entity(tableName = "agent_skills")
data class AgentSkill(
  @PrimaryKey val id: String,
  val title: String,
  val description: String,
  val category: String,
  val triggerKeywords: String,
  val systemInstructions: String,
  val toolsEnabled: String,
  val inputSchema: String,
  val outputFormat: String,
  val benchmarkScore: Int,
  val testCount: Int,
  val isPublished: Boolean,
  val shareSlug: String,
  val priceType: String, // FREE, PAID, SUBSCRIPTION
  val priceSar: Double,
  val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "test_run_results")
data class TestRunResult(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val skillId: String,
  val skillTitle: String,
  val inputPrompt: String,
  val outputResponse: String,
  val reasoningTrace: String,
  val latencyMs: Long,
  val tokensUsed: Int,
  val scorePercentage: Int,
  val status: String, // SUCCESS, WARNING, ERROR
  val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_subscription")
data class UserSubscriptionState(
  @PrimaryKey val id: Int = 1,
  val currentTierId: String = "FREE", // FREE, PRO, ENTERPRISE
  val subscribedDate: Long = System.currentTimeMillis(),
  val renewalDate: Long = System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000,
  val isActive: Boolean = false,
  val supportedDevelopersCount: Int = 0,
  val totalSavedSar: Double = 0.0,
  val executionsUsedThisMonth: Int = 14
)

@Entity(tableName = "transactions")
data class TransactionRecord(
  @PrimaryKey val id: String,
  val type: String, // PURCHASE, SUBSCRIPTION, PAYOUT
  val titleAr: String,
  val amountSar: Double,
  val paymentMethod: String, // MADA, APPLE_PAY, STC_PAY, VISA
  val timestamp: Long = System.currentTimeMillis(),
  val status: String = "COMPLETED",
  val receiptNumber: String
)
