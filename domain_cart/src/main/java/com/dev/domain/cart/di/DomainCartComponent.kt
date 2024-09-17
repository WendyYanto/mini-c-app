package com.dev.domain.cart.di

import com.dev.core.scope.AppScope
import com.dev.domain.cart.DomainCartTextProvider
import com.squareup.anvil.annotations.ContributesTo

@ContributesTo(AppScope::class)
interface DomainCartComponent {

    fun getDomainCartTextProvider(): DomainCartTextProvider
}