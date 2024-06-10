package com.dev.core.di

import com.dev.core.CoreTextProvider
import com.dev.core.CoreTextProviderImpl
import dagger.Module
import dagger.Provides

@Module
interface CoreTextModule {

    companion object {

        @Provides
        fun provideCoreTextProvider(): CoreTextProvider {
            return CoreTextProviderImpl()
        }
    }
}