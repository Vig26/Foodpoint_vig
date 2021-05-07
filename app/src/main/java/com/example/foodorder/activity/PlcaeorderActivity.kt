package com.example.foodorder.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.foodorder.R
import com.example.foodorder.fargment.AllRestuarants

class PlcaeorderActivity : AppCompatActivity() {
    lateinit var btnok : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plcaeorder)

        btnok = findViewById(R.id.btnok)
        btnok.setOnClickListener {
            val intent = Intent(this@PlcaeorderActivity,HomePageActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }
    }
}