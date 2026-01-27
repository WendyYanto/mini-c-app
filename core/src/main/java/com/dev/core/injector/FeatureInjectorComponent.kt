package com.dev.core.injector

import com.dev.annotation.InjectorClassKey
import com.dev.core.scope.AppScope
import com.squareup.anvil.annotations.ContributesTo
import dev.zacsweers.metro.Multibinds
import kotlin.reflect.KClass

@ContributesTo(AppScope::class)
interface FeatureInjectorComponent {

    @Multibinds
    fun kClassFeatureInjectors(): Map<KClass<*>, FeatureInjector<*, *>>

    @Multibinds(allowEmpty = true)
    fun featureInjectors(): Map<InjectorClassKey, FeatureInjector<*, *>>
}