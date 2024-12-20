package com.dev.feature_cart_app

import android.app.Application
import android.util.Log
import com.dev.core.ComponentHolder
import com.dev.wenn.main.di.AppComponent
import com.dev.wenn.main.di.ComponentProvider
import com.dev.wenn.main.di.ComponentsRegistry

class App : Application(), ComponentProvider by ComponentsRegistry {

//    @Inject
//    lateinit var dynamicTextProvider: DynamicTextProvider

    override fun onCreate() {
        super.onCreate()
        val appComponent = AppComponent.init(this)
        appComponent.inject(this)
        ComponentHolder.components += appComponent
    }
}