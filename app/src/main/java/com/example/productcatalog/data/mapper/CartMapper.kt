package com.example.productcatalog.data.mapper

import com.example.productcatalog.data.local.entity.CartItemEntity
import com.example.productcatalog.domain.model.CartItem
import com.example.productcatalog.domain.model.Money
import com.example.productcatalog.domain.model.Product

fun CartItemEntity.toDomain() = CartItem(productId, name, Money(priceCents), imageUrl, quantity)
fun CartItem.toEntity() = CartItemEntity(productId, name, price.cents, imageUrl, quantity)
fun Product.toCartItem() = CartItem(id, name, price, thumbnail, 1)
