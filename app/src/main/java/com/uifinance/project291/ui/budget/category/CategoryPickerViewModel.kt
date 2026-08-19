package com.uifinance.project291.ui.budget.category

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

sealed class CategoryPickerUiState {
    object Loading : CategoryPickerUiState()
    data class Success(val categories: List<CategoryWithChildren>) : CategoryPickerUiState()
    data class Error(val message: String) : CategoryPickerUiState()
}

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class CategoryPickerViewModel @Inject constructor(
    private val repository: CategoryRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            repository.seedDefaultsIfNeeded()
        }
    }

    private val _categoryType = MutableStateFlow(CategoryType.EXPENSE)
    val categoryType: StateFlow<CategoryType> = _categoryType.asStateFlow()

    private val _selectedParentCategory = MutableStateFlow<Category?>(null)
    val selectedParentCategory: StateFlow<Category?> = _selectedParentCategory.asStateFlow()

    private val _selectedChildCategory = MutableStateFlow<Category?>(null)
    val selectedChildCategory: StateFlow<Category?> = _selectedChildCategory.asStateFlow()

    private val _categorySelectedEvent = MutableSharedFlow<Category>()
    val categorySelectedEvent: SharedFlow<Category> = _categorySelectedEvent.asSharedFlow()

    val uiState: StateFlow<CategoryPickerUiState> = _categoryType
        .flatMapLatest { type ->
            repository.getCategoriesWithChildren(type)
                .map { categories -> CategoryPickerUiState.Success(categories) as CategoryPickerUiState }
                .onStart { emit(CategoryPickerUiState.Loading) }
                .catch { e -> emit(CategoryPickerUiState.Error(e.message ?: "Unknown error")) }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CategoryPickerUiState.Loading
        )

    fun setCategoryType(type: CategoryType) {
        if (_categoryType.value != type) {
            _categoryType.value = type
            _selectedParentCategory.value = null
            _selectedChildCategory.value = null
        }
    }

    fun selectParentCategory(category: Category?) {
        _selectedParentCategory.value = category
        _selectedChildCategory.value = null
    }

    fun selectChildCategory(category: Category?) {
        _selectedChildCategory.value = category
        category?.let {
            viewModelScope.launch {
                _categorySelectedEvent.emit(it)
            }
        }
    }

    fun selectCategory(category: Category) {
        viewModelScope.launch {
            _categorySelectedEvent.emit(category)
        }
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
}
