package com.dev.data.product.di

import com.dev.core.scope.AppScope
import com.dev.core.scope.ApplicationScope
import com.dev.core.scope.DomainScope
import com.dev.data.product.DataProductTextProvider
import com.dev.data.product.DataProductTextProviderImpl
import com.squareup.anvil.annotations.ContributesTo
import dagger.Binds
import dagger.Module

@ContributesTo(AppScope::class)
@Module
interface DataProductModule {

    @Binds
    @ApplicationScope
    fun bindDataProductTextProvider(
        impl: DataProductTextProviderImpl
    ): DataProductTextProvider
}