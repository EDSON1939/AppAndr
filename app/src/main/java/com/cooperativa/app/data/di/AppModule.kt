package com.cooperativa.app.data.di

import android.app.Application
import com.cooperativa.app.data.managers.TokenManager
import com.cooperativa.app.data.network.services.AuthService
import com.cooperativa.app.data.network.services.AuthServiceImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val app: Application) {

    @Provides
    @Singleton
    fun provideTokenManager(): TokenManager {
        return TokenManager(app)
    }

    @Provides
    @Singleton
    fun provideAuthServiceImpl(api: AuthService, tokenManager: TokenManager): AuthServiceImpl {
        return AuthServiceImpl(api, tokenManager)
    }
}
