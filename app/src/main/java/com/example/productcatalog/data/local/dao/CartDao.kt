package com.example.productcatalog.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.productcatalog.data.local.entity.CartItemEntity
import com.example.productcatalog.domain.model.CartItem
import kotlinx.coroutines.flow.Flow

@Dao
abstract class CartDao {
    @Query("SELECT * FROM cart_items ORDER BY productId")
    abstract fun observeItems(): Flow<List<CartItemEntity>>
    @Query("SELECT * FROM cart_items WHERE productId = :id")
    abstract suspend fun find(id: Int): CartItemEntity?
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun put(item: CartItemEntity)
    @Query("DELETE FROM cart_items WHERE productId = :id")
    abstract suspend fun remove(id: Int)
    @Query("UPDATE cart_items SET quantity = quantity + 1 WHERE productId = :id AND quantity < 999")
    abstract suspend fun increment(id: Int): Int
    @Query("UPDATE cart_items SET quantity = quantity - 1 WHERE productId = :id AND quantity > 1")
    abstract suspend fun decrement(id: Int)
    @Query("DELETE FROM cart_items WHERE productId = :id AND quantity = 1")
    abstract suspend fun removeSingle(id: Int): Int

    @Transaction
    open suspend fun add(item: CartItemEntity): Boolean {
        val existing = find(item.productId)
        if (existing == null) put(item.copy(quantity = 1))
        else if (existing.quantity < CartItem.MAX_QUANTITY) increment(item.productId)
        else return false
        return true
    }
    @Transaction
    open suspend fun increase(id: Int): Boolean {
        val existing = find(id) ?: return true
        if (existing.quantity >= CartItem.MAX_QUANTITY) return false
        increment(id)
        return true
    }
    @Transaction
    open suspend fun decrease(id: Int) {
        if (removeSingle(id) == 0) decrement(id)
    }
}
