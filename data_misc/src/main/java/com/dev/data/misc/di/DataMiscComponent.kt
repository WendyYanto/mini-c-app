package com.dev.data.misc.di

import android.content.Context
import com.dev.core.CoreComponentInjector
import com.dev.core.di.CoreComponent
import com.dev.core.di.CoreComponentApi
import com.dev.core.scope.DataScope
import com.dev.data.misc.DataMiscTextProvider
import dagger.Component

@Component(
    dependencies = [CoreComponentApi::class],
    modules = [DataMiscModule::class]
)
@DataScope
interface DataMiscComponent {

    @Component.Factory
    interface Factory {

        fun build(
            coreComponent: CoreComponentApi
        ): DataMiscComponent
    }

    companion object Initializer {

        fun init(context: Context): DataMiscComponent =
            DaggerDataMiscComponent.factory()
                .build(
                    coreComponent = CoreComponentInjector.getCoreComponent(context)
                )
    }

    fun getDataMiscTextProvider(): DataMiscTextProvider
}