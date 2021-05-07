package com.example.foodorder.fargment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.foodorder.R
import com.example.foodorder.adapter.OrderHistoryAdapter
import com.example.foodorder.model.FoodItem
import com.example.foodorder.model.MenuList
import com.example.foodorder.model.OrderHistoryList
import java.lang.Exception

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OrderHistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OrderHistoryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var recyclerView: RecyclerView
    lateinit var recyclerAdapter: OrderHistoryAdapter
    lateinit var linearLayoutManager:LinearLayoutManager


    val orderInfoList = arrayListOf<OrderHistoryList>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_order_history, container, false)

        val sharedPreferences = activity?.getSharedPreferences(getString(R.string.Preference_file_name), Context.MODE_PRIVATE)
        val user_id =  sharedPreferences?.getString("user_id", "1")
        recyclerView = view.findViewById(R.id.recyclerorderhistory)
        linearLayoutManager = LinearLayoutManager(activity as Context)


        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v2/orders/fetch_result/$user_id"

        val jsonObjectRequest = object : JsonObjectRequest(Method.GET,url,null,Response.Listener {
            try {
                val data = it.getJSONObject("data")
                val success = data.getBoolean("success")
                if(success)
                {
                    val info = data.getJSONArray("data")
                    for(j in 0 until info.length()) {
                        val foodinfoitem = arrayListOf<FoodItem>()
                        val orderitem = info.getJSONObject(j)
                        val fooditem = orderitem.getJSONArray("food_items")

                        for (i in 0 until fooditem.length()) {
                            val foodinfo = fooditem.getJSONObject(i)
                            val item = FoodItem(
                                foodinfo.getString("food_item_id"),
                                foodinfo.getString("name"),
                                foodinfo.getString("cost")
                            )
                            foodinfoitem.add(item)
                        }
                        val orderdata = OrderHistoryList(
                            orderitem.getString("order_id"),
                            orderitem.getString("restaurant_name"),
                            orderitem.getString("total_cost"),
                            orderitem.getString("order_placed_at"),
                            foodinfoitem
                        )
                        orderInfoList.add(orderdata)
                    }
                    recyclerAdapter = OrderHistoryAdapter(activity as Context,orderInfoList)
                    recyclerView.layoutManager = linearLayoutManager
                    recyclerView.adapter = recyclerAdapter
                }
            }
            catch (e:Exception)
            {
                Toast.makeText(activity as Context,"Some Error Occur $e",Toast.LENGTH_SHORT).show()
            }
        },Response.ErrorListener {  }){
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String,String>()
                headers["Content-type"] = "application/json"
                headers["token"] = "f447a4b9862de8"
                return headers
            }
        }
        queue.add(jsonObjectRequest)
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OrderHistoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OrderHistoryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}