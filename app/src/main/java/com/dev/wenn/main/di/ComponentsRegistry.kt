package com.dev.wenn.main.di

import com.dev.core.di.CoreComponent
import com.dev.wenn.main.App

object ComponentsRegistry : ComponentProvider {

    private lateinit var app: App
    private lateinit var coreComponent: CoreComponent

    fun init(app: App) {
        this.app = app
    }

    override fun getCoreComponent(): CoreComponent {
        if (!::coreComponent.isInitialized) {
            coreComponent = CoreComponent.Initializer.init()
        }
        return coreComponent
    }
}