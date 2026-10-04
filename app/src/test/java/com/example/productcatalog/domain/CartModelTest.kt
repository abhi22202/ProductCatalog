package com.example.productcatalog.domain

import com.example.productcatalog.domain.model.Cart
import com.example.productcatalog.domain.model.CartItem
import com.example.productcatalog.domain.model.Money
import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal

class CartModelTest {
    @Test fun moneyRoundsAtCentBoundary() {
        assertEquals(1235L, Money.fromDecimal(BigDecimal("12.345")).cents)
    }

    @Test fun cartTotalsComeFromQuantitiesAndPrices() {
        val cart = Cart(listOf(item(1, 250, 2), item(2, 199, 3)))
        assertEquals(5, cart.totalItems)
        assertEquals(1097L, cart.totalPrice.cents)
        assertEquals(0, Cart(emptyList()).totalItems)
        assertEquals(0L, Cart(emptyList()).totalPrice.cents)
    }

    @Test(expected = IllegalArgumentException::class)
    fun cartItemCannotHaveZeroQuantity() { item(1, 100, 0) }

    private fun item(id: Int, cents: Long, quantity: Int) =
        CartItem(id, "Product $id", Money(cents), "image", quantity)
}
