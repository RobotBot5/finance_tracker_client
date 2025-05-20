package com.robotbot.finance_tracker_client.remote

import android.annotation.SuppressLint
import com.google.gson.GsonBuilder
import com.robotbot.finance_tracker_client.remote.token.AuthInterceptor
import com.robotbot.finance_tracker_client.remote.util.LocalDateTypeAdapter
import com.robotbot.finance_tracker_client.remote.util.PROD_BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import javax.inject.Inject

internal class ApiFactory @Inject constructor(
    authInterceptor: AuthInterceptor
) {

    @SuppressLint("NewApi")
    private val gson = GsonBuilder()
        .registerTypeAdapter(LocalDate::class.java, LocalDateTypeAdapter())
        .create()

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }).build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(PROD_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient)
        .build()
}
