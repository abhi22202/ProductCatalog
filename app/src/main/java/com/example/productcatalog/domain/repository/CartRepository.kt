package com.example.productcatalog.domain.repository

import com.example.productcatalog.domain.model.CartItem
import com.example.productcatalog.domain.model.Outcome
import com.example.productcatalog.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun observeItems(): Flow<List<CartItem>>
    suspend fun add(product: Product): Outcome<Unit>
    suspend fun increase(productId: Int): Outcome<Unit>
    suspend fun decrease(productId: Int): Outcome<Unit>
    suspend fun remove(productId: Int): Outcome<Unit>
}
