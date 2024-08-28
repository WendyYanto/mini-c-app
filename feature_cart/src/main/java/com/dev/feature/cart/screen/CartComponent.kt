package com.dev.feature.cart.screen

import androidx.appcompat.app.AppCompatActivity
import com.dev.core.CoreComponentInjector
import com.dev.core.di.CoreComponentApi
import com.dev.core.scope.FeatureScope
import com.dev.domain.cart.di.DomainCartComponent
import com.dev.domain.cart.di.DomainCartComponentProvider
import dagger.Component

@FeatureScope
@Component(
    dependencies = [
        CoreComponentApi::class,
        DomainCartComponent::class,
    ]
)
interface CartComponent {

    fun inject(activity: CartActivity)

    @Component.Factory
    interface Factory {

        fun build(
            coreComponent: CoreComponentApi,
            domainCartComponent: DomainCartComponent
        ): CartComponent
    }

    companion object Initializer {

        fun init(activity: AppCompatActivity) = DaggerCartComponent.factory()
            .build(
                coreComponent = CoreComponentInjector.getCoreComponent(activity),
                domainCartComponent = (activity.applicationContext as DomainCartComponentProvider).getDomainCartComponent(),
            )
    }
}