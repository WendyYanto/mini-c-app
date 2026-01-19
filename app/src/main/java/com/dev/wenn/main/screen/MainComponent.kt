package com.dev.wenn.main.screen

import com.dev.core.scope.ActivityScope
import com.squareup.anvil.annotations.ContributesSubcomponent
import com.squareup.anvil.annotations.ContributesTo
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.GraphExtension
import dev.zacsweers.metro.SingleIn

//@ContributesSubcomponent(
//    scope = ActivityScope::class,
//    parentScope = AppScope::class
//)
//interface MainComponent {
//
//    fun inject(activity: MainActivity)
//
//    @ContributesTo(AppScope::class)
//    interface ParentComponent {
//        fun createMainComponent(): MainComponent
//    }
//}

//@GraphExtension(
//    scope = AppScope::class,
//)
//interface MainComponent {
//
//    fun inject(activity: MainActivity)
//
//    @ContributesTo(AppScope::class)
//    interface ParentComponent {
//        fun createMainComponent(): MainComponent
//    }
//}