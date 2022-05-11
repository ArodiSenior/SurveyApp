package com.arodi.surveyapp.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog
import com.arodi.surveyapp.R
import com.arodi.surveyapp.base.ViewModelFactory
import com.arodi.surveyapp.databinding.FragmentThirdBinding
import com.arodi.surveyapp.entities.AnswerEntity
import com.arodi.surveyapp.repository.MainRepository
import com.arodi.surveyapp.ui.activities.MainActivity
import com.arodi.surveyapp.utils.Constants
import com.arodi.surveyapp.utils.NetworkState
import com.arodi.surveyapp.viewmodel.MainActivityViewModel
import java.io.ByteArrayOutputStream

class ThirdFragment : Fragment() {

    private lateinit var binding: FragmentThirdBinding
    private var viewModel: MainActivityViewModel? = null
    var sharedPreferences: SharedPreferences? = null
    private var bitmap: Bitmap? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_third, container, false)
        sharedPreferences = activity?.getSharedPreferences(Constants.EMAIL_PREF, AppCompatActivity.MODE_PRIVATE)

        initUI()
        initViewModel()

        return binding.root

    }

    private fun initUI() {
        val repository = MainRepository()
        val viewModelFactory = ViewModelFactory(repository)

        viewModel = ViewModelProvider(this, viewModelFactory).get(MainActivityViewModel::class.java)

        binding.Capture.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (intent.resolveActivity(requireActivity().packageManager) != null) {
                startActivityForResult(intent, 480)
            }
        }

        binding.Previous.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.Finish.setOnClickListener{
            if(validate()){
                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                val imageBytes: ByteArray = byteArrayOutputStream.toByteArray()
                val imageString: String = Base64.encodeToString(imageBytes, 0)
                val name = sharedPreferences!!.getString(Constants.NAME, "")
                val gender = sharedPreferences!!.getString(Constants.GENDER, "")
                val size = binding.Answer.editText!!.text.toString().trim()

                val answerEntity = AnswerEntity(null, name, Constants.NAME_ID, gender, Constants.GENDER_ID, size, Constants.SIZE_ID, imageString)
                activity?.let { viewModel!!.insertAnswer(it, answerEntity) }

                val dialog = SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE)
                dialog.setCancelable(false)
                dialog.setTitle("Thank You!")
                dialog.setConfirmClickListener { sweetAlertDialog ->
                    sweetAlertDialog.dismiss()
                    val intent = Intent(activity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                }
                dialog.show()
            }

        }
    }

    private fun validate(): Boolean{
        var valid = true

        if (binding.Answer.editText!!.text.toString().trim().isEmpty()){
            binding.Answer.error = "Please input farm size"
            valid = false
        }else {
            binding.Answer.error = ""
        }

        if (bitmap == null) {
            Toast.makeText(activity, "Please take a photo", Toast.LENGTH_LONG).show()
            valid = false
        }
        return valid
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 480 && resultCode == AppCompatActivity.RESULT_OK && data != null && data.extras != null) {
            bitmap = data.extras!!["data"] as Bitmap?
            binding.Image.setImageBitmap(bitmap)
        }
    }

    private fun initViewModel() {
        viewModel!!.questionList.observe(viewLifecycleOwner) {
            println("response $it")
            if (it != null){
                binding.Question.text = it.strings!!.en!!.q_size_of_farm
                Constants.NAME_ID = it.questions!![0].id!!
                Constants.GENDER_ID = it.questions!![1].id!!
                Constants.SIZE_ID = it.questions!![2].id!!
            }
        }

        if (NetworkState.getInstance(activity).isConnected) {
            activity?.let { viewModel!!.fetchApiQuestionList(it) }
        }

        activity?.let { viewModel!!.fetchLocalQuestionList(it) }

    }
}