package com.uifinance.project291.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class CategoryWithChildren(
    @Embedded val category: Category,
    @Relation(
        parentColumn = "id",
        entityColumn = "parentId"
    )
    val children: List<Category>
)
