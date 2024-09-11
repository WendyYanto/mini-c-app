package com.dev.data.product.di

import android.content.Context
import com.dev.core.CoreComponentInjector
import com.dev.core.di.CoreComponent
import com.dev.core.scope.AppScope
import com.dev.core.scope.DataScope
import com.dev.data.product.DataProductTextProvider
import com.squareup.anvil.annotations.ContributesTo
import dagger.Component

@ContributesTo(AppScope::class)
interface DataProductComponent {

    fun getDataProductTextProvider(): DataProductTextProvider
}