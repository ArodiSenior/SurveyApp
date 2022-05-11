package com.arodi.surveyapp.ui.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.arodi.surveyapp.R
import com.arodi.surveyapp.base.ViewModelFactory
import com.arodi.surveyapp.databinding.ActivityLoginBinding
import com.arodi.surveyapp.repository.MainRepository
import com.arodi.surveyapp.utils.Constants
import com.arodi.surveyapp.utils.NetworkState
import com.arodi.surveyapp.viewmodel.MainActivityViewModel


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var viewModel: MainActivityViewModel? = null
    var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        val repository = MainRepository()
        val viewModelFactory = ViewModelFactory(repository)
        sharedPreferences = getSharedPreferences(Constants.EMAIL_PREF, MODE_PRIVATE)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainActivityViewModel::class.java)


        binding.Button.setOnClickListener { v ->
            if (validate()) {
                if (!sharedPreferences!!.contains(Constants.IS_SENT)) {
                    if(NetworkState.getInstance(this).isConnected){
                        val body = "Hello Tech, User with phone number "+binding.Phone.editText!!.text.toString().trim()+
                                " has just logged in to the Survey App."
                        viewModel!!.sendEmail(body)
                        val editor: SharedPreferences.Editor = sharedPreferences!!.edit()
                        editor.putBoolean(Constants.IS_SENT, true)
                        editor.apply()
                    }
                }

                startActivity(Intent(this, MainActivity::class.java))
            }
        }
    }


    private fun validate(): Boolean {
        var valid = true
        if (binding.Phone.editText!!.text.toString().trim().isEmpty() ||
            !binding.Phone.editText!!.text.toString().trim().startsWith("+254") ||
            binding.Phone.editText!!.text.toString().trim().length != 13) {

            binding.Phone.error = "Please input valid phone number"
            valid = false
        } else {
            binding.Phone.error = ""
        }
        if (binding.Password.editText!!.text.toString().trim().isEmpty() ||
            binding.Password.editText!!.text.toString().trim() != "1234GYD%$") {

            binding.Password.error = "Please input valid password"
            valid = false
        } else {
            binding.Password.error = ""
        }
        return valid
    }
}