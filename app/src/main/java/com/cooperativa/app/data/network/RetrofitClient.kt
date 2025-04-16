package com.cooperativa.app.data.network

import com.cooperativa.app.data.network.interceptors.TrafficStatsInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class RetrofitClient @Inject constructor(
    private val retrofit: Retrofit
) {
    fun <T> create(service: Class<T>): T = retrofit.create(service)
}