package com.example.productcatalog.data.mapper

import com.example.productcatalog.data.remote.dto.ProductDto
import com.example.productcatalog.domain.model.Money
import com.example.productcatalog.domain.model.Product

fun ProductDto.toDomain(): Product {
    val validId = requireNotNull(id).also { require(it > 0) }
    val validTitle = requireNotNull(title).also { require(it.isNotBlank()) }
    val validPrice = requireNotNull(price).also { require(it.signum() >= 0) }
    val validRating = requireNotNull(rating).also { require(it.isFinite() && it in 0.0..5.0) }
    val validStock = requireNotNull(stock).also { require(it >= 0) }
    return Product(validId, validTitle, requireNotNull(description), Money.fromDecimal(validPrice),
        validRating, requireNotNull(category), brand, validStock, thumbnail.orEmpty(), images.orEmpty())
}
