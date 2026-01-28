package com.dev.core.injector

import com.dev.annotation.InjectorClassKey
import com.dev.core.BuildConfig
import com.dev.core.ComponentHolder

@Suppress("UNCHECKED_CAST")
inline fun <reified T, reified U> T.injectComponentWithDependency(
    noinline dependencyFactory: () -> U
) {
    val injector = ComponentHolder
        .component<FeatureInjectorComponent>()
        .featureInjectors()[InjectorClassKey(T::class)] as? FeatureInjector<T, U>

    injector?.inject(this, dependencyFactory)
}

@Suppress("UNCHECKED_CAST")
inline fun <reified T> T.injectComponent() {
    val injector = ComponentHolder
        .component<FeatureInjectorComponent>()
        .featureInjectors()[InjectorClassKey(T::class)] as? FeatureInjector<T, Unit>

    injector?.inject(this) { }
}