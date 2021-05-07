package com.example.foodorder.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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

class RegisterActivity : AppCompatActivity() {

    lateinit var edittxtname : EditText
    lateinit var edittxtemail : EditText
    lateinit var edittxtmobilenumreg : EditText
    lateinit var edittxtaddress : EditText
    lateinit var edittxtpassreg : EditText
    lateinit var edittxtconfirmpass : EditText
    lateinit var btnregister : Button
    lateinit var toolbarreg : Toolbar
    lateinit var  sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        sharedPreferences = getSharedPreferences(getString(R.string.Preference_file_name), Context.MODE_PRIVATE)



        edittxtname = findViewById(R.id.edittxtname)
        edittxtemail = findViewById(R.id.edittxtemail)
        edittxtmobilenumreg = findViewById(R.id.edittxtmobilereg)
        edittxtaddress = findViewById(R.id.edittxtdeliveryadd)
        edittxtpassreg = findViewById(R.id.edittxtpasswordreg)
        edittxtconfirmpass = findViewById(R.id.edittxtconfirmpassreg)
        btnregister = findViewById(R.id.btbregister)
        toolbarreg = findViewById(R.id.registertoolbar)

        fun isvalidEmailId(email : String):Boolean
        {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
        fun addErrorIcon(text: EditText,errormsg : String)
        {
            text.error = errormsg

        }


        setSupportActionBar(toolbarreg)
        supportActionBar?.title = "Register Yourself"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        toolbarreg.setNavigationOnClickListener{onBackPressed()}

        btnregister.setOnClickListener {

            if(edittxtname.text.toString().isEmpty())
            {
                addErrorIcon(edittxtname,"Invalid Name")
            }
            if(edittxtaddress.text.toString().isEmpty())
            {
                addErrorIcon(edittxtaddress,"Invalid Address")
            }
            if(edittxtmobilenumreg.text.toString().length != 10)
            {
                addErrorIcon(edittxtmobilenumreg,"Must be 10Chars")
            }

            if(isvalidEmailId(edittxtemail.text.toString()))
            {
                val emailId = edittxtemail.text.toString()
            }
            else
            {
                addErrorIcon(edittxtemail,"Invalid Email")
            }
            if(edittxtpassreg.text.toString() == edittxtconfirmpass.text.toString())
            {
                val pass = edittxtpassreg.text.toString()
            }
            else
            {
                addErrorIcon(edittxtpassreg,"Password don't Match")
                addErrorIcon(edittxtconfirmpass,"Password don't Match")
            }

            val queue = Volley.newRequestQueue(this@RegisterActivity)
            val url = "http://13.235.250.119/v2/register/fetch_result"

            val jsonparams = JSONObject()
            jsonparams.put("name",edittxtname.text)
            jsonparams.put("email",edittxtemail.text)
            jsonparams.put("mobile_number",edittxtmobilenumreg.text)
            jsonparams.put("address",edittxtaddress.text)
            jsonparams.put("password",edittxtpassreg.text)


            val jsonObjectRequest = object :JsonObjectRequest(Method.POST,url,jsonparams,Response.Listener {

                try {
                    val data = it.getJSONObject("data")
                    val success = data.getBoolean("success")
                    if(success)
                    {
                        savePreferences(edittxtname,edittxtemail,edittxtmobilenumreg,edittxtaddress)
                        sharedPreferences.edit().putBoolean("isLoggedIn",true).apply()
                        val intent = Intent(this@RegisterActivity,HomePageActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else
                    {
                        Toast.makeText(this@RegisterActivity,"This email is already Registered",Toast.LENGTH_SHORT).show()
                    }
                }
                catch (e:Exception)
                {
                    Toast.makeText(this@RegisterActivity,"Some Error has Occur",Toast.LENGTH_SHORT).show()
                }
            },Response.ErrorListener {
                Toast.makeText(this@RegisterActivity,"Unexpected Error has Occur",Toast.LENGTH_SHORT).show()
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
    }
    fun savePreferences(name:EditText,email:EditText,mobnum:EditText,address:EditText)
    {
        sharedPreferences.edit().putString("name",name.text.toString()).apply()
        sharedPreferences.edit().putString("email",email.text.toString()).apply()
        sharedPreferences.edit().putString("mobile_number",mobnum.text.toString()).apply()
        sharedPreferences.edit().putString("address",address.text.toString()).apply()
    }
    override fun onBackPressed() {
        val intent = Intent(this@RegisterActivity,LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}