package com.dev.data.user.di

import com.dev.annotation.MetroProvides
import com.dev.core.CoreTextProvider
import com.dev.core.scope.AppScope
import com.dev.core.scope.ApplicationScope
import com.dev.data.user.DataJavaTextProvider
import com.dev.data.user.DataJavaTextProviderImpl
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides

@Module
@ContributesTo(AppScope::class)
interface DataUserModule {

    companion object {

        @Provides
        // this is required for metro interops
        @MetroProvides
        @ApplicationScope
        fun provideDataJavaTextProviderImpl(
            coreTextProvider: CoreTextProvider
        ): DataJavaTextProvider {
            return DataJavaTextProviderImpl(
                coreTextProvider
            )
        }
    }
}