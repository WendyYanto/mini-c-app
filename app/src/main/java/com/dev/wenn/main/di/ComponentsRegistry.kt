package com.dev.wenn.main.di

import com.dev.core.ComponentHolder
import com.dev.core.di.CoreComponent
import com.dev.data.misc.di.DataMiscComponent
import com.dev.data.order.di.DataOrderComponent
import com.dev.data.order.di.DataOrderComponentProvider
import com.dev.data.product.di.DataProductComponent
import com.dev.data.product.di.DataProductComponentProvider
import com.dev.data.user.di.DataUserComponent
import com.dev.domain.cart.di.DomainCartComponent
import com.dev.domain.cart.di.DomainCartComponentProvider
import com.dev.wenn.main.App

object ComponentsRegistry : ComponentProvider {

    private lateinit var app: App
    private lateinit var coreComponent: CoreComponent
    private lateinit var dataUserComponent: DataUserComponent
    private lateinit var dataProductComponent: DataProductComponent
    private lateinit var dataOrderComponent: DataOrderComponent
    private lateinit var dataMiscComponent: DataMiscComponent
    private lateinit var domainCartComponent: DomainCartComponent

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
        return ComponentHolder.component<DataProductComponentProvider>()
            .getDataProductComponent()
    }

    override fun getDataOrderComponent(): DataOrderComponent {
       return ComponentHolder.component<DataOrderComponentProvider>()
           .getDataOrderComponent()
    }

    override fun getDataMiscComponent(): DataMiscComponent {
        if (!::dataMiscComponent.isInitialized) {
            dataMiscComponent = DataMiscComponent.Initializer.init(app)
        }
        return dataMiscComponent
    }

    override fun getDomainCartComponent(): DomainCartComponent {
        return ComponentHolder.component<DomainCartComponentProvider>()
            .getDomainCartComponent()
    }
}