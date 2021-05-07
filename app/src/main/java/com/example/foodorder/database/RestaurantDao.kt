package com.example.foodorder.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RestaurantDao {

    @Insert
    fun insertRestaurant(resEntity : RestaurantEntity)

    @Delete
    fun deleteRestaurant(resEntity : RestaurantEntity)

    @Query("SELECT * FROM Restaurants" )
    fun getallRes():List<RestaurantEntity>

    @Query("SELECT * FROM Restaurants WHERE resId = :res_id")
    fun getresById(res_id : String):RestaurantEntity

    @Query("DELETE FROM Restaurants")
    fun deleteallfav()
}