package com.example.productcatalog.domain.usecase

import com.example.productcatalog.domain.model.Cart
import com.example.productcatalog.domain.model.Product
import com.example.productcatalog.domain.repository.CartRepository
import kotlinx.coroutines.flow.map

class ObserveCartUseCase(private val repository: CartRepository) {
    operator fun invoke() = repository.observeItems().map(::Cart)
}
class AddToCartUseCase(private val repository: CartRepository) {
    suspend operator fun invoke(product: Product) = repository.add(product)
}
class IncreaseCartQuantityUseCase(private val repository: CartRepository) {
    suspend operator fun invoke(id: Int) = repository.increase(id)
}
class DecreaseCartQuantityUseCase(private val repository: CartRepository) {
    suspend operator fun invoke(id: Int) = repository.decrease(id)
}
class RemoveCartItemUseCase(private val repository: CartRepository) {
    suspend operator fun invoke(id: Int) = repository.remove(id)
}
