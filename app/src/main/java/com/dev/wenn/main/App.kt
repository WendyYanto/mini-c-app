package com.dev.wenn.main

import android.app.Application
import android.widget.Toast
import com.dev.core.ComponentHolder
import com.dev.wenn.main.di.AppComponent
import com.dev.wenn.main.di.ComponentProvider
import com.dev.wenn.main.di.ComponentsRegistry
import com.dev.wenn.main.di.MetroAppComponent
import dev.zacsweers.metro.createGraphFactory

class App : Application(), ComponentProvider by ComponentsRegistry {

    override fun onCreate() {
        super.onCreate()

        ComponentsRegistry.init(this)
        val appComponent = AppComponent.Initializer.init(this)

        initMetroApp(appComponent)
    }

    private fun initMetroApp(appComponent: AppComponent) {
        val metroAppComponent = createGraphFactory<MetroAppComponent.Factory>().create(appComponent)
        ComponentHolder.components += metroAppComponent
        val text = metroAppComponent.provideCoreTextProvider().getText()
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}