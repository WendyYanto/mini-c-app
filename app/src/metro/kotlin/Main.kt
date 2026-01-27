package com.dev.merged

import com.dev.core.ComponentHolder
import com.dev.core.injector.FeatureInjector
import com.dev.core.scope.AppScope
import com.dev.core.scope.ApplicationScope
import com.dev.wenn.main.di.AppComponent
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Includes
import dev.zacsweers.metro.createGraphFactory
import kotlin.reflect.KClass

@ApplicationScope
@DependencyGraph(
    scope = AppScope::class,
)
interface MergedAppComponent : AppComponent {

    @DependencyGraph.Factory
    interface Factory {
        fun create(
            @Includes appComponent: AppComponent,
            // empty bindings
//            @Includes classMap: Map<Class<*>, FeatureInjector<*, *>>
        ): MergedAppComponent
    }
}

internal fun registerDI(
    appComponent: AppComponent
) {
    ComponentHolder.components += createGraphFactory<MergedAppComponent.Factory>()
        .create(appComponent = appComponent)
}

