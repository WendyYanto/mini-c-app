package com.dev.wenn.main.binders

import com.dev.annotation.BindsDependencyModule
import com.dev.core.scope.AppScope

@BindsDependencyModule(
    scope = AppScope::class
)
interface TextBinderModule {

    /**
     * @StringKey("first")
     */
    val FirstTextBinder.binds: TextBinder

    /**
     * @StringKey("second")
     */
    val SecondTextBinder.binds: TextBinder
}