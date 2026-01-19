package com.dev.core.injector

import com.dev.annotation.MetroMultiBinds
import com.dev.core.scope.AppScope
import com.squareup.anvil.annotations.ContributesTo
import kotlin.reflect.KClass

@ContributesTo(AppScope::class)
interface FeatureInjectorComponent {

    @MetroMultiBinds
    fun kClassFeatureInjectors(): Map<KClass<*>, FeatureInjector<*, *>>

    fun featureInjectors(): Map<Class<*>, FeatureInjector<*, *>>
}