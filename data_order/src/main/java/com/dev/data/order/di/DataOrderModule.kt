package com.dev.data.order.di

import com.dev.core.scope.AppScope
import com.dev.core.scope.ApplicationScope
import com.dev.data.order.DataOrderTextProvider
import com.dev.data.order.DataOrderTextProviderImpl
import com.squareup.anvil.annotations.ContributesTo
import dagger.Binds
import dagger.Module

@ContributesTo(AppScope::class)
@Module
interface DataOrderModule {

    @Binds
    @ApplicationScope
    fun bindDataOrderTextProvider(
        impl: DataOrderTextProviderImpl
    ): DataOrderTextProvider
}