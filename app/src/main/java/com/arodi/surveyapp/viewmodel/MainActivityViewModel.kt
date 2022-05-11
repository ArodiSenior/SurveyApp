package com.arodi.surveyapp.viewmodel

import android.content.Context
import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arodi.surveyapp.R
import com.arodi.surveyapp.entities.AnswerEntity
import com.arodi.surveyapp.models.*
import com.arodi.surveyapp.repository.MainRepository
import com.arodi.surveyapp.utils.Constants
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


class MainActivityViewModel(private val repository: MainRepository) : ViewModel() {
    var questionList: MutableLiveData<QuestionModel?> = MutableLiveData()

    fun fetchApiQuestionList(context: Context) {
        viewModelScope.launch {
            try {
                val questionModel = repository.getQuestionList()
                println("questionModel $questionModel")
                withContext(Dispatchers.IO) {
                    repository.insertQuestionList(context, questionModel)
                }
                fetchLocalQuestionList(context)
            }catch (e: Exception){
                println("exception $e")
            }

        }
    }

    fun insertAnswer(context: Context, answerEntity: AnswerEntity) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    repository.insertAnswer(context, answerEntity)
                }
            }catch (e: Exception){
                println("exception $e")
            }

        }
    }

    fun submitResponse(context: Context) {
        viewModelScope.launch {
            try {
                val answers = withContext(Dispatchers.IO) {
                    repository.getAnswers(context)
                }
                repository.submitResponse(answers)

            }catch (e: Exception){
                println("exception $e")
            }

        }
    }

    fun sendEmail(body: String){
        viewModelScope.launch {
            repository.sendEmail(body);
        }
    }


    fun fetchLocalQuestionList(context: Context) {
        viewModelScope.launch {
            val isExist = withContext(Dispatchers.IO) {
                repository.checkQuestionListExist(context)
            }
            if(isExist){
                val response = withContext(Dispatchers.IO) {
                    repository.fetchLocalQuestionList(context)
                }
                questionList.postValue(response)
            }else{
                questionList.postValue(null)
            }
        }
    }

}
