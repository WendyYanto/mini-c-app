package com.dev.wenn.main.screen

import com.dev.core.scope.ActivityScope
import com.dev.core.scope.AppScope
import com.squareup.anvil.annotations.ContributesSubcomponent
import com.squareup.anvil.annotations.ContributesTo

@ContributesSubcomponent(
    scope = ActivityScope::class,
    parentScope = AppScope::class
)
interface CartComponent {

    fun inject(activity: CartActivity)

    @ContributesTo(AppScope::class)
    interface ParentComponent {

        fun createCartComponent(): CartComponent
    }
}