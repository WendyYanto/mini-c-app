package com.dev.wenn.main.di

import com.dev.core.di.CoreComponent
import com.dev.core.scope.AppScope
import com.dev.core.scope.ApplicationScope
import com.dev.data.order.di.DataOrderComponent
import com.dev.data.product.di.DataProductComponent
import com.dev.domain.cart.di.DomainCartComponent
import com.dev.feature_cart_app.App
import com.squareup.anvil.annotations.MergeComponent

@ApplicationScope
@MergeComponent(
    scope = AppScope::class,
    dependencies = [
        CoreComponent::class,
        DataProductComponent::class,
        DataOrderComponent::class,
        DomainCartComponent::class
    ]
)
interface AppComponent :
    CoreComponent,
    DataProductComponent,
    DataOrderComponent,
    DomainCartComponent {

    fun inject(app: App)

    companion object Initializer {

        fun init(app: App): AppComponent {
            ComponentsRegistry.init(app)
            return DaggerAppComponent.builder()
                .coreComponent(app.getCoreComponent())
                .dataProductComponent(app.getDataProductComponent())
                .dataOrderComponent(app.getDataOrderComponent())
                .domainCartComponent(app.getDomainCartComponent())
                .build()
        }
    }
}