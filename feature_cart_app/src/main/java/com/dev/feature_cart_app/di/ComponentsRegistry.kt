package com.dev.wenn.main.di

import com.dev.core.ComponentHolder
import com.dev.core.di.CoreComponent
import com.dev.data.order.di.DataOrderComponent
import com.dev.data.order.di.DataOrderComponentProvider
import com.dev.data.product.di.DataProductComponent
import com.dev.data.product.di.DataProductComponentProvider
import com.dev.domain.cart.di.DomainCartComponent
import com.dev.domain.cart.di.DomainCartComponentProvider
import com.dev.feature_cart_app.App

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

    override fun getDataProductComponent(): DataProductComponent {
        return ComponentHolder.component<DataProductComponentProvider>()
            .getDataProductComponent()
    }

    override fun getDataOrderComponent(): DataOrderComponent {
        return ComponentHolder.component<DataOrderComponentProvider>()
            .getDataOrderComponent()
    }

    override fun getDomainCartComponent(): DomainCartComponent {
        return ComponentHolder.component<DomainCartComponentProvider>()
            .getDomainCartComponent()
    }
}