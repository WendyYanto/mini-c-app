package com.dev.data.order.di

import com.dev.core.scope.AppScope
import com.dev.core.scope.ApplicationScope
import com.dev.data.order.DataOrderTextDependency
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides

@ContributesTo(AppScope::class)
@Module
interface DataOrderModule {

    companion object {

        @Provides
        @ApplicationScope
        fun provideDataOrderTextDependency(): DataOrderTextDependency {
            return DataOrderTextDependency()
        }
    }

//    @Binds
//    @ApplicationScope
//    fun bindDataOrderTextProvider(
//        impl: DataOrderTextProviderImpl
//    ): DataOrderTextProvider
}