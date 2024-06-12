package com.dev.data.misc.di

import com.dev.data.misc.DataMiscTextProvider
import com.dev.data.misc.DataMiscTextProviderImpl
import dagger.Binds
import dagger.Module

@Module
interface DataMiscModule {

    @Binds
    fun bindDataMiscTextProvider(
        impl: DataMiscTextProviderImpl
    ): DataMiscTextProvider
}