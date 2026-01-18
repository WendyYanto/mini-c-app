package com.dev.data.product

import com.dev.annotation.MetroInject
import com.dev.core.CoreTextProvider
import com.dev.core.scope.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@ContributesBinding(AppScope::class)
@MetroInject
class DataProductTextProviderImpl @Inject constructor(
    private val coreTextProvider: CoreTextProvider
) : DataProductTextProvider {
    override fun getProductText(): String {
        return "product text with core: ${coreTextProvider.getText()}"
    }
}