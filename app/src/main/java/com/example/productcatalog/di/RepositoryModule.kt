package com.example.productcatalog.di

import com.example.productcatalog.data.repository.CartRepositoryImpl
import com.example.productcatalog.data.repository.ProductRepositoryImpl
import com.example.productcatalog.domain.repository.CartRepository
import com.example.productcatalog.domain.repository.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds @Singleton abstract fun products(impl: ProductRepositoryImpl): ProductRepository
    @Binds @Singleton abstract fun cart(impl: CartRepositoryImpl): CartRepository
}
