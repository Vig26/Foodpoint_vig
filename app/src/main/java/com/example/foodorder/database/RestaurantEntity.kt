package com.example.foodorder.database


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Restaurants")
class RestaurantEntity (
    @PrimaryKey val resId : Int,
    @ColumnInfo(name = "resName") val resname : String,
    @ColumnInfo(name = "resRate")val resrating : String,
    @ColumnInfo(name = "resCost")val rescost : String,
    @ColumnInfo(name = "resImage")val resimage : String
)