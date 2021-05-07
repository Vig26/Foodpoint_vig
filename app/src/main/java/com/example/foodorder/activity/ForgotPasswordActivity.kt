package com.example.foodorder.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.foodorder.R
import org.json.JSONObject

class ForgotPasswordActivity : AppCompatActivity() {

    lateinit var txtmobilenum: EditText
    lateinit var txtEmailId: EditText
    lateinit var btnNext: Button
    lateinit var forgottoolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        txtmobilenum = findViewById(R.id.forgotxtmobilereg)
        txtEmailId = findViewById(R.id.forgottxtemail)
        btnNext = findViewById(R.id.btnNext)
        forgottoolbar = findViewById(R.id.forgottoolbar)

        setSupportActionBar(forgottoolbar)
        supportActionBar?.title = "Forgot Password"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        forgottoolbar.setNavigationOnClickListener { onBackPressed() }

        val queue = Volley.newRequestQueue(this@ForgotPasswordActivity)
        val url = "http://13.235.250.119/v2/forgot_password/fetch_result"

        val jsonParams = JSONObject()
        jsonParams.put("mobile_number", txtmobilenum.text)
        jsonParams.put("email", txtEmailId.text)

        btnNext.setOnClickListener {
            if((!Patterns.EMAIL_ADDRESS.matcher(txtEmailId.text).matches())||(txtmobilenum.text.toString().length != 10))
            {
                if (!Patterns.EMAIL_ADDRESS.matcher(txtEmailId.text).matches())
                {
                    txtEmailId.error = "Invalid Email"
                }
                else
                {
                    txtmobilenum.error = "Invalid Mobile Number"
                }
            }
            val jsonObjectRequest =
                object : JsonObjectRequest(Method.POST, url, jsonParams, Response.Listener {
                    try {
                        val data = it.getJSONObject("data")
                        val success = data.getBoolean("success")
                        val firstTry = data.getBoolean("first_try")
                        if (success) {
                            val intent = Intent(this@ForgotPasswordActivity, ResetPasswordActivity::class.java)
                            intent.putExtra("mobile_number",txtmobilenum.text.toString())
                            intent.putExtra("firsttry",firstTry)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(
                                this@ForgotPasswordActivity,
                                "OTP is already sent to Registered Email Check your Inbox",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(
                            this@ForgotPasswordActivity,
                            "Check yor Email and Mobile Number",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }, Response.ErrorListener {
                    Toast.makeText(
                        this@ForgotPasswordActivity,
                        "Some Unexpected error $it",
                        Toast.LENGTH_SHORT
                    ).show()
                }) {

                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application-type"
                        headers["token"] = "f447a4b9862de8"
                        return headers
                    }
                }
            queue.add(jsonObjectRequest)
        }
    }
    override fun onBackPressed() {
        val intent = Intent(this@ForgotPasswordActivity,LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}