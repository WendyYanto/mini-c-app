package com.dev.core.injector

import com.dev.core.scope.AppScope
import com.squareup.anvil.annotations.ContributesTo
import javax.inject.Provider

@ContributesTo(AppScope::class)
interface FeatureInjectorComponent {

    fun featureInjectors(): Map<Class<*>, Provider<FeatureInjector<*, *>>>
}