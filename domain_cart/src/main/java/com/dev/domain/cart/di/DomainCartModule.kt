package com.dev.domain.cart.di

import com.dev.core.scope.AppScope
import com.dev.core.scope.ApplicationScope
import com.dev.domain.cart.DomainCartTextProvider
import com.dev.domain.cart.DomainCartTextProviderImpl
import com.squareup.anvil.annotations.ContributesTo
import dagger.Binds
import dagger.Module

@Module
@ContributesTo(AppScope::class)
interface DomainCartModule {

    @Binds
    @ApplicationScope
    fun bindDomainCartTextProvider (
        impl: DomainCartTextProviderImpl
    ): DomainCartTextProvider
}