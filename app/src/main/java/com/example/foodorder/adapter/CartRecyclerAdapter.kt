package com.example.foodorder.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorder.R
import com.example.foodorder.database.CartEntity

class CartRecyclerAdapter(val  context: Context,val cartItem : List<CartEntity>):RecyclerView.Adapter<CartRecyclerAdapter.CartViewHolder>()
{

    class CartViewHolder(view : View):RecyclerView.ViewHolder(view)
    {
        val item_name : TextView = view.findViewById(R.id.cartitemName)
        val cost_for_one : TextView = view.findViewById(R.id.cartrupees)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartRecyclerAdapter.CartViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cartlist_row,parent,false)

        return CartRecyclerAdapter.CartViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cartItem.size
    }

    override fun onBindViewHolder(holder: CartRecyclerAdapter.CartViewHolder, position: Int) {

        val res = cartItem[position]
        holder.item_name.text = res.name
        holder.cost_for_one.text = res.cost_for_one.toString()
    }
}