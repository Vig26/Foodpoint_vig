package com.example.foodorder.activity


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils.isEmpty
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.foodorder.R
import com.example.foodorder.adapter.AllRestaurantsAdapter
import com.example.foodorder.adapter.FavoritesListAdapter
import com.example.foodorder.adapter.MenuListAdapter
import com.example.foodorder.database.CartDatabase
import com.example.foodorder.database.CartEntity
import com.example.foodorder.database.RestaurantDatabase
import com.example.foodorder.database.RestaurantEntity
import com.example.foodorder.model.MenuList
import com.example.foodorder.model.Restaurants
import com.example.foodorder.util.ConnectionManager

class MenuListActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: LinearLayoutManager
    lateinit var recyclerAdapter : MenuListAdapter
    lateinit var toolbarmenu : Toolbar
    var resId : String? = "1"
    var resName : String? = "Restaurant Name"
    lateinit var btnproceed : Button
    lateinit var progressBarLayout: RelativeLayout
    lateinit var progressBar: ProgressBar

    val menuInfoList = arrayListOf<MenuList>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_list)


        progressBarLayout = findViewById(R.id.progresslayoutmenu)
        progressBar = findViewById(R.id.progressbarmenu)
        progressBarLayout.visibility = View.VISIBLE
        progressBar.visibility = View.VISIBLE
        resId = intent.getStringExtra("res_id")
        resName = intent.getStringExtra("res_name")
        recyclerView = findViewById(R.id.recyclermenulist)
        toolbarmenu = findViewById(R.id.toolbarmenulist)
        btnproceed = findViewById(R.id.btnproceedtoCart)
        setSupportActionBar(toolbarmenu)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = resName
        supportActionBar?.setHomeButtonEnabled(true)
        toolbarmenu.setNavigationOnClickListener { onBackPressed() }
        layoutManager = LinearLayoutManager(this@MenuListActivity)

        if(ConnectionManager().checkConnectivity(this@MenuListActivity))
        {
            val queue = Volley.newRequestQueue(this@MenuListActivity)
            val url = "http://13.235.250.119/v2/restaurants/fetch_result/$resId"

            val jsonObjectRequest = object : JsonObjectRequest(Method.GET,url,null, Response.Listener {
                try {
                    val data = it.getJSONObject("data")
                    val success = data.getBoolean("success")
                    if(success)
                    {
                        progressBarLayout.visibility = View.GONE
                        val info = data.getJSONArray("data")

                        for (i in 0 until info.length()) {
                            val menuJsonObject = info.getJSONObject(i)

                            val menuData = MenuList(
                                menuJsonObject.getString("id"),
                                menuJsonObject.getString("name"),
                                menuJsonObject.getString("cost_for_one"),
                                menuJsonObject.getString("restaurant_id")
                            )
                            menuInfoList.add(menuData)
                        }
                        recyclerAdapter = MenuListAdapter(this@MenuListActivity as Context, menuInfoList)
                        recyclerView.layoutManager = layoutManager
                        recyclerView.adapter = recyclerAdapter
                    }
                    else
                    {
                        Toast.makeText(this@MenuListActivity,"Some error Occur", Toast.LENGTH_SHORT).show()
                    }
                }
                catch (e:Exception) {
                    Toast.makeText(this@MenuListActivity,"Unexpected error Occur", Toast.LENGTH_SHORT).show()
                }
            }, Response.ErrorListener {
                Toast.makeText(this@MenuListActivity,"Some error occured", Toast.LENGTH_SHORT).show()
            }){
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String,String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "f447a4b9862de8"
                    return headers
                }
            }
            queue.add(jsonObjectRequest)
        }
        else
        {
            val dialog = androidx.appcompat.app.AlertDialog.Builder(this@MenuListActivity)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection is not found")
            dialog.setPositiveButton("Settings") { text, listener ->
                val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                finish()
            }
            dialog.setNegativeButton("Cancel") { text, listener ->
                ActivityCompat.finishAffinity(this@MenuListActivity)
            }
            dialog.create()
            dialog.show()
        }
        btnproceed.setOnClickListener {
            DeleteCart(this@MenuListActivity).execute().get()
            val itemList = recyclerAdapter.getOrderList()
            for( i in itemList)
            {
                val async = DBAsyncTask(this@MenuListActivity,i,1).execute()
                val result = async.get()
                if(result)
                {
                   val intent = Intent(this@MenuListActivity,CartActivity::class.java)
                    intent.putExtra("res_name",resName)
                    intent.putExtra("res_id",resId)
                    intent.addCategory(Intent.CATEGORY_HOME)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }
                else
                {
                    Toast.makeText(this@MenuListActivity,"Some Error Occur",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onBackPressed() {
        if(recyclerAdapter.getOrderList().isNotEmpty())
        {
            val dialog = androidx.appcompat.app.AlertDialog.Builder(this@MenuListActivity)
            dialog.setTitle("Confirmation")
            dialog.setMessage("Going back will reset cart items.Do you still want to proceed?")
            dialog.setPositiveButton("YES") { text, listener ->
                DeleteCart(this@MenuListActivity).execute().get()
                super.onBackPressed()
            }
            dialog.setNegativeButton("NO") { text, listener ->

            }
            dialog.create()
            dialog.show()
        }
        else {
            super.onBackPressed()
        }
    }
}
class DeleteCart(val context: Context):AsyncTask<Void , Void, Boolean>()
{
    override fun doInBackground(vararg params: Void?) : Boolean {
        val db = Room.databaseBuilder(context, CartDatabase::class.java,"cart-db").build()

        db.cartDao().deleteAllCart()

        return true
    }

}
class DBAsyncTask(val context: Context, val orderEntity: CartEntity, val mode:Int): AsyncTask<Void, Void, Boolean>()
{
    override fun doInBackground(vararg params: Void?): Boolean {
        val db = Room.databaseBuilder(context, CartDatabase::class.java,"cart-db").build()
        when(mode)
        {
            1 ->
            {
                db.cartDao().insertCart(orderEntity)
                db.close()
                return true
            }
            2 ->
            {
                db.cartDao().deleteAllCart()
                db.close()
                return true
            }

        }
        return false
    }
}