package com.example.productcatalog.data.repository

import com.example.productcatalog.data.mapper.toDomain
import com.example.productcatalog.data.remote.api.ProductsApi
import com.example.productcatalog.domain.model.AppError
import com.example.productcatalog.domain.model.Outcome
import com.example.productcatalog.domain.repository.ProductRepository
import com.google.gson.JsonParseException
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(private val api: ProductsApi) : ProductRepository {
    override suspend fun getProducts() = request { requireNotNull(api.products().products).map { it.toDomain() } }
    override suspend fun searchProducts(query: String) = request {
        requireNotNull(api.search(query).products).map { it.toDomain() }
    }
    override suspend fun getProduct(id: Int) = request { api.product(id).toDomain().also { require(it.id == id) } }

    private suspend fun <T> request(block: suspend () -> T): Outcome<T> = try {
        Outcome.Success(block())
    } catch (cancelled: CancellationException) {
        throw cancelled
    } catch (error: Exception) {
        Outcome.Failure(when (error) {
            is SocketTimeoutException -> AppError.Timeout
            is HttpException -> when (error.code()) {
                404 -> AppError.NotFound
                in 500..599 -> AppError.Server
                in 400..499 -> AppError.RequestRejected
                else -> AppError.InvalidResponse
            }
            is JsonParseException, is IllegalArgumentException, is ArithmeticException -> AppError.InvalidResponse
            is IOException -> AppError.NoInternet
            else -> AppError.Unknown
        })
    }
}
