package com.example.foodorder.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.foodorder.R
import com.example.foodorder.util.ConnectionManager
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    lateinit var editmobilenum: EditText
    lateinit var editpassword: EditText
    lateinit var txtforgotpass: TextView
    lateinit var txtregister: TextView
    lateinit var btnlogin: Button
    lateinit var sharedPreferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPreferences = getSharedPreferences(getString(R.string.Preference_file_name), Context.MODE_PRIVATE)

        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn",false)

        if(isLoggedIn)
        {
            val intent = Intent(this@LoginActivity,HomePageActivity::class.java)
            startActivity(intent)
            finish()
        }

        editmobilenum = findViewById(R.id.edittxtmobilenum)
        editpassword = findViewById(R.id.edittxtpasword)
        txtforgotpass = findViewById(R.id.forgotpass)
        txtregister = findViewById(R.id.register)
        btnlogin = findViewById(R.id.btblogin)
        val number = editmobilenum.text
        val password = editpassword.text
        val queue = Volley.newRequestQueue(this@LoginActivity)
        val url = "http://13.235.250.119/v2/login/fetch_result"
        val jsonparams = JSONObject()
        jsonparams.put("mobile_number", number)
        jsonparams.put("password", password)

        btnlogin.setOnClickListener {
            if (ConnectionManager().checkConnectivity(this@LoginActivity)) {
                val jsonObjectRequest =
                    object : JsonObjectRequest(Method.POST, url,jsonparams, Response.Listener {
                        try {
                            val data = it.getJSONObject("data")
                            val Success = data.getBoolean("success")
                            val info = data.getJSONObject("data")
                            if (Success) {
                                val intent = Intent(this@LoginActivity,HomePageActivity::class.java)
                                startActivity(intent)
                                finish()
                                val name = info.getString("name")
                                val mobnum = info.getString("mobile_number")
                                val email = info.getString("email")
                                val user_id = info.getString("user_id")
                                val address = info.getString("address")
                                sharedPreferences.edit().putString("user_id",user_id).apply()
                                sharedPreferences.edit().putString("name",name).apply()
                                sharedPreferences.edit().putString("mobile_number",mobnum).apply()
                                sharedPreferences.edit().putString("email",email).apply()
                                sharedPreferences.edit().putString("address",address).apply()
                                sharedPreferences.edit().putBoolean("isLoggedIn",true).apply()
                            } else {
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Incorrect Number or Password",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(
                                this@LoginActivity,
                                "Some Unexpected Error Occur ",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }, Response.ErrorListener {
                        Toast.makeText(
                            this@LoginActivity,
                            "Some Volley Error Occur $it",
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
            } else {
                Toast.makeText(this@LoginActivity, "Incorrect Number or Password", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        txtregister.setOnClickListener{
            val intent = Intent(this@LoginActivity,RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
        txtforgotpass.setOnClickListener {
            val intent = Intent(this@LoginActivity,ForgotPasswordActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}
