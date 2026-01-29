package com.dev.data.product.di

import com.dev.core.scope.AppScope
import com.dev.data.product.DataProductTextProvider
import com.squareup.anvil.annotations.ContributesTo

@ContributesTo(AppScope::class)
interface DataProductComponent {

    fun getDataProductTextProvider(): DataProductTextProvider
}