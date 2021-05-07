package com.example.foodorder.model

data class OrderHistoryList (
    val order_id : String,
    val restaurant_name : String,
    val total_cost : String,
    val orderplacedate : String,
    val foodItems : ArrayList<FoodItem>
)