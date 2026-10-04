package com.example.productcatalog.di

import com.example.productcatalog.data.remote.api.ProductsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "https://dummyjson.com/"
    @Provides @Singleton
    fun httpClient(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .callTimeout(30, TimeUnit.SECONDS)
        .build()
    @Provides @Singleton
    fun productsApi(client: OkHttpClient): ProductsApi = Retrofit.Builder()
        .baseUrl(BASE_URL).client(client).addConverterFactory(GsonConverterFactory.create())
        .build().create(ProductsApi::class.java)
}
