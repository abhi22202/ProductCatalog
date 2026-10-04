package com.example.productcatalog.data.remote.api

import com.example.productcatalog.data.remote.dto.ProductDto
import com.example.productcatalog.data.remote.dto.ProductsResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductsApi {
    @GET("products")
    suspend fun products(@Query("limit") limit: Int = 0): ProductsResponseDto
    @GET("products/search")
    suspend fun search(@Query("q") query: String, @Query("limit") limit: Int = 0): ProductsResponseDto
    @GET("products/{id}")
    suspend fun product(@Path("id") id: Int): ProductDto
}
