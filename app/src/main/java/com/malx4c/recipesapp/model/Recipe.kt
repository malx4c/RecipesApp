package com.malx4c.recipesapp.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Entity
@Serializable
@Parcelize
@TypeConverters(RecipeTypeConverter::class)
data class Recipe(
    @PrimaryKey val id: Int,
    val title: String,
    val ingredients: List<Ingredient>,
    val method: List<String>,
    val imageUrl: String,
    @ColumnInfo(name = "categoryId") var categoryId: Int? = null,
    @ColumnInfo(name = "isFavorite") val isFavorite: Boolean = false
) : Parcelable

class RecipeTypeConverter {
    @TypeConverter
    fun fromIngredient(ingredients: List<Ingredient>): String {
        return Json.encodeToString(ingredients)
    }

    @TypeConverter
    fun toIngredient(data: String): List<Ingredient> {
        return Json.decodeFromString(data)
    }

    @TypeConverter
    fun fromMethod(method: List<String>): String {
        return Json.encodeToString(method)
    }

    @TypeConverter
    fun toMethod(data: String): List<String> {
        return Json.decodeFromString(data)
    }
}