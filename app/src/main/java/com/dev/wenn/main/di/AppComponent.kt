package com.dev.wenn.main.di

import com.dev.core.di.CoreComponent
import com.dev.core.scope.AppScope
import com.dev.core.scope.ApplicationScope
import com.dev.data.misc.di.DataMiscComponent
import com.dev.data.order.di.DataOrderComponent
import com.dev.data.product.di.DataProductComponent
import com.dev.data.user.di.DataUserComponent
import com.dev.domain.cart.di.DomainCartComponent
import com.dev.wenn.main.App
import com.squareup.anvil.annotations.MergeComponent
import dagger.Component

@ApplicationScope
@MergeComponent(
    scope = AppScope::class,
    dependencies = [
        CoreComponent::class,
        DataUserComponent::class,
        DataProductComponent::class,
        DataOrderComponent::class,
        DataMiscComponent::class,
        DomainCartComponent::class
    ]
)
interface AppComponent :
    CoreComponent,
    DataUserComponent,
    DataProductComponent,
    DataOrderComponent,
    DataMiscComponent,
    DomainCartComponent {

    companion object Initializer {

        fun init(app: App): AppComponent {
            ComponentsRegistry.init(app)
            return DaggerAppComponent.builder()
                .coreComponent(app.getCoreComponent())
                .dataUserComponent(app.getDataUserComponent())
                .dataProductComponent(app.getDataProductComponent())
                .dataOrderComponent(app.getDataOrderComponent())
                .dataMiscComponent(app.getDataMiscComponent())
                .domainCartComponent(app.getDomainCartComponent())
                .build()
        }
    }
}