package com.arodi.surveyapp.retrofit

import com.arodi.surveyapp.entities.AnswerEntity
import com.arodi.surveyapp.models.QuestionModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface RetrofitInterface {

    @GET("d628facc-ec18-431d-a8fc-9c096e00709a")
    suspend fun getQuestionList(): QuestionModel

    @POST("send_mail.php")
    suspend fun sendMail(
        @Query("subject") subject: String?,
        @Query("to") to: String?,
        @Query("body") body: String?,
        @Query("from") from: String?
    )

    @POST("submit_answers")
    suspend fun submitResponse(@Body answers: List<AnswerEntity>): Response<AnswerEntity>
}