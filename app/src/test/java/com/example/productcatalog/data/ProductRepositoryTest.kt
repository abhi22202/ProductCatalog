package com.example.productcatalog.data

import com.example.productcatalog.data.remote.api.ProductsApi
import com.example.productcatalog.data.remote.dto.ProductDto
import com.example.productcatalog.data.remote.dto.ProductsResponseDto
import com.example.productcatalog.data.repository.ProductRepositoryImpl
import com.example.productcatalog.domain.model.AppError
import com.example.productcatalog.domain.model.Outcome
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.HttpException
import java.io.IOException
import java.math.BigDecimal

class ProductRepositoryTest {
    @Test fun mapsSuccessfulResponseIntoDomainModel() = runBlocking {
        val result = ProductRepositoryImpl(FakeApi { response = ProductsResponseDto(listOf(dto())) }).getProducts()
        assertTrue(result is Outcome.Success)
        assertEquals(1235L, (result as Outcome.Success).value.single().price.cents)
    }

    @Test fun emptyResponseIsSuccessfulEmptyList() = runBlocking {
        val result = ProductRepositoryImpl(FakeApi { response = ProductsResponseDto(emptyList()) }).getProducts()
        assertEquals(emptyList<Any>(), (result as Outcome.Success).value)
    }

    @Test fun malformedResponseIsInvalidResponse() = runBlocking {
        val result = ProductRepositoryImpl(FakeApi { response = ProductsResponseDto(null) }).getProducts()
        assertEquals(AppError.InvalidResponse, (result as Outcome.Failure).error)
    }

    @Test fun ioFailureMapsToNoInternet() = runBlocking {
        val result = ProductRepositoryImpl(FakeApi { failure = IOException() }).getProducts()
        assertEquals(AppError.NoInternet, (result as Outcome.Failure).error)
    }

    private class FakeApi(configure: FakeApi.() -> Unit) : ProductsApi {
        var response = ProductsResponseDto(emptyList())
        var failure: Exception? = null
        init { configure() }
        private fun checkFailure() { failure?.let { throw it } }
        override suspend fun products(limit: Int): ProductsResponseDto { checkFailure(); return response }
        override suspend fun search(query: String, limit: Int): ProductsResponseDto { checkFailure(); return response }
        override suspend fun product(id: Int): ProductDto { checkFailure(); return dto() }
    }

    companion object {
        private fun dto() = ProductDto(1, "Item", "Description", BigDecimal("12.345"), 4.5,
            "category", "brand", 4, "image", listOf("image"))
    }
}
