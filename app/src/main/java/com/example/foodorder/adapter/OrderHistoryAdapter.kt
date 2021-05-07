package com.example.foodorder.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorder.R
import com.example.foodorder.model.OrderHistoryList


class OrderHistoryAdapter(val  context: Context, val orderhistItem : ArrayList<OrderHistoryList>): RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder>()
{

   lateinit var recyclerAdapter : FoodItemsAdapter
    lateinit var layoutManager: LinearLayoutManager


    class OrderHistoryViewHolder(view : View): RecyclerView.ViewHolder(view)
    {
        val res_name : TextView = view.findViewById(R.id.resnmaeorder)
        val date : TextView = view.findViewById(R.id.orderhistoryDate)
        val recyclerView : RecyclerView = view.findViewById(R.id.recycleritems)
        val totalCost : TextView = view.findViewById(R.id.totalcosthis)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):OrderHistoryAdapter.OrderHistoryViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.orderhistory_row,parent,false)

        return OrderHistoryAdapter.OrderHistoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return orderhistItem.size
    }

    override fun onBindViewHolder(holder: OrderHistoryAdapter.OrderHistoryViewHolder, position: Int) {

        val res = orderhistItem[position]
        holder.res_name.text = res.restaurant_name
        holder.date.text = res.orderplacedate.split(" ")[0]
        val tot = res.total_cost
        holder.totalCost.text = "Total cost : Rs.$tot"
        recyclerAdapter = FoodItemsAdapter(context,res.foodItems)
        layoutManager = LinearLayoutManager(context)
        holder.recyclerView.layoutManager = layoutManager
        holder.recyclerView.adapter = recyclerAdapter
    }
}