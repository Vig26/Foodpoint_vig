package com.example.foodorder.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.foodorder.R
import org.json.JSONObject

class ResetPasswordActivity : AppCompatActivity() {

    lateinit var txtOTP : EditText
    lateinit var txtnewPass : EditText
    lateinit var txtConfrimpass : EditText
    lateinit var btnSubmit : Button
    lateinit var resettoolbar : Toolbar
    lateinit var sharedPreferences: SharedPreferences
    lateinit var progressbarLayout : RelativeLayout
    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password_activtiy)


        sharedPreferences = getSharedPreferences(getString(R.string.Preference_file_name), Context.MODE_PRIVATE)

        txtOTP = findViewById(R.id.otpedittxt)
        txtnewPass = findViewById(R.id.newpassedit)
        txtConfrimpass = findViewById(R.id.confrimpassedit)
        btnSubmit = findViewById(R.id.btnSubmit)
        resettoolbar = findViewById(R.id.resettoolbar)


        setSupportActionBar(resettoolbar)
        supportActionBar?.title = "Reset Password"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        resettoolbar.setNavigationOnClickListener{onBackPressed()}
        val firstTry = intent.getBooleanExtra("firsttry",false)
        if(firstTry)
        {

            val dialog = AlertDialog.Builder(this@ResetPasswordActivity)
            dialog.setTitle("Error")
            dialog.setMessage("Otp is sent to the Registered Email")
            dialog.setNeutralButton("Ok") {text,listener ->

            }
            dialog.create()
            dialog.show()
        }
        else
        {
            val dialog = AlertDialog.Builder(this@ResetPasswordActivity)
            dialog.setTitle("Error")
            dialog.setMessage("Prefer the previous sent email for OTP")
            dialog.setNeutralButton("Ok") {text,listener ->

            }
            dialog.create()
            dialog.show()
        }

    val mobileNumber = intent.getStringExtra("mobile_number")

        val queue = Volley.newRequestQueue(this@ResetPasswordActivity)
        val url = "http://13.235.250.119/v2/reset_password/fetch_result"

        val jsonParams = JSONObject()
        jsonParams.put("mobile_number",mobileNumber)
        jsonParams.put("otp",txtOTP.text)


        btnSubmit.setOnClickListener {
            if(txtnewPass.text.toString() == txtConfrimpass.text.toString())
            {
                jsonParams.put("password",txtConfrimpass.text)
            }
            else
            {
                txtnewPass.error = "Check password"
                txtConfrimpass.error = "Check Password"
            }
            val jsonObjectRequest = object :JsonObjectRequest(Method.POST,url,jsonParams,Response.Listener {
                try{
                    val data = it.getJSONObject("data")
                    val success = data.getBoolean("success")
                    if(success)
                    {
                        val successmsg = data.getString("successMessage")
                        Toast.makeText(this@ResetPasswordActivity,successmsg,Toast.LENGTH_SHORT).show()
                        sharedPreferences.edit().putBoolean("isLoggedIn",false).apply()
                        val intent = Intent(this@ResetPasswordActivity,LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else
                    {
                        Toast.makeText(this@ResetPasswordActivity,"Entered OTP is wrong",Toast.LENGTH_SHORT).show()
                    }
                }
                catch (e:Exception)
                {
                    Toast.makeText(this@ResetPasswordActivity,"some error $it",Toast.LENGTH_SHORT).show()
                }
            },Response.ErrorListener {
                Toast.makeText(this@ResetPasswordActivity,"Some unexpected error has Occur",Toast.LENGTH_SHORT).show()
            })
            {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String , String>()
                    headers["Content-type"] = "application/type"
                    headers["token"] = "f447a4b9862de8"
                    return headers
                }
            }
            queue.add(jsonObjectRequest)
        }
    }
    override fun onBackPressed()
    {
        val intent = Intent(this@ResetPasswordActivity,LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

}