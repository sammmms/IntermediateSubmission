package com.example.intermediatesubmission.data.retrofit

import android.content.Context
import android.util.Log
import com.example.intermediatesubmission.util.SharedPreferences
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(context: Context) : Interceptor {
    private val sharedPreferences = SharedPreferences(context)

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequest = request.newBuilder()
            .addHeader("Authorization", "Bearer ${sharedPreferences.getToken()}")
            .build()

        Log.d("Interceptor", "Authorization : Bearer ${sharedPreferences.getToken()}")
        return chain.proceed(newRequest)
    }
}