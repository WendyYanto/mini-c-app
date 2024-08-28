package com.dev.domain.cart.di

import android.content.Context
import com.dev.core.CoreComponentInjector
import com.dev.core.di.CoreComponent
import com.dev.core.di.CoreComponentApi
import com.dev.core.scope.DomainScope
import com.dev.data.order.di.DataOrderComponent
import com.dev.data.order.di.DataOrderComponentProvider
import com.dev.data.product.di.DataProductComponent
import com.dev.data.product.di.DataProductComponentProvider
import com.dev.domain.cart.DomainCartTextProvider
import dagger.Component

@Component(
    dependencies = [CoreComponentApi::class,
        DataProductComponent::class,
        DataOrderComponent::class],
    modules = [DomainCartModule::class]
)
@DomainScope
interface DomainCartComponent {

    @Component.Factory
    interface Factory {

        fun build(
            coreComponent: CoreComponentApi,
            dataProductComponent: DataProductComponent,
            dataOrderComponent: DataOrderComponent
        ): DomainCartComponent
    }

    companion object Initializer {

        fun init(context: Context): DomainCartComponent =
            DaggerDomainCartComponent.factory()
                .build(
                    coreComponent = CoreComponentInjector.getCoreComponent(context),
                    dataProductComponent = (context.applicationContext as DataProductComponentProvider).getDataProductComponent(),
                    dataOrderComponent = (context.applicationContext as DataOrderComponentProvider).getDataOrderComponent(),
                )
    }

    fun getDomainCartTextProvider(): DomainCartTextProvider
}