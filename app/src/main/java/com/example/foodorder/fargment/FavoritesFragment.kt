package com.example.foodorder.fargment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.foodorder.R
import com.example.foodorder.adapter.AllRestaurantsAdapter
import com.example.foodorder.adapter.FavoritesListAdapter
import com.example.foodorder.database.RestaurantDatabase
import com.example.foodorder.database.RestaurantEntity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FavoritesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavoritesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: GridLayoutManager
    lateinit var recyclerAdapter : FavoritesListAdapter
    lateinit var relativeLayout: RelativeLayout
    var dbrestaurantlist = arrayListOf<RestaurantEntity>()

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
        val view = inflater.inflate(R.layout.fragment_favorites, container, false)
        relativeLayout = view.findViewById(R.id.favempty)
        recyclerView = view.findViewById(R.id.recyclerfav)
        layoutManager = GridLayoutManager(activity  as Context,2)
        dbrestaurantlist = RetriveFavorites(activity as Context).execute().get()
        if(dbrestaurantlist.isNullOrEmpty())
        {
            relativeLayout.visibility=View.VISIBLE
        }
        if(!dbrestaurantlist.isNullOrEmpty())
        {
            relativeLayout.visibility = View.GONE
            recyclerAdapter = FavoritesListAdapter(activity as Context,dbrestaurantlist,this)
            recyclerView.adapter = recyclerAdapter
            recyclerView.layoutManager = layoutManager
        }

        return view
    }
    fun notifyRecycler()
    {
        if(dbrestaurantlist.isNullOrEmpty())
        {
            relativeLayout.visibility = View.VISIBLE
        }
        recyclerAdapter.notifyDataSetChanged()
    }
    class RetriveFavorites(val context: Context): AsyncTask<Void, Void, ArrayList<RestaurantEntity>>()
    {
        override fun doInBackground(vararg params: Void?): ArrayList<RestaurantEntity> {
            val db = Room.databaseBuilder(context, RestaurantDatabase::class.java,"restaurants-db").build()

            return db.resDao().getallRes() as ArrayList<RestaurantEntity>
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FavoritesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FavoritesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}