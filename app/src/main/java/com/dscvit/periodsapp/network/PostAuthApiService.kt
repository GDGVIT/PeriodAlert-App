package com.dscvit.periodsapp.network

import android.content.Context
import com.dscvit.periodsapp.utils.Constants
import com.dscvit.periodsapp.utils.PreferenceHelper
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object PostAuthApiService {

    fun createRetrofit(context: Context): ApiInterface {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getOkHttpClient(context))
            .build()

        return retrofit.create(ApiInterface::class.java)
    }

    private fun getOkHttpClient(context: Context): OkHttpClient {
        val sharedPreferences = PreferenceHelper.customPrefs(context, Constants.PREF_NAME)

        val httpClient = OkHttpClient.Builder()

        httpClient.addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .addHeader(
                    "Content-Type",
                    "application/json"
                )
                .addHeader(
                    "Authorization",
                    "Token ${sharedPreferences.getString(Constants.PREF_AUTH_KEY, "")}"
                )
            val request = requestBuilder.build()
            return@addInterceptor chain.proceed(request)
        }
        return httpClient.build()
    }
}