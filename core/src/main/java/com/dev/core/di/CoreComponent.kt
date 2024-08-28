package com.dev.core.di

import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [CoreTextModule::class]
)
@Singleton
interface CoreComponent: CoreComponentApi {

    @Component.Factory
    interface Factory {

        fun build(): CoreComponent
    }

    companion object Initializer {

        fun init() = DaggerCoreComponent.factory().build()
    }
}