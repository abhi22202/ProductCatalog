package com.example.productcatalog.presentation.navigation

object Routes {
    const val PRODUCTS = "products"
    const val CART = "cart"
    const val PRODUCT_ID = "productId"
    const val DETAILS = "product/{productId}"
    fun details(id: Int): String = "product/$id"
}
