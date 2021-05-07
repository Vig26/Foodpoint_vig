package com.example.foodorder.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorder.R
import com.example.foodorder.model.FoodItem

class FoodItemsAdapter(val  context: Context, val FoodItem : ArrayList<FoodItem>): RecyclerView.Adapter<FoodItemsAdapter.FoodItemViewHolder>()
{

    class FoodItemViewHolder(view : View): RecyclerView.ViewHolder(view)
    {
        val item_name : TextView = view.findViewById(R.id.cartitemName)
        val cost_for_one : TextView = view.findViewById(R.id.cartrupees)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodItemsAdapter.FoodItemViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cartlist_row,parent,false)

        return FoodItemsAdapter.FoodItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return FoodItem.size
    }

    override fun onBindViewHolder(holder: FoodItemsAdapter.FoodItemViewHolder, position: Int) {
        val res = FoodItem[position]
        holder.item_name.text = res.name
        holder.cost_for_one.text = res.cost
    }
}