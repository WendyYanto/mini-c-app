package com.dev.data.product

import com.dev.core.CoreTextProvider
import com.dev.core.scope.AppScope
import com.dev.core.scope.ApplicationScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@ApplicationScope
@ContributesBinding(AppScope::class)
class DataProductTextProviderImpl @Inject constructor(
    private val coreTextProvider: CoreTextProvider
) : DataProductTextProvider {
    override fun getProductText(): String {
        return "product text with core: ${coreTextProvider.getText()}"
    }
}