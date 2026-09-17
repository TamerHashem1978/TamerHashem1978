package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.AgentProduct
import com.example.data.model.AgentSkill
import com.example.data.model.TestRunResult
import com.example.data.model.TransactionRecord
import com.example.data.model.UserSubscriptionState
import kotlinx.coroutines.flow.Flow

@Dao
interface SabaqDao {

  // Agent Products
  @Query("SELECT * FROM agent_products ORDER BY isFeatured DESC, executionsCount DESC")
  fun getAllProducts(): Flow<List<AgentProduct>>

  @Query("SELECT * FROM agent_products WHERE id = :productId LIMIT 1")
  suspend fun getProductById(productId: String): AgentProduct?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertProducts(products: List<AgentProduct>)

  @Update
  suspend fun updateProduct(product: AgentProduct)

  @Query("UPDATE agent_products SET isPurchased = 1 WHERE id = :productId")
  suspend fun markProductPurchased(productId: String)

  // Agent Skills (Builder & Tested skills)
  @Query("SELECT * FROM agent_skills ORDER BY createdAt DESC")
  fun getAllSkills(): Flow<List<AgentSkill>>

  @Query("SELECT * FROM agent_skills WHERE id = :id LIMIT 1")
  suspend fun getSkillById(id: String): AgentSkill?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertSkill(skill: AgentSkill)

  @Update
  suspend fun updateSkill(skill: AgentSkill)

  @Query("DELETE FROM agent_skills WHERE id = :id")
  suspend fun deleteSkillById(id: String)

  // Test Run Results
  @Query("SELECT * FROM test_run_results ORDER BY timestamp DESC")
  fun getAllTestRuns(): Flow<List<TestRunResult>>

  @Query("SELECT * FROM test_run_results WHERE skillId = :skillId ORDER BY timestamp DESC")
  fun getTestRunsForSkill(skillId: String): Flow<List<TestRunResult>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertTestRun(testRun: TestRunResult)

  // User Subscription
  @Query("SELECT * FROM user_subscription WHERE id = 1 LIMIT 1")
  fun getSubscriptionState(): Flow<UserSubscriptionState?>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun saveSubscriptionState(state: UserSubscriptionState)

  // Transactions
  @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
  fun getAllTransactions(): Flow<List<TransactionRecord>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertTransaction(transaction: TransactionRecord)
}
