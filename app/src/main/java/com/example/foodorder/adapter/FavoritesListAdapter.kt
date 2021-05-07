package com.example.foodorder.adapter

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.foodorder.R
import com.example.foodorder.activity.MenuListActivity
import com.example.foodorder.database.RestaurantDatabase
import com.example.foodorder.database.RestaurantEntity
import com.example.foodorder.fargment.FavoritesFragment
import com.example.foodorder.model.Restaurants
import com.squareup.picasso.Picasso

class FavoritesListAdapter(val context: Context, val itemList : ArrayList<RestaurantEntity>,val Fav:FavoritesFragment):
    RecyclerView.Adapter<FavoritesListAdapter.AllRestaurantsViewholder>() {

    class AllRestaurantsViewholder(view : View): RecyclerView.ViewHolder(view)
    {
        val name : TextView = view.findViewById(R.id.txtresnamefav)
        val rating : TextView = view.findViewById(R.id.txtresratefav)
        val cost : TextView = view.findViewById(R.id.txtrsss)
        val image : ImageView = view.findViewById(R.id.imgfav)
        val imgbtn : ImageView = view.findViewById(R.id.favimagres)
        val content : RelativeLayout = view.findViewById(R.id.reltaivelaoutfav)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllRestaurantsViewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.favorites_rows,parent,false)

        return AllRestaurantsViewholder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: AllRestaurantsViewholder, position: Int) {

        val res = itemList[position]
        holder.name.text = res.resname
        holder.rating.text = res.resrating
        val cost = res.rescost
        holder.cost.text = "Rs.$cost"
        Picasso.get().load(res.resimage).into(holder.image)
        val resEntity = RestaurantEntity(
            res.resId.toInt(),
            res.resname,
            res.resrating,
            res.rescost,
            res.resimage
        )

        holder.content.setOnClickListener {
            val intent = Intent(context, MenuListActivity::class.java)
            intent.putExtra("res_id",res.resId.toString())
            intent.putExtra("res_name",res.resname)
            context.startActivity(intent)
        }
        holder.imgbtn.setOnClickListener {
                val async = DBAsyncTask(context, resEntity, 3).execute()
                val result = async.get()
                if (result) {
                    Toast.makeText(
                        context,
                        "Restaurant Removed from Favorites",
                        Toast.LENGTH_SHORT
                    ).show()
                    itemList.removeAt(position)
                    Fav.notifyRecycler()
                }
        }
    }
}