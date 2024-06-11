package com.dev.wenn.main.di

import com.dev.core.di.CoreComponent
import com.dev.data.user.di.DataUserComponent
import com.dev.wenn.main.App

object ComponentsRegistry : ComponentProvider {

    private lateinit var app: App
    private lateinit var coreComponent: CoreComponent
    private lateinit var dataUserComponent: DataUserComponent

    fun init(app: App) {
        this.app = app
    }

    override fun getCoreComponent(): CoreComponent {
        if (!::coreComponent.isInitialized) {
            coreComponent = CoreComponent.Initializer.init()
        }
        return coreComponent
    }

    override fun getDataUserComponent(): DataUserComponent {
        if (!::dataUserComponent.isInitialized) {
            dataUserComponent = DataUserComponent.Initializer.init(app)
        }
        return dataUserComponent
    }
}