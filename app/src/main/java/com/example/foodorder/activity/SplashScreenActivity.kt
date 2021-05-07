package com.example.foodorder.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.example.foodorder.R


class SplashScreenActivity : AppCompatActivity() {


    lateinit var slide : Animation
    lateinit var imgview : ImageView
    lateinit var txtview : TextView
    private val SPLASH_TIME = 2500L

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash_screen)

        imgview = findViewById(R.id.imgviewsplash)
        txtview = findViewById(R.id.txtviewsplash)
        slide = AnimationUtils.loadAnimation(this,
            R.anim.slide_animation
        )

        imgview.animation = slide
        txtview.animation = slide

        android.os.Handler().postDelayed(
            {
                val intent = Intent(this@SplashScreenActivity,
                    LoginActivity::class.java)
                startActivity(intent)
                finish()
            },SPLASH_TIME)
    }
}