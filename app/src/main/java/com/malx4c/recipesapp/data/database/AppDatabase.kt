package com.malx4c.recipesapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.malx4c.recipesapp.data.dao.CategoryDao
import com.malx4c.recipesapp.data.dao.RecipesDao
import com.malx4c.recipesapp.model.Category
import com.malx4c.recipesapp.model.Recipe

@Database(entities = [Category::class, Recipe::class], version = 1)
abstract  class AppDatabase: RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun recipesDao(): RecipesDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        "database-recipes"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}