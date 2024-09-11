package com.dev.wenn.main.di

import com.dev.core.di.CoreComponent
import com.dev.data.order.di.DataOrderComponent
import com.dev.data.product.di.DataProductComponent
import com.dev.domain.cart.di.DomainCartComponent
import com.dev.feature_cart_app.App

object ComponentsRegistry : ComponentProvider {

    private lateinit var app: App
    private lateinit var coreComponent: CoreComponent
    private lateinit var dataProductComponent: DataProductComponent
    private lateinit var dataOrderComponent: DataOrderComponent
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

    override fun getDomainCartComponent(): DomainCartComponent {
        if (!::domainCartComponent.isInitialized) {
            domainCartComponent = DomainCartComponent.Initializer.init(app)
        }
        return domainCartComponent
    }
}