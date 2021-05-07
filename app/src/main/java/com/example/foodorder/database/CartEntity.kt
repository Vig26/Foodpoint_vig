package com.example.foodorder.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orderCart")
data class CartEntity(
    @PrimaryKey val itemId : Int,
    @ColumnInfo(name="itemName") val name : String,
    @ColumnInfo(name="Cost_for_one")val cost_for_one : Int,
    @ColumnInfo(name="restaurantId")val restaurant_id : Int
)