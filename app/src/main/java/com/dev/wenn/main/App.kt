package com.dev.wenn.main

import android.app.Application
import com.dev.core.ComponentHolder
import com.dev.wenn.main.di.AppComponent
import com.dev.wenn.main.di.ComponentProvider
import com.dev.wenn.main.di.ComponentsRegistry
import com.dev.wenn.main.di.MetroAppComponent
import dev.zacsweers.metro.createGraphFactory

class App : Application(), ComponentProvider by ComponentsRegistry {

    override fun onCreate() {
        super.onCreate()
        val appComponent = AppComponent.init(this)
        appComponent.inject(this)
        ComponentHolder.components += appComponent

//        registerMetroDi(appComponent)
    }

    private fun registerMetroDi(appComponent: AppComponent) {
        ComponentHolder.components += createGraphFactory<MetroAppComponent.Factory>()
            .create(appComponent)
    }
}