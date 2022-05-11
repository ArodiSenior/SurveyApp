package com.arodi.surveyapp.fragments

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.arodi.surveyapp.R
import com.arodi.surveyapp.base.ViewModelFactory
import com.arodi.surveyapp.databinding.FragmentFirstBinding
import com.arodi.surveyapp.repository.MainRepository
import com.arodi.surveyapp.utils.Constants
import com.arodi.surveyapp.utils.NetworkState
import com.arodi.surveyapp.viewmodel.MainActivityViewModel

class FirstFragment : Fragment() {

    private lateinit var binding: FragmentFirstBinding
    private var viewModel: MainActivityViewModel? = null
    var sharedPreferences: SharedPreferences? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_first, container, false)
        sharedPreferences = activity?.getSharedPreferences(Constants.EMAIL_PREF, AppCompatActivity.MODE_PRIVATE)

        initUI()
        initViewModel()

        return binding.root

    }

    private fun initUI() {
        val repository = MainRepository()
        val viewModelFactory = ViewModelFactory(repository)

        viewModel = ViewModelProvider(this, viewModelFactory).get(MainActivityViewModel::class.java)

        binding.Next.setOnClickListener {
            if (binding.Answer.editText!!.text.toString().trim().isEmpty()){
                binding.Answer.error = "Please input farmer name"
            }else{
                binding.Answer.error = ""

                val editor = sharedPreferences?.edit()
                editor?.putString(Constants.NAME, binding.Answer.editText!!.text.toString().trim())
                editor?.apply()

                val transaction = activity?.supportFragmentManager?.beginTransaction()
                transaction?.replace(R.id.FrameLayout, SecondFragment())
                transaction?.addToBackStack("fragment_two")
                transaction?.commit()
            }

        }

    }

    private fun initViewModel() {
        viewModel!!.questionList.observe(viewLifecycleOwner) {
            println("response $it")
            if (it != null){
                binding.Question.text = it.strings!!.en!!.q_farmer_name
            }
        }

        if (NetworkState.getInstance(activity).isConnected) {
            activity?.let { viewModel!!.fetchApiQuestionList(it) }
        }

        activity?.let { viewModel!!.fetchLocalQuestionList(it) }

    }

}