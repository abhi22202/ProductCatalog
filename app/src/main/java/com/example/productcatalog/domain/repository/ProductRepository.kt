package com.example.productcatalog.domain.repository

import com.example.productcatalog.domain.model.Outcome
import com.example.productcatalog.domain.model.Product

interface ProductRepository {
    suspend fun getProducts(): Outcome<List<Product>>
    suspend fun searchProducts(query: String): Outcome<List<Product>>
    suspend fun getProduct(id: Int): Outcome<Product>
}
