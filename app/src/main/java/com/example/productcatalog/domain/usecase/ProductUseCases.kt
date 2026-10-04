package com.example.productcatalog.domain.usecase

import com.example.productcatalog.domain.repository.ProductRepository

class GetProductsUseCase(private val repository: ProductRepository) {
    suspend operator fun invoke() = repository.getProducts()
}
class SearchProductsUseCase(private val repository: ProductRepository) {
    suspend operator fun invoke(query: String) = if (query.isBlank()) repository.getProducts()
        else repository.searchProducts(query.trim())
}
class GetProductUseCase(private val repository: ProductRepository) {
    suspend operator fun invoke(id: Int) = repository.getProduct(id)
}
