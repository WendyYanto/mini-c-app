package com.dev.data.order.di

import android.content.Context
import com.dev.core.CoreComponentInjector
import com.dev.core.di.CoreComponent
import com.dev.core.scope.DataScope
import com.dev.data.order.DataOrderTextProvider
import dagger.Component

@Component(
    dependencies = [CoreComponent::class],
    modules = [DataOrderModule::class]
)
@DataScope
interface DataOrderComponent {

    @Component.Factory
    interface Factory {

        fun build(
            coreComponent: CoreComponent
        ): DataOrderComponent
    }

    companion object Initializer {

        fun init(context: Context): DataOrderComponent =
            DaggerDataOrderComponent.factory()
                .build(
                    coreComponent = CoreComponentInjector.getCoreComponent(context)
                )
    }

    fun getDataOrderTextProvider(): DataOrderTextProvider
}