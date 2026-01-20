package com.dev.core.di

import com.dev.core.CoreTextProvider
import com.dev.core.CoreTextProviderImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
interface CoreTextModule {

    companion object {

        @Provides
        @Singleton
        fun provideCoreTextProvider(): CoreTextProvider {
            return CoreTextProviderImpl()
        }
    }
}