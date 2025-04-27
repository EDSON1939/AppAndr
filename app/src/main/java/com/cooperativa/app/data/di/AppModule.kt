package com.cooperativa.app.data.di

import com.cooperativa.app.data.managers.DeviceInfoHelper
import android.app.Application
import android.content.Context
import com.cooperativa.app.data.managers.TokenManager
import com.cooperativa.app.data.network.services.AccountsService
import com.cooperativa.app.data.network.services.AccountsServiceImpl
import com.cooperativa.app.data.network.services.AuthService
import com.cooperativa.app.data.network.services.AuthServiceImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val app: Application) {

    @Provides
    @Singleton
    fun provideApplication(): Application = app

    @Provides
    @Singleton
    fun provideContext(): Context = app.applicationContext


    @Provides
    @Singleton
    fun provideTokenManager(context: Context): TokenManager {
        return TokenManager(context)
    }

    @Provides
    @Singleton
    fun provideAuthServiceImpl(api: AuthService, tokenManager: TokenManager): AuthServiceImpl {
        return AuthServiceImpl(api, tokenManager)
    }

    @Provides
    @Singleton
    fun provideAccountServiceImpl(api: AccountsService, tokenManager: TokenManager): AccountsServiceImpl {
        return AccountsServiceImpl(api, tokenManager)
    }

    @Provides
    @Singleton
    fun provideDeviceInfoHelper(context: Context): DeviceInfoHelper {
        return DeviceInfoHelper(context)
    }
}
