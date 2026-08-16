package com.uifinance.project291.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uifinance.project291.data.local.entity.Category
import com.uifinance.project291.data.local.entity.CategoryType
import com.uifinance.project291.data.local.entity.CategoryWithChildren
import com.uifinance.project291.data.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class CategoryManagementUiState {
    object Loading : CategoryManagementUiState()
    data class Success(val categories: List<CategoryWithChildren>) : CategoryManagementUiState()
    data class Error(val message: String) : CategoryManagementUiState()
}

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class CategoryManagementViewModel @Inject constructor(
    private val repository: CategoryRepository
) : ViewModel() {

    private val _categoryType = MutableStateFlow(CategoryType.EXPENSE)
    val categoryType: StateFlow<CategoryType> = _categoryType.asStateFlow()

    val uiState: StateFlow<CategoryManagementUiState> = _categoryType
        .flatMapLatest { type ->
            repository.getCategoriesWithChildren(type)
                .map { categories -> CategoryManagementUiState.Success(categories) as CategoryManagementUiState }
                .onStart { emit(CategoryManagementUiState.Loading) }
                .catch { e -> emit(CategoryManagementUiState.Error(e.message ?: "Unknown error")) }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CategoryManagementUiState.Loading
        )

    fun setCategoryType(type: CategoryType) {
        _categoryType.value = type
    }

    fun addCategory(name: String, iconRes: String, colorHex: String, parentId: Long? = null) {
        viewModelScope.launch {
            val category = Category(
                name = name,
                iconRes = iconRes,
                colorHex = colorHex,
                type = _categoryType.value,
                parentId = parentId
            )
            repository.insert(category)
        }
    }

    fun updateCategory(category: Category) {
        viewModelScope.launch {
            repository.update(category)
        }
    }

    fun deleteCategory(category: Category, moveChildrenToOther: Boolean = false) {
        viewModelScope.launch {
            if (category.parentId == null) {
                val state = uiState.value
                if (state is CategoryManagementUiState.Success) {
                    val categoryWithChildren = state.categories.find { it.category.id == category.id }
                    val children = categoryWithChildren?.children ?: emptyList()

                    if (children.isNotEmpty()) {
                        if (moveChildrenToOther) {
                            var otherCategory = state.categories.find {
                                it.category.name.equals("Other", ignoreCase = true) && it.category.parentId == null
                            }?.category

                            if (otherCategory == null) {
                                val newOtherId = repository.insert(Category(
                                    name = "Other",
                                    iconRes = "more_horiz",
                                    colorHex = "#9E9E9E",
                                    type = category.type,
                                    parentId = null
                                ))
                                otherCategory = Category(
                                    id = newOtherId,
                                    name = "Other",
                                    iconRes = "more_horiz",
                                    colorHex = "#9E9E9E",
                                    type = category.type
                                )
                            }

                            children.forEach { child ->
                                repository.update(child.copy(parentId = otherCategory.id))
                            }
                        } else {
                            children.forEach { child ->
                                repository.delete(child)
                            }
                        }
                    }
                }
            }
            repository.delete(category)
        }
    }

    fun reorderCategories(categories: List<Category>) {
        viewModelScope.launch {
            repository.updateSortOrders(categories)
        }
    }
}
