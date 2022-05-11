package com.arodi.surveyapp.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.arodi.surveyapp.R
import com.arodi.surveyapp.base.ViewModelFactory
import com.arodi.surveyapp.databinding.FragmentSecondBinding
import com.arodi.surveyapp.repository.MainRepository
import com.arodi.surveyapp.utils.Constants
import com.arodi.surveyapp.utils.NetworkState
import com.arodi.surveyapp.viewmodel.MainActivityViewModel


class SecondFragment : Fragment() {

    private lateinit var binding: FragmentSecondBinding
    private var viewModel: MainActivityViewModel? = null
    private var SELECTED_GENDER: String? = null
    var sharedPreferences: SharedPreferences? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_second, container, false)
        sharedPreferences = activity?.getSharedPreferences(Constants.EMAIL_PREF, AppCompatActivity.MODE_PRIVATE)

        initUI()
        initViewModel()

        return binding.root

    }

    private fun initUI() {
        val repository = MainRepository()
        val viewModelFactory = ViewModelFactory(repository)

        viewModel = ViewModelProvider(this, viewModelFactory).get(MainActivityViewModel::class.java)

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.Male -> {SELECTED_GENDER = "MALE"}
                R.id.Female -> {SELECTED_GENDER = "FEMALE"}
                R.id.Other -> {SELECTED_GENDER = "OTHER"}
            }
        }
        binding.Next.setOnClickListener {
            if (SELECTED_GENDER == null){
                Toast.makeText(context, "Please select farmer gender", Toast.LENGTH_SHORT).show()
            }else{
                val editor = sharedPreferences?.edit()
                editor?.putString(Constants.GENDER, SELECTED_GENDER)
                editor?.apply()

                val transaction = activity?.supportFragmentManager?.beginTransaction()
                transaction?.replace(R.id.FrameLayout, ThirdFragment())
                transaction?.addToBackStack("fragment_three")
                transaction?.commit()
            }

        }

        binding.Previous.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun initViewModel() {
        viewModel!!.questionList.observe(viewLifecycleOwner) {
            println("response $it")
            if (it != null){
                binding.Question.text = it.strings!!.en!!.q_farmer_gender
            }
        }

        if (NetworkState.getInstance(activity).isConnected) {
            activity?.let { viewModel!!.fetchApiQuestionList(it) }
        }

        activity?.let { viewModel!!.fetchLocalQuestionList(it) }

    }

}