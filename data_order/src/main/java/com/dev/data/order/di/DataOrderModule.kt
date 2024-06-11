package com.dev.data.order.di

import com.dev.data.order.DataOrderTextProvider
import com.dev.data.order.DataOrderTextProviderImpl
import dagger.Binds
import dagger.Module

@Module
interface DataOrderModule {

    @Binds
    fun bindDataOrderTextProvider(
        impl: DataOrderTextProviderImpl
    ): DataOrderTextProvider
}