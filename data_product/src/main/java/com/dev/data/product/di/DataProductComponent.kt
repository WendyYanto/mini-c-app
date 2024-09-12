package com.dev.data.product.di

import android.content.Context
import com.dev.core.CoreComponentInjector
import com.dev.core.di.CoreComponent
import com.dev.core.di.CoreComponentApi
import com.dev.core.scope.DataScope
import com.dev.data.product.DataProductTextProvider
import dagger.Component

@Component(
    dependencies = [CoreComponentApi::class],
    modules = [DataProductModule::class]
)
@DataScope
interface DataProductComponent {

    @Component.Factory
    interface Factory {

        fun build(
            coreComponent: CoreComponentApi
        ): DataProductComponent
    }

    companion object Initializer {

        fun init(context: Context): DataProductComponent =
            DaggerDataProductComponent.factory()
                .build(
                    coreComponent = CoreComponentInjector.getCoreComponent(context)
                )
    }

    fun getDataProductTextProvider(): DataProductTextProvider
}