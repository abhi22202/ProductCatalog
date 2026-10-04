package com.example.productcatalog.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productcatalog.domain.model.AppError
import com.example.productcatalog.domain.model.Cart
import com.example.productcatalog.domain.model.Outcome
import com.example.productcatalog.domain.usecase.*
import com.example.productcatalog.presentation.components.ContentState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CartUiState(
    val content: ContentState<Cart> = ContentState.Loading,
    val operationError: AppError? = null,
)

@HiltViewModel
class CartViewModel @Inject constructor(
    private val observeCart: ObserveCartUseCase,
    private val increaseQuantity: IncreaseCartQuantityUseCase,
    private val decreaseQuantity: DecreaseCartQuantityUseCase,
    private val removeItem: RemoveCartItemUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CartUiState())
    val uiState = _uiState.asStateFlow()
    private var observeJob: Job? = null
    init { retry() }

    fun retry() {
        observeJob?.cancel()
        observeJob = viewModelScope.launch {
            _uiState.update { it.copy(content = ContentState.Loading) }
            observeCart().catch { error ->
                if (error is CancellationException) throw error
                _uiState.update { it.copy(content = ContentState.Error(AppError.Storage)) }
            }.collect { cart ->
                _uiState.update { it.copy(content = if (cart.items.isEmpty()) ContentState.Empty
                    else ContentState.Success(cart)) }
            }
        }
    }
    fun increase(id: Int) = mutate { increaseQuantity(id) }
    fun decrease(id: Int) = mutate { decreaseQuantity(id) }
    fun remove(id: Int) = mutate { removeItem(id) }
    private fun mutate(operation: suspend () -> Outcome<Unit>) {
        viewModelScope.launch {
            val result = operation()
            if (result is Outcome.Failure) _uiState.update { it.copy(operationError = result.error) }
        }
    }
    fun errorShown() { _uiState.update { it.copy(operationError = null) } }
}
