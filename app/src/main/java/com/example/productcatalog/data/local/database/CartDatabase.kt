package com.example.productcatalog.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.productcatalog.data.local.dao.CartDao
import com.example.productcatalog.data.local.entity.CartItemEntity

@Database(entities = [CartItemEntity::class], version = 1, exportSchema = true)
abstract class CartDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
}
