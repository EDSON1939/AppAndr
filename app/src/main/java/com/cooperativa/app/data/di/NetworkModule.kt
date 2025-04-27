package com.cooperativa.app.data.di

import com.cooperativa.app.data.network.interceptors.TrafficStatsInterceptor
import com.cooperativa.app.data.network.services.AccountsService
import com.cooperativa.app.data.network.services.AuthService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
object NetworkModule {
    private const val BASE_URL = "http://192.168.0.6:5001/"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(TrafficStatsInterceptor())
        .addInterceptor { chain ->
            chain.proceed(
                chain.request()
                    .newBuilder()
                    .addHeader("Accept", "application/json")
                    .build()
            )
        }
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)

    // Añade este nuevo método
    @Provides
    @Singleton
    fun provideAccountsService(retrofit: Retrofit): AccountsService =
        retrofit.create(AccountsService::class.java)
}