package com.cooperativa.app.di

import com.cooperativa.app.MainActivity
import com.cooperativa.app.data.di.AppModule
import com.cooperativa.app.data.di.NetworkModule
import com.cooperativa.app.ui.viewmodel.AuthViewModelFactory
import com.cooperativa.app.ui.screens.accounts.viewmodel.AccountsViewModelFactory
import javax.inject.Singleton
import dagger.Component

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class])
interface AppComponent {
    // Método para inyectar las dependencias en la Activity
    fun inject(activity: MainActivity)

    // Provisión para obtener la factory de nuestro ViewModel
    fun authViewModelFactory(): AuthViewModelFactory

    fun AccountsViewModelFactory(): AccountsViewModelFactory

}
