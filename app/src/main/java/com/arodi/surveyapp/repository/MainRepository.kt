package com.arodi.surveyapp.repository

import android.content.Context
import android.location.Location
import com.arodi.surveyapp.database.DatabaseClient
import com.arodi.surveyapp.entities.AnswerEntity
import com.arodi.surveyapp.entities.QuestionEntity
import com.arodi.surveyapp.models.QuestionModel
import com.arodi.surveyapp.retrofit.RetrofitInstance
import com.arodi.surveyapp.retrofit.RetrofitInterface
import com.arodi.surveyapp.utils.Constants
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.http.Query
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList


class MainRepository {
    private val retrofitInstance = RetrofitInstance.getRetrofitInstance()
    private val retrofitInterface = retrofitInstance.create(RetrofitInterface::class.java)

    private val emailInstance = RetrofitInstance.sendEmailInstance()
    private val emailInterface = emailInstance.create(RetrofitInterface::class.java)

    suspend fun getQuestionList(): QuestionModel {
        return retrofitInterface.getQuestionList()
    }

    suspend fun submitResponse(answers: List<AnswerEntity>) {
        try {
            emailInterface.submitResponse(answers)
        }catch (e: Exception){
            println(e.message)
        }
    }

    suspend fun sendEmail(body: String) {
        try {
            emailInterface.sendMail(Constants.SUBJECT, Constants.TO, body, Constants.FROM)
        }catch (e: Exception){
            println(e.message)
        }
    }

    fun fetchLocalQuestionList(context: Context): QuestionModel{
        val questionEntity = DatabaseClient.getInstance(context).database.questionDao().fetchAll()
        val listType = object : TypeToken<QuestionModel?>() {}.type

        return  Gson().fromJson(questionEntity.questionModel!!, listType)
    }

    fun insertQuestionList(context: Context, questionModel: QuestionModel){
        val entity = QuestionEntity(null, Gson().toJson(questionModel))
        DatabaseClient.getInstance(context).database.questionDao().insert(entity)
    }

    fun insertAnswer(context: Context, answerEntity: AnswerEntity){
        DatabaseClient.getInstance(context).database.answerDao().insert(answerEntity)
    }

    fun getAnswers(context: Context): List<AnswerEntity>{
        return DatabaseClient.getInstance(context).database.answerDao().fetchAll()
    }

    fun checkQuestionListExist(context: Context): Boolean{
        return DatabaseClient.getInstance(context).database.questionDao().isExists()
    }
}