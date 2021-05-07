package com.example.foodorder.adapter

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.provider.Settings
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorder.R
import com.example.foodorder.activity.MenuListActivity
import com.example.foodorder.database.CartEntity
import com.example.foodorder.model.MenuList


class MenuListAdapter (val context : Context, val itemList : ArrayList<MenuList>): RecyclerView.Adapter<MenuListAdapter.MenuListViewholder>() {


    val orderlist = arrayListOf<CartEntity>()
    class MenuListViewholder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.menulistname)
        val cost: TextView = view.findViewById(R.id.cost_for_one_menulist)
        val btnadd: Button = view.findViewById(R.id.btnadd)
        val srlno: TextView = view.findViewById(R.id.srno)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuListViewholder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.menulist_rows, parent, false)
        return MenuListViewholder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: MenuListViewholder, position: Int) {

        val menu = itemList[position]
        holder.name.text = menu.name
        holder.cost.text = menu.cost_for_one
        holder.srlno.text = (position + 1).toString()

        val orderEntity = CartEntity(
            menu.id.toInt(),
            menu.name,
            menu.cost_for_one.toInt(),
            menu.restaurant_id.toInt()
        )

        if(orderlist.contains(orderEntity))
        {
            holder.btnadd.text = "Remove"
            val favcolor =
                ContextCompat.getColor(context, R.color.colorRemoveCart)
            holder.btnadd.setBackgroundColor(favcolor)
            holder.btnadd.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_butrem,0)
        }
        else
        {
            holder.btnadd.text = "Add"
            val notfavcolor =
                ContextCompat.getColor(context, R.color.colorPrimary)
            holder.btnadd.setBackgroundColor(notfavcolor)
            holder.btnadd.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_butadd,0)
        }

        holder.btnadd.setOnClickListener {
            if(holder.btnadd.text == "Add") {
                orderlist.add(orderEntity)
                (context as MenuListActivity).findViewById<Button>(R.id.btnproceedtoCart).visibility = View.VISIBLE
            }
            else
            {
                orderlist.remove(orderEntity)
                if(orderlist.isNullOrEmpty()) {
                    (context as MenuListActivity).findViewById<Button>(R.id.btnproceedtoCart).visibility =
                        View.GONE
                }
            }
            (context as MenuListActivity).recyclerAdapter.notifyDataSetChanged()
        }
    }
    fun getOrderList():ArrayList<CartEntity>
    {
        return orderlist
    }
}
