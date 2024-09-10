package com.dev.wenn.main

import android.app.Application
import android.widget.Toast
import com.dev.core.DynamicTextProvider
import com.dev.wenn.main.di.AppComponent
import com.dev.wenn.main.di.ComponentProvider
import com.dev.wenn.main.di.ComponentsRegistry
import javax.inject.Inject

class App : Application(), ComponentProvider by ComponentsRegistry {

    @Inject
    lateinit var dynamicTextProvider: DynamicTextProvider

    override fun onCreate() {
        super.onCreate()
        AppComponent.init(this).inject(this)
        Toast.makeText(this, "Hi: ${dynamicTextProvider.loadText()}", Toast.LENGTH_SHORT).show()
    }
}