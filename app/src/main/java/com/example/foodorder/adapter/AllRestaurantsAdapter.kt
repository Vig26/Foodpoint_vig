package com.example.foodorder.adapter

import android.content.Context
import android.content.Intent
import android.media.Image
import android.os.AsyncTask
import android.os.TestLooperManager
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.foodorder.R
import com.example.foodorder.activity.MenuListActivity
import com.example.foodorder.database.RestaurantDatabase
import com.example.foodorder.database.RestaurantEntity
import com.example.foodorder.model.Restaurants
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.restaurants_row.view.*

class AllRestaurantsAdapter(val context: Context , val itemList : ArrayList<Restaurants>):RecyclerView.Adapter<AllRestaurantsAdapter.AllRestaurantsViewholder>() {

    class AllRestaurantsViewholder(view : View):RecyclerView.ViewHolder(view)
    {
        val name : TextView = view.findViewById(R.id.restaurantname)
        val rating : TextView = view.findViewById(R.id.ratingres)
        val cost : TextView = view.findViewById(R.id.price)
        val image : ImageView = view.findViewById(R.id.imglist)
        val content : LinearLayout = view.findViewById(R.id.content)
        val favorite : ImageView = view.findViewById(R.id.favoriteRestaurants)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllRestaurantsViewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.restaurants_row,parent,false)

        return AllRestaurantsViewholder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: AllRestaurantsViewholder, position: Int) {

        val res = itemList[position]
        holder.name.text = res.name
        holder.rating.text = res.rating
        holder.cost.text = res.cost
        Picasso.get().load(res.image).into(holder.image)
        val resEntity = RestaurantEntity(
            res.id.toInt(),
            res.name,
            res.rating,
            res.cost,
            res.image
        )
        holder.content.setOnClickListener {
            val intent = Intent(context,MenuListActivity::class.java)
            intent.putExtra("res_id",res.id)
            intent.putExtra("res_name",res.name)
            context.startActivity(intent)
        }
        val checkFav = DBAsyncTask(context, resEntity, 1).execute()
        val isFav = checkFav.get()
        if (isFav) {
            holder.favorite.setImageResource(R.drawable.ic_favres)
        }else {
            holder.favorite.setImageResource(R.drawable.ic_favorites)
        }
        holder.favorite.setOnClickListener {
            if (!DBAsyncTask(context, resEntity, 1).execute().get()) {
                val async = DBAsyncTask(context, resEntity, 2).execute()
                val result = async.get()
                if (result) {
                    Toast.makeText(
                         context,
                        "Restaurant added to Favorites",
                        Toast.LENGTH_SHORT
                    ).show()
                    holder.favorite.setImageResource(R.drawable.ic_favres)
                } else {
                    Toast.makeText(
                        context,
                        "Some Error has Occur",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                val async = DBAsyncTask(context, resEntity, 3).execute()
                val result = async.get()
                if (result) {
                    Toast.makeText(
                        context,
                        "Restaurant Removed from Favorites",
                        Toast.LENGTH_SHORT
                    ).show()
                    holder.favorite.setImageResource(R.drawable.ic_favorites)
                } else {
                    Toast.makeText(
                        context,
                        "Some Error has Occured",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
class DBAsyncTask(val context: Context, val resEntity: RestaurantEntity, val mode:Int): AsyncTask<Void, Void, Boolean>()
{
    override fun doInBackground(vararg params: Void?): Boolean {
        val db = Room.databaseBuilder(context, RestaurantDatabase::class.java,"restaurants-db").build()
        when(mode)
        {
            1 ->
            {
                val restaurant : RestaurantEntity? = db.resDao().getresById(resEntity.resId.toString())
                db.close()
                return restaurant != null
            }
            2 ->
            {
                db.resDao().insertRestaurant(resEntity)
                db.close()
                return true
            }
            3 ->
            {
                db.resDao().deleteRestaurant(resEntity)
                db.close()
                return true
            }
        }
        return false
    }
}