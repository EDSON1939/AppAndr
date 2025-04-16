package com.cooperativa.app

import android.app.Application
import com.cooperativa.app.data.di.AppModule
import com.cooperativa.app.di.AppComponent
import com.cooperativa.app.di.DaggerAppComponent

class CooperativaApp : Application() {
    // Inicializamos el componente
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            // Si tu módulo necesita el Application, lo proporcionas aquí.
            .appModule(AppModule(this))
            .build()
    }
}
