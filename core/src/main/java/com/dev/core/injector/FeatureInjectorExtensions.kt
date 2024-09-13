package com.dev.core.injector

import com.dev.core.ComponentHolder

@Suppress("UNCHECKED_CAST")
inline fun <reified T, reified U> T.injectFeatureWithDependency(
    noinline dependencyFactory: () -> U
) {
    val injector = ComponentHolder
        .component<FeatureInjectorComponent>()
        .featureInjectors()[T::class.java] as? FeatureInjector<T, U>

    injector?.inject(this, dependencyFactory)
}

@Suppress("UNCHECKED_CAST")
inline fun <reified T> T.injectFeature() {
    val injector = ComponentHolder
        .component<FeatureInjectorComponent>()
        .featureInjectors()[T::class.java] as? FeatureInjector<T, Unit>

    injector?.inject(this) { }
}