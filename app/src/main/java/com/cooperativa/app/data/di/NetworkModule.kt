// NetworkModule.kt
package com.cooperativa.app.data.di

import com.cooperativa.app.data.network.services.AccountsService
import com.cooperativa.app.data.network.services.AuthService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
object NetworkModule {
    private const val AUTH_BASE_URL     = "http://192.168.100.32:5001/"
    private const val ACCOUNTS_BASE_URL = "http://192.168.100.32:5002/"

    @Provides @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            // aquí tus interceptores…
            .build()

    @Provides @Singleton @Named("authRetrofit")
    fun provideAuthRetrofit(okHttp: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(AUTH_BASE_URL)
            .client(okHttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides @Singleton @Named("accountsRetrofit")
    fun provideAccountsRetrofit(okHttp: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(ACCOUNTS_BASE_URL)
            .client(okHttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides @Singleton
    fun provideAuthService(
        @Named("authRetrofit") retrofit: Retrofit
    ): AuthService = retrofit.create(AuthService::class.java)

    @Provides @Singleton
    fun provideAccountsService(
        @Named("accountsRetrofit") retrofit: Retrofit
    ): AccountsService = retrofit.create(AccountsService::class.java)
}
