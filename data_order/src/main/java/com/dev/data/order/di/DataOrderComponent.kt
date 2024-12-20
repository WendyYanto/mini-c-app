package com.dev.data.order.di

import com.dev.core.scope.AppScope
import com.dev.data.order.DataOrderTextProvider
import com.squareup.anvil.annotations.ContributesTo

@ContributesTo(AppScope::class)
interface DataOrderComponent {

    fun getDataOrderTextProvider(): DataOrderTextProvider
}