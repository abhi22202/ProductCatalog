package com.example.productcatalog.di

import com.example.productcatalog.domain.repository.CartRepository
import com.example.productcatalog.domain.repository.ProductRepository
import com.example.productcatalog.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides fun getProducts(repository: ProductRepository) = GetProductsUseCase(repository)
    @Provides fun searchProducts(repository: ProductRepository) = SearchProductsUseCase(repository)
    @Provides fun getProduct(repository: ProductRepository) = GetProductUseCase(repository)
    @Provides fun observeCart(repository: CartRepository) = ObserveCartUseCase(repository)
    @Provides fun addToCart(repository: CartRepository) = AddToCartUseCase(repository)
    @Provides fun increase(repository: CartRepository) = IncreaseCartQuantityUseCase(repository)
    @Provides fun decrease(repository: CartRepository) = DecreaseCartQuantityUseCase(repository)
    @Provides fun remove(repository: CartRepository) = RemoveCartItemUseCase(repository)
}
