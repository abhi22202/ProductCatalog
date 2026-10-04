package com.example.productcatalog.data.remote.dto

import java.math.BigDecimal


data class ProductDto(
    val id: Int?, val title: String?, val description: String?, val price: BigDecimal?,
    val rating: Double?, val category: String?, val brand: String?, val stock: Int?,
    val thumbnail: String?, val images: List<String>?,
)
data class ProductsResponseDto(val products: List<ProductDto>?)
