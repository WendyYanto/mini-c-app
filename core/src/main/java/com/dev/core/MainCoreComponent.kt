package com.dev.core

import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [CoreTextModule::class]
)
@Singleton
interface MainCoreComponent {

    @Component.Factory
    interface Factory {

        fun build(): MainCoreComponent
    }

    companion object Initializer {

        fun init() = DaggerMainCoreComponent.factory().build()
    }

    fun provideCoreTextProvider(): CoreTextProvider
}