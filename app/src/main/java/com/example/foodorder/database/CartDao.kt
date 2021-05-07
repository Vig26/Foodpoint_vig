package com.example.foodorder.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CartDao {

    @Insert
    fun insertCart( cartEntity : CartEntity)

    @Delete
    fun deleteCart(cartEntity: CartEntity)

    @Query("SELECT * FROM orderCart")
    fun getAllcart() : List<CartEntity>

    @Query("DELETE FROM orderCart")
    fun deleteAllCart()
}