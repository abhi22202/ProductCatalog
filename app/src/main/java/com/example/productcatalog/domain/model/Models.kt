package com.example.productcatalog.domain.model

import java.math.BigDecimal
import java.math.RoundingMode

/** USD minor units: round once at the API boundary, then calculate exactly. */
@JvmInline
value class Money(val cents: Long) {
    init { require(cents >= 0) }
    companion object {
        fun fromDecimal(value: BigDecimal): Money = Money(
            value.setScale(2, RoundingMode.HALF_UP).movePointRight(2).longValueExact()
        )
    }
}

data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val price: Money,
    val rating: Double,
    val category: String,
    val brand: String?,
    val stock: Int,
    val thumbnail: String,
    val images: List<String>,
)

data class CartItem(
    val productId: Int,
    val name: String,
    val price: Money,
    val imageUrl: String,
    val quantity: Int,
) {
    init { require(quantity in 1..MAX_QUANTITY) }
    val subtotal: Money get() = Money(Math.multiplyExact(price.cents, quantity.toLong()))
    companion object { const val MAX_QUANTITY = 999 }
}

data class Cart(val items: List<CartItem>) {
    val totalItems: Int = items.sumOf { it.quantity }
    val totalPrice: Money = Money(items.fold(0L) { total, item -> Math.addExact(total, item.subtotal.cents) })
}

enum class AppError { NoInternet, Timeout, Server, NotFound, RequestRejected, InvalidResponse, Storage, QuantityLimit, Unknown }

sealed interface Outcome<out T> {
    data class Success<T>(val value: T) : Outcome<T>
    data class Failure(val error: AppError) : Outcome<Nothing>
}
