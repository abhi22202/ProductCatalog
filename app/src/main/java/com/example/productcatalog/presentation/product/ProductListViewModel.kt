package com.example.productcatalog.presentation.product

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productcatalog.domain.model.Outcome
import com.example.productcatalog.domain.model.Product
import com.example.productcatalog.domain.usecase.GetProductsUseCase
import com.example.productcatalog.domain.usecase.SearchProductsUseCase
import com.example.productcatalog.presentation.components.ContentState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProductListUiState(
    val query: String = "",
    val content: ContentState<List<Product>> = ContentState.Loading,
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val getProducts: GetProductsUseCase,
    private val searchProducts: SearchProductsUseCase,
    private val savedState: SavedStateHandle,
) : ViewModel() {
    private val query = savedState.getStateFlow(QUERY, "")
    private val retryVersion = MutableStateFlow(0)
    private val _uiState = MutableStateFlow(ProductListUiState(query = query.value))
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(query.map(String::trim).distinctUntilChanged(), retryVersion) { text, retry -> text to retry }
                .flatMapLatest { (text, _) ->
                    flow {
                        emit(ContentState.Loading)
                        // Switch immediately, then debounce inside the cancellable branch. An old
                        // request cannot publish during the new query's debounce window.
                        if (text.isNotEmpty()) delay(SEARCH_DEBOUNCE_MS)
                        val result = if (text.isEmpty()) getProducts() else searchProducts(text)
                        emit(when (result) {
                            is Outcome.Success -> if (result.value.isEmpty()) ContentState.Empty
                                else ContentState.Success(result.value)
                            is Outcome.Failure -> ContentState.Error(result.error)
                        })
                    }
                }.collect { content -> _uiState.update { it.copy(content = content) } }
        }
    }

    fun onQueryChanged(text: String) {
        _uiState.update { it.copy(query = text, content = if (text.trim() == it.query.trim())
            it.content else ContentState.Loading) }
        savedState[QUERY] = text
    }
    fun retry() { retryVersion.update { it + 1 } }

    companion object {
        const val SEARCH_DEBOUNCE_MS = 350L
        private const val QUERY = "searchQuery"
    }
}
