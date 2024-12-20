package com.dev.core.injector


interface FeatureInjector<T, U> {

    /**
     * @param injectTarget will be Activity or Fragment
     * @param dependencyFactory to provide dependency from injectTarget
     */
    fun inject(injectTarget: T, dependencyFactory: () -> U)
}