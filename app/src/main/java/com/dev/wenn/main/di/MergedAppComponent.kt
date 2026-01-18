package com.dev.wenn.main.di

import com.dev.core.scope.AppScope
import com.dev.core.scope.ApplicationScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Includes

@ApplicationScope
@DependencyGraph(
    scope = AppScope::class,
)
interface MetroAppComponent : AppComponent {

    @DependencyGraph.Factory
    interface Factory {
        fun create(
            @Includes appComponent: AppComponent,
        ): MetroAppComponent
    }
}