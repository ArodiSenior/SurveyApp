package com.arodi.surveyapp.ui.activities

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.arodi.surveyapp.R
import com.arodi.surveyapp.base.ViewModelFactory
import com.arodi.surveyapp.databinding.ActivityMainBinding
import com.arodi.surveyapp.fragments.FirstFragment
import com.arodi.surveyapp.repository.MainRepository
import com.arodi.surveyapp.utils.NetworkState
import com.arodi.surveyapp.viewmodel.MainActivityViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var viewModel: MainActivityViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setSupportActionBar(binding.toolbar)
        binding.toolbar.setTitleTextAppearance(this, R.style.RobotoBoldTextAppearance)

        initUI()
        submitResponse()

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.FrameLayout, FirstFragment())
        transaction.commit()

        val handler = Handler()
        val delay = 15 * 60  * 1000

        handler.postDelayed(object : Runnable {
            override fun run() {
                submitResponse()
                handler.postDelayed(this, delay.toLong())
            }
        }, delay.toLong())

    }

    private fun submitResponse() {
        if (NetworkState.getInstance(this).isConnected) {
            viewModel!!.submitResponse(this)
        }

    }

    private fun initUI() {
        val repository = MainRepository()
        val viewModelFactory = ViewModelFactory(repository)

        viewModel = ViewModelProvider(this, viewModelFactory).get(MainActivityViewModel::class.java)
    }

        override fun onBackPressed() {
        if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

}