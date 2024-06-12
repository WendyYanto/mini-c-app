package com.dev.wenn.main.di

import com.dev.core.di.CoreComponent
import com.dev.data.misc.di.DataMiscComponent
import com.dev.data.order.di.DataOrderComponent
import com.dev.data.product.di.DataProductComponent
import com.dev.data.user.di.DataUserComponent
import com.dev.wenn.main.App

object ComponentsRegistry : ComponentProvider {

    private lateinit var app: App
    private lateinit var coreComponent: CoreComponent
    private lateinit var dataUserComponent: DataUserComponent
    private lateinit var dataProductComponent: DataProductComponent
    private lateinit var dataOrderComponent: DataOrderComponent
    private lateinit var dataMiscComponent: DataMiscComponent

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

    override fun getDataProductComponent(): DataProductComponent {
        if (!::dataProductComponent.isInitialized) {
            dataProductComponent = DataProductComponent.Initializer.init(app)
        }
        return dataProductComponent
    }

    override fun getDataOrderComponent(): DataOrderComponent {
        if (!::dataOrderComponent.isInitialized) {
            dataOrderComponent = DataOrderComponent.Initializer.init(app)
        }
        return dataOrderComponent
    }

    override fun getDataMiscComponent(): DataMiscComponent {
        if (!::dataMiscComponent.isInitialized) {
            dataMiscComponent = DataMiscComponent.Initializer.init(app)
        }
        return dataMiscComponent
    }
}