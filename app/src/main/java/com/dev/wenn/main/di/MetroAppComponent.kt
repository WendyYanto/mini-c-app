package com.dev.wenn.main.di

import com.dev.core.scope.ApplicationScope
import com.dev.wenn.main.screen.MetroMainActivity
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Includes

@ApplicationScope
@DependencyGraph(
    scope = AppScope::class,
)
interface MetroAppComponent : AppComponent {

    fun injectMetroMainActivity(mainActivity: MetroMainActivity)

    @DependencyGraph.Factory
    interface Factory {
        fun create(
            @Includes appComponent: AppComponent,
        ): MetroAppComponent
    }
}