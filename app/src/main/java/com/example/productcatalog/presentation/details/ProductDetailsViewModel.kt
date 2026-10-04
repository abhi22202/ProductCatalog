package com.example.productcatalog.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productcatalog.domain.model.AppError
import com.example.productcatalog.domain.model.Outcome
import com.example.productcatalog.domain.model.Product
import com.example.productcatalog.domain.usecase.AddToCartUseCase
import com.example.productcatalog.domain.usecase.GetProductUseCase
import com.example.productcatalog.presentation.components.ContentState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface CartFeedback {
    data object Added : CartFeedback
    data class Failed(val error: AppError) : CartFeedback
}
data class ProductDetailsUiState(
    val content: ContentState<Product> = ContentState.Loading,
    val adding: Boolean = false,
    val feedback: CartFeedback? = null,
)

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    savedState: SavedStateHandle,
    private val getProduct: GetProductUseCase,
    private val addToCart: AddToCartUseCase,
) : ViewModel() {
    private val productId: Int? = savedState["productId"]
    private val _uiState = MutableStateFlow(ProductDetailsUiState())
    val uiState = _uiState.asStateFlow()
    private var loadJob: Job? = null
    init { retry() }

    fun retry() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.update { it.copy(content = ContentState.Loading) }
            val result = productId?.takeIf { it > 0 }?.let { getProduct(it) }
                ?: Outcome.Failure(AppError.NotFound)
            _uiState.update { it.copy(content = when (result) {
                is Outcome.Success -> ContentState.Success(result.value)
                is Outcome.Failure -> ContentState.Error(result.error)
            }) }
        }
    }
    fun add() {
        val state = _uiState.value
        val product = (state.content as? ContentState.Success)?.data ?: return
        if (state.adding || product.stock == 0) return
        _uiState.update { it.copy(adding = true) }
        viewModelScope.launch {
            val result = addToCart(product)
            _uiState.update { it.copy(adding = false, feedback = when (result) {
                is Outcome.Success -> CartFeedback.Added
                is Outcome.Failure -> CartFeedback.Failed(result.error)
            }) }
        }
    }
    fun feedbackShown() { _uiState.update { it.copy(feedback = null) } }
}
