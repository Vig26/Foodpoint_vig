package com.example.foodorder.fargment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Request.Method.GET
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.foodorder.R
import com.example.foodorder.adapter.AllRestaurantsAdapter
import com.example.foodorder.database.RestaurantDatabase
import com.example.foodorder.database.RestaurantEntity
import com.example.foodorder.model.Restaurants
import com.example.foodorder.util.ConnectionManager
import java.lang.reflect.Method
import java.util.*
import kotlin.collections.HashMap

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AllRestuarants.newInstance] factory method to
 * create an instance of this fragment.
 */
class AllRestuarants : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: LinearLayoutManager
    lateinit var recyclerAdapter : AllRestaurantsAdapter
    lateinit var progressBarLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    val ratingComparator = Comparator<Restaurants>{
            res1,res2 ->
        res1.rating.compareTo(res2.rating,true)
    }
    val CostComparator = Comparator<Restaurants>{res1,res2 ->
        res1.cost.compareTo(res2.cost,true)
    }

    val resInfoList = arrayListOf<Restaurants>()
    var checkedRadio = 0
    val tempInfoList = arrayListOf<Restaurants>()

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
        val view = inflater.inflate(R.layout.fragment_all_restaurants, container, false)
        setHasOptionsMenu(true)
        recyclerView = view.findViewById(R.id.recyclerviewRestaurantpage)
        progressBarLayout = view.findViewById(R.id.progresslayoutres)
        progressBar = view.findViewById(R.id.progressbarres)
        progressBarLayout.visibility = View.VISIBLE
        progressBar.visibility = View.VISIBLE
        layoutManager = LinearLayoutManager(activity)

        if(ConnectionManager().checkConnectivity(activity as Context))
        {
            val queue = Volley.newRequestQueue(activity as Context)
            val url = "http://13.235.250.119/v2/restaurants/fetch_result/"

            val jsonObjectRequest = object :JsonObjectRequest(Method.GET,url,null,Response.Listener {
                try {
                    val data = it.getJSONObject("data")
                    val success = data.getBoolean("success")
                    if(success)
                    {
                        progressBarLayout.visibility =View.GONE
                        val info = data.getJSONArray("data")

                        for (i in 0 until info.length()) {
                            val resJsonObject = info.getJSONObject(i)

                            val resData = Restaurants(
                                resJsonObject.getString("id"),
                                resJsonObject.getString("name"),
                                resJsonObject.getString("rating"),
                                resJsonObject.getString("cost_for_one"),
                                resJsonObject.getString("image_url")
                            )
                            resInfoList.add(resData)
                        }
                        tempInfoList.addAll(resInfoList)
                        recyclerAdapter =
                            AllRestaurantsAdapter(activity as Context, resInfoList)
                        recyclerView.adapter = recyclerAdapter
                        recyclerView.layoutManager = layoutManager


                        }
                    else
                    {
                        Toast.makeText(activity as Context,"Some error Occur",Toast.LENGTH_SHORT).show()
                    }
                    }
                catch (e:Exception) {
                    if(activity != null) {
                        Toast.makeText(
                            activity as Context,
                            "Unexpected error Occur",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            },Response.ErrorListener {
                Toast.makeText(activity as Context,"Some error occured",Toast.LENGTH_SHORT).show()
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
            val dialog = androidx.appcompat.app.AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internt Connection is not found")
            dialog.setPositiveButton("Settings") { text, listener ->
                val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Cancel") { text, listener ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }

        return view
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.menu_soting,menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item?.itemId
        if(id == R.id.menusort)
        {
            val dialog = AlertDialog.Builder(activity as Context)
            var checkedItems = "Nothing"
            val listItems = arrayOf("Relavance","Cost(Low to High)","Cost(High to Low)","Rating")
            dialog.setTitle("Sort By?")
            dialog.setSingleChoiceItems(listItems,checkedRadio){
                    dialogInterface , i ->checkedItems = listItems[i]
            }
            dialog.setNegativeButton("OK"){text, listener ->
                if(checkedItems == listItems[0])
                {
                    checkedRadio = 0
                    resInfoList.clear()
                    resInfoList.addAll(tempInfoList)
                    recyclerAdapter.notifyDataSetChanged()
                }
                else if(checkedItems == listItems[1])
                {
                    checkedRadio = 1
                    Collections.sort(resInfoList , CostComparator)
                    recyclerAdapter.notifyDataSetChanged()
                }
                else if(checkedItems == listItems[2])
                {
                    checkedRadio = 2
                    Collections.sort(resInfoList ,CostComparator)
                    resInfoList.reverse()
                    recyclerAdapter.notifyDataSetChanged()
                }
                else
                {
                    checkedRadio = 3
                    Collections.sort(resInfoList , ratingComparator)
                    resInfoList.reverse()
                    recyclerAdapter.notifyDataSetChanged()
                }
            }
            dialog.create()
            dialog.show()
        }
        recyclerAdapter.notifyDataSetChanged()
        return super.onOptionsItemSelected(item)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AllRestuarants.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AllRestuarants().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}