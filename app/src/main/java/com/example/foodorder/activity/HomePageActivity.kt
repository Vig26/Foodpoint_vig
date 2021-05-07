package com.example.foodorder.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.TestLooperManager
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.room.Room
import com.example.foodorder.R
import com.example.foodorder.database.CartDatabase
import com.example.foodorder.database.RestaurantDatabase
import com.example.foodorder.fargment.*
import com.google.android.material.navigation.NavigationView

class HomePageActivity : AppCompatActivity() {
    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var navigationView: NavigationView
    lateinit var toolbar: Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var sharedPreferences: SharedPreferences
    lateinit var headerView: View
    lateinit var txtname : TextView
    lateinit var txtmobnum :TextView

    var previousMenuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        drawerLayout = findViewById(R.id.drawer_layout)
        coordinatorLayout = findViewById(R.id.coordinator_layout)
        navigationView = findViewById(R.id.navigation_menu)
        toolbar = findViewById(R.id.toolbarrestaurent)
        frameLayout = findViewById(R.id.frame_layout)
        headerView = navigationView.getHeaderView(0)
        txtname = headerView.findViewById(R.id.navtxtname)
        txtmobnum = headerView.findViewById(R.id.navtxtmobnum)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "The toolbar"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        openAllRestaurants()
        sharedPreferences = getSharedPreferences("FoodOrder Preferences", Context.MODE_PRIVATE)
        val name = sharedPreferences.getString("name","Name")
        val mobnum = sharedPreferences.getString("mobile_number","Mobile Number")
        txtname.text = name
        txtmobnum.text = "+91-$mobnum"

        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this@HomePageActivity,
            drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        navigationView.setNavigationItemSelectedListener {
            if (previousMenuItem != null) {
                previousMenuItem?.isChecked = false
            }
            it.isCheckable = true
            it.isChecked = true
            previousMenuItem = it

            when(it.itemId)
            {
                R.id.home ->{
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame_layout,
                            AllRestuarants()
                        )
                        .commit()
                    supportActionBar?.title = "All Restaurants"
                    drawerLayout.closeDrawers()
                }
                R.id.myprofile ->{
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame_layout,
                            MyProfileFragment()
                        )
                        .commit()
                    supportActionBar?.title = "My Profile"
                    drawerLayout.closeDrawers()
                }
                R.id.favrestau ->
                {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame_layout,
                            FavoritesFragment()
                        )
                        .commit()
                    supportActionBar?.title = "Favorite Restaurants"
                    drawerLayout.closeDrawers()
                }
                R.id.faqs ->{
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame_layout,
                            FaqFargment()
                        )
                        .commit()
                    supportActionBar?.title = "Frequently Asked Questions"
                    drawerLayout.closeDrawers()
                }
                R.id.orderhistory ->
                {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame_layout,
                            OrderHistoryFragment()
                        )
                        .commit()
                    supportActionBar?.title = "My Previous Orders"
                    drawerLayout.closeDrawers()
                }
                R.id.loggout ->
                {
                    drawerLayout.closeDrawers()
                    val dialog = AlertDialog.Builder(this@HomePageActivity)
                    dialog.setTitle("Confirmation")
                    dialog.setMessage("Are you sure you want to exit?")
                    dialog.setPositiveButton("YES") { text, listener ->
                        Deletefav(this@HomePageActivity).execute().get()
                        sharedPreferences.edit().putBoolean("isLoggedIn",false).apply()
                        val intent = Intent(this@HomePageActivity,LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    dialog.setNegativeButton("NO") { text, listener ->
                        openAllRestaurants()
                    }
                    dialog.create()
                    dialog.show()
                }
            }
            return@setNavigationItemSelectedListener true
        }
        }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    fun openAllRestaurants()
    {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.frame_layout,
                AllRestuarants()
            )
            .commit()
        supportActionBar?.title = "All Restaurants"
        drawerLayout.closeDrawers()
        navigationView.setCheckedItem(R.id.home)
    }

    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentById(R.id.frame_layout)
        when(frag)
        {
            !is AllRestuarants -> openAllRestaurants()

            else -> super.onBackPressed()
        }
    }
    class Deletefav(val context: Context): AsyncTask<Void, Void, Boolean>()
    {
        override fun doInBackground(vararg params: Void?) : Boolean {
            val db = Room.databaseBuilder(context, RestaurantDatabase::class.java,"restaurants-db").build()

            db.resDao().deleteallfav()
            db.close()

            return true
        }

    }
}
