package com.example.foodorder.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.foodorder.R
import com.example.foodorder.adapter.CartRecyclerAdapter
import com.example.foodorder.database.CartDatabase
import com.example.foodorder.database.CartEntity
import kotlinx.android.synthetic.main.activity_cart.*
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class CartActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var orderTxt : TextView
    lateinit var layoutManager: LinearLayoutManager
    lateinit var btnplaceOrder : Button
    lateinit var recyclerAdapter: CartRecyclerAdapter
    lateinit var toolbar:Toolbar
    lateinit var sharedPreferences: SharedPreferences
    var resName : String? = "Restaurant Name"
    var resId : String? = "1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        var total_cost = 0
        recyclerView = findViewById(R.id.cartrecycler)
        btnplaceOrder = findViewById(R.id.btnplaceOrder)
        toolbar = findViewById(R.id.carttoolbar)
        orderTxt = findViewById(R.id.orderfrom)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "My Cart"
        supportActionBar?.setHomeButtonEnabled(true)
        sharedPreferences =
            getSharedPreferences(getString(R.string.Preference_file_name), Context.MODE_PRIVATE)
        val dbItemList = GetfromCart(this@CartActivity).execute().get()
        toolbar.setNavigationOnClickListener { super.onBackPressed() }
        layoutManager = LinearLayoutManager(this@CartActivity)
        val user_id = sharedPreferences.getString("user_id", "1")
        for (element in dbItemList) {
            total_cost += element.cost_for_one
        }
        resName = intent.getStringExtra("res_name")
        orderTxt.text = resName
        resId = intent.getStringExtra("res_id")
        recyclerAdapter = CartRecyclerAdapter(this@CartActivity as Context, dbItemList)
        recyclerView.adapter = recyclerAdapter
        recyclerView.layoutManager = layoutManager

        btnplaceOrder.text = "Place Order(Total: Rs.$total_cost)"



        val queue = Volley.newRequestQueue(this@CartActivity)
        val url = "http://13.235.250.119/v2/place_order/fetch_result/"

        val jsonParams = JSONObject()
        jsonParams.put("user_id", user_id)
        println("user Id$user_id")
        jsonParams.put("restaurant_id", resId)
        println("res Id $resId")
        jsonParams.put("total_cost", total_cost)
        val foodArray = JSONArray()
        for (i in 0 until dbItemList.size) {
            val foodid = JSONObject()
            foodid.put("food_item_id", dbItemList[i].itemId)
            foodArray.put(i, foodid)
        }
        jsonParams.put("food", foodArray)


        btnplaceOrder.setOnClickListener {
            val jsonObjectRequest =
                object : JsonObjectRequest(Method.POST, url, jsonParams, Response.Listener {
                    try {
                        val data = it.getJSONObject("data")
                        val success = data.getBoolean("success")
                        if (success) {
                            val intent = Intent(this@CartActivity,PlcaeorderActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this@CartActivity, "Not Placed", Toast.LENGTH_SHORT)
                                .show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(this@CartActivity, "Some Error Occure", Toast.LENGTH_SHORT)
                            .show()
                    }
                }, Response.ErrorListener {
                    Toast.makeText(
                        this@CartActivity,
                        "Your Order is not Placed",
                        Toast.LENGTH_SHORT
                    ).show()

                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "f447a4b9862de8"
                        return headers
                    }
                }
            queue.add(jsonObjectRequest)
        }
    }
    class GetfromCart(val context: Context): AsyncTask<Void, Void, List<CartEntity>>()
    {
        override fun doInBackground(vararg params: Void?) : List<CartEntity> {
            val db = Room.databaseBuilder(context, CartDatabase::class.java,"cart-db").build()

            return db.cartDao().getAllcart()
        }

    }
}
