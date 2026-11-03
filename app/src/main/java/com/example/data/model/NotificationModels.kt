package com.example.data.model

import java.util.UUID

enum class SabaqAlertType {
  SUBSCRIPTION_EXPIRING,
  SKILL_VERSION_UPDATE,
  PAYMENT_COMPLETED,
  GENERAL_INFO
}

data class SabaqToastAlert(
  val id: String = UUID.randomUUID().toString(),
  val type: SabaqAlertType,
  val title: String,
  val message: String,
  val actionLabel: String? = null,
  val targetId: String? = null,
  val metadata: String? = null,
  val daysRemaining: Int? = null,
  val newVersion: String? = null,
  val timestamp: Long = System.currentTimeMillis()
)
