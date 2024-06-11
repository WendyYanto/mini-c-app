package com.dev.data.product

import com.dev.core.CoreTextProvider
import javax.inject.Inject

class DataProductTextProviderImpl @Inject constructor(
    private val coreTextProvider: CoreTextProvider
) : DataProductTextProvider {
    override fun getProductText(): String {
        return "product text with core: ${coreTextProvider.getText()}"
    }
}