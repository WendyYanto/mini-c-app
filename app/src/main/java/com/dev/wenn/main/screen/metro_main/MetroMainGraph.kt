package com.dev.wenn.main.screen.metro_main

import androidx.appcompat.app.AppCompatActivity
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.GraphExtension
import dev.zacsweers.metro.Provides

// This is like subcomponent
@GraphExtension
interface MetroMainGraph {

    fun inject(mainActivity: MetroMainActivity)

    // this is like subcomponent.factory
    @GraphExtension.Factory
    interface Factory {
        fun create(@Provides activity: AppCompatActivity): MetroMainGraph
    }

    @ContributesTo(scope = AppScope::class)
    interface ParentComponent {
        fun getMetroMainGraphFactory(): Factory
    }
}