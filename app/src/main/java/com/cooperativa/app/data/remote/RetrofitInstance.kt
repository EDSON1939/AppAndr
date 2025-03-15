package com.cooperativa.app.data.remote


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.net.TrafficStats


// 🚀 Interceptor para etiquetar sockets con TrafficStats
class TrafficStatsInterceptor : okhttp3.Interceptor {
    override fun intercept(chain: okhttp3.Interceptor.Chain): okhttp3.Response {
        val tag = 1001 // Puedes usar cualquier número
        TrafficStats.setThreadStatsTag(tag)
        return chain.proceed(chain.request())
    }
}

object RetrofitInstance {
    private const val BASE_URL = "http://192.168.0.4:5001/"

    val api: ApiService by lazy { // <-- ASEGÚRATE DE QUE SEA ApiService
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                okhttp3.OkHttpClient.Builder()
                    .addInterceptor(TrafficStatsInterceptor()) // ✅ Agrega el interceptor
                    .build()
            )
            .build()
            .create(ApiService::class.java)
    }
}
