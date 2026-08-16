package com.uifinance.project291.data.local.dao

import androidx.room.*
import com.uifinance.project291.data.local.entity.Category
import com.uifinance.project291.data.local.entity.CategoryType
import com.uifinance.project291.data.local.entity.CategoryWithChildren
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories WHERE type = :type ORDER BY sortOrder ASC")
    fun getCategoriesByType(type: CategoryType): Flow<List<Category>>

    @Transaction
    @Query("SELECT * FROM categories WHERE type = :type AND parentId IS NULL ORDER BY sortOrder ASC")
    fun getCategoriesWithChildren(type: CategoryType): Flow<List<CategoryWithChildren>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: Category): Long

    @Update
    suspend fun update(category: Category)

    @Delete
    suspend fun delete(category: Category)

    @Update
    suspend fun updateSortOrders(categories: List<Category>)

    @Query("SELECT COUNT(*) FROM categories")
    suspend fun getCategoryCount(): Int
}
