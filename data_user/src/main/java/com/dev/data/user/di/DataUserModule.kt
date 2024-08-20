package com.dev.data.user.di

import com.dev.data.user.DataUserTextProvider
import com.dev.data.user.DataUserTextProviderImpl
import dagger.Binds
import dagger.Module

@Module
interface DataUserModule {

    @DataUserScope
    @Binds
    fun bindDataUserTextProvider(
        impl: DataUserTextProviderImpl
    ): DataUserTextProvider
}