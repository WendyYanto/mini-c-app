package com.dev.core.di

import com.dev.core.CoreTextProvider
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [CoreTextModule::class]
)
@Singleton
interface CoreComponent {

    @Component.Factory
    interface Factory {

        fun build(): CoreComponent
    }

    companion object Initializer {

        fun init() = DaggerCoreComponent.factory().build()
    }

    fun provideCoreTextProvider(): CoreTextProvider
}