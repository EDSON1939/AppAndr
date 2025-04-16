package com.cooperativa.app.data.network.interceptors

import android.net.TrafficStats
import okhttp3.Interceptor
import okhttp3.Response

class TrafficStatsInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        TrafficStats.setThreadStatsTag(1001) // Tag para estadísticas de red
        return chain.proceed(chain.request())
    }
}