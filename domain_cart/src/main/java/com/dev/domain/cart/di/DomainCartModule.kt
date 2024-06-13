package com.dev.domain.cart.di

import com.dev.domain.cart.DomainCartTextProvider
import com.dev.domain.cart.DomainCartTextProviderImpl
import dagger.Binds
import dagger.Module

@Module
interface DomainCartModule {

    @Binds
    fun bindDomainCartTextProvider (
        impl: DomainCartTextProviderImpl
    ): DomainCartTextProvider
}