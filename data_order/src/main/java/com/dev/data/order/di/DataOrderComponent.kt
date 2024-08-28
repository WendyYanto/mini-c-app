package com.dev.data.order.di

import android.content.Context
import com.dev.core.CoreComponentInjector
import com.dev.core.di.CoreComponent
import com.dev.core.di.CoreComponentApi
import com.dev.core.scope.DataScope
import com.dev.core.scope.FeatureScope
import com.dev.data.order.DataOrderTextProvider
import dagger.Component

@Component(
    dependencies = [CoreComponentApi::class],
    modules = [DataOrderModule::class]
)
@DataScope
interface DataOrderComponent {

    @Component.Factory
    interface Factory {

        fun build(
            coreComponent: CoreComponentApi
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