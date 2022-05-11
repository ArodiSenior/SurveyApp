package com.arodi.surveyapp.retrofit

import com.arodi.surveyapp.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    companion object {
        fun getRetrofitInstance(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        fun sendEmailInstance(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(Constants.EMAIL_URL+Constants.DIR)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}