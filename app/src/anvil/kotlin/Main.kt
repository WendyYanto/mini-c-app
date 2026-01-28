package com.dev.merged

import com.dev.core.ComponentHolder
import com.dev.core.injector.FeatureInjector
import com.dev.core.scope.AppScope
import com.dev.core.scope.ApplicationScope
import com.dev.wenn.main.di.AppComponent
import com.squareup.anvil.annotations.MergeComponent
import dagger.BindsInstance
import dagger.Component
import kotlin.reflect.KClass

@ApplicationScope
@MergeComponent(
    scope = AppScope::class,
    dependencies = [AppComponent::class]
)
interface MergedAppComponent: AppComponent {

    @Component.Factory
    interface Factory {
        fun build(
            appComponent: AppComponent,
        ): MergedAppComponent
    }

    companion object Initializer {

        fun init(appComponent: AppComponent): AppComponent {
            return DaggerMergedAppComponent.factory()
                .build(
                    appComponent = appComponent,
                )
        }
    }
}

internal fun registerDI(
    appComponent: AppComponent
) {
    val mergedComponent = MergedAppComponent.init(appComponent)
    ComponentHolder.components += mergedComponent
}