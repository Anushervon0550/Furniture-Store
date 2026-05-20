package com.example.furniturestore.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.furniturestore.data.local.dao.CartDao
import com.example.furniturestore.data.local.dao.FavoriteDao
import com.example.furniturestore.data.local.entity.CartEntity
import com.example.furniturestore.data.local.entity.FavoriteEntity

@Database(
    entities = [CartEntity::class, FavoriteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
    abstract fun favoriteDao(): FavoriteDao
}
