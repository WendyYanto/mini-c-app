package com.dev.data.product.di

import com.dev.data.product.DataProductTextProvider
import com.dev.data.product.DataProductTextProviderImpl
import dagger.Binds
import dagger.Module

@Module
interface DataProductModule {

    @Binds
    fun bindDataProductTextProvider(
        impl: DataProductTextProviderImpl
    ): DataProductTextProvider
}