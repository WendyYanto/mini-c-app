package com.dev.wenn.main.screen

import com.dev.core.scope.ActivityScope
import com.dev.core.scope.AppScope
import com.squareup.anvil.annotations.ContributesSubcomponent
import com.squareup.anvil.annotations.ContributesTo

@ContributesSubcomponent(
    scope = ActivityScope::class,
    parentScope = AppScope::class
)
interface MainComponent {

    fun inject(activity: MainActivity)

    @ContributesTo(AppScope::class)
    interface ParentComponent {
        fun createMainComponent(): MainComponent
    }
}