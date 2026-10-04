package com.example.productcatalog.data.repository

import com.example.productcatalog.data.local.dao.CartDao
import com.example.productcatalog.data.mapper.toCartItem
import com.example.productcatalog.data.mapper.toDomain
import com.example.productcatalog.data.mapper.toEntity
import com.example.productcatalog.domain.model.AppError
import com.example.productcatalog.domain.model.Outcome
import com.example.productcatalog.domain.model.Product
import com.example.productcatalog.domain.repository.CartRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(private val dao: CartDao) : CartRepository {
    override fun observeItems() = dao.observeItems().map { rows -> rows.map { it.toDomain() } }
    override suspend fun add(product: Product) = write { dao.add(product.toCartItem().toEntity()) }
    override suspend fun increase(productId: Int) = write { dao.increase(productId) }
    override suspend fun decrease(productId: Int) = write { dao.decrease(productId); true }
    override suspend fun remove(productId: Int) = write { dao.remove(productId); true }

    private suspend fun write(block: suspend () -> Boolean): Outcome<Unit> = try {
        if (block()) Outcome.Success(Unit) else Outcome.Failure(AppError.QuantityLimit)
    } catch (cancelled: CancellationException) {
        throw cancelled
    } catch (_: Exception) {
        Outcome.Failure(AppError.Storage)
    }
}
