package com.example.foodorder.fargment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.foodorder.R
import java.util.jar.Attributes

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var profname : TextView
    lateinit var profnumber : TextView
    lateinit var profemail : TextView
    lateinit var profaddress : TextView

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
        val view = inflater.inflate(R.layout.fragment_my_profile, container, false)
        val sharedPreferences =activity?.getSharedPreferences(getString(R.string.Preference_file_name), Context.MODE_PRIVATE)
        profname = view.findViewById(R.id.txtnameprof)
        profnumber = view.findViewById(R.id.txtmobnumprof)
        profemail = view.findViewById(R.id.txtemailprof)
        profaddress = view.findViewById(R.id.txtaddressprof)

        val name = sharedPreferences?.getString("name", "Name")
        val mobnum = sharedPreferences?.getString("mobile_number","Mobile Number")
        val email = sharedPreferences?.getString("email","Email")
        val address = sharedPreferences?.getString("address","Delivery address")
        profname.text = name
        profemail.text = email
        profnumber.text = mobnum
        profaddress.text = address
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MyProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}