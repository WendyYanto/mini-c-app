package com.dev.wenn.main

import android.app.Application
import android.widget.Toast
import com.dev.core.BuildConfig
import com.dev.core.ComponentHolder
import com.dev.merged.registerDI
import com.dev.wenn.main.di.AppComponent
import com.dev.wenn.main.di.ComponentProvider
import com.dev.wenn.main.di.ComponentsRegistry

class App : Application(), ComponentProvider by ComponentsRegistry {

    override fun onCreate() {
        super.onCreate()
        val appComponent = AppComponent.init(this)
        appComponent.inject(this)
        ComponentHolder.components += appComponent

        registerDI(appComponent)
        if (BuildConfig.useMetro) {
            Toast.makeText(this, "using Metro as DI", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "using Anvil as DI", Toast.LENGTH_SHORT).show()
        }
    }

    private fun registerAnvilDi(
        appComponent: AppComponent
    ) {
//        val mergedComponent = MergedAppComponent.init(appComponent)
//        ComponentHolder.components += mergedComponent
    }

    private fun registerMetroDi(appComponent: AppComponent) {

    }
}