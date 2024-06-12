package com.dev.wenn.main

import android.app.Application
import com.dev.wenn.main.di.AppComponent
import com.dev.wenn.main.di.ComponentProvider
import com.dev.wenn.main.di.ComponentsRegistry

class App : Application(), ComponentProvider by ComponentsRegistry {

    override fun onCreate() {
        super.onCreate()
        AppComponent.Initializer.init(this)
    }
}