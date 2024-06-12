package com.dev.wenn.main.di

import com.dev.core.di.CoreComponent
import com.dev.core.scope.ApplicationScope
import com.dev.data.misc.di.DataMiscComponent
import com.dev.data.order.di.DataOrderComponent
import com.dev.data.product.di.DataProductComponent
import com.dev.data.user.di.DataUserComponent
import com.dev.wenn.main.App
import dagger.Component

@ApplicationScope
@Component(
    dependencies = [
        CoreComponent::class,
        DataUserComponent::class,
        DataProductComponent::class,
        DataOrderComponent::class,
        DataMiscComponent::class
    ]
)
interface AppComponent :
    CoreComponent,
    DataUserComponent,
    DataProductComponent,
    DataOrderComponent,
    DataMiscComponent {

    companion object Initializer {

        fun init(app: App): AppComponent {
            ComponentsRegistry.init(app)
            return DaggerAppComponent.builder()
                .coreComponent(app.getCoreComponent())
                .dataUserComponent(app.getDataUserComponent())
                .dataProductComponent(app.getDataProductComponent())
                .dataOrderComponent(app.getDataOrderComponent())
                .dataMiscComponent(app.getDataMiscComponent())
                .build()
        }
    }
}