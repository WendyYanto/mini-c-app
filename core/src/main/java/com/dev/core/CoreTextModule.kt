package com.dev.core

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