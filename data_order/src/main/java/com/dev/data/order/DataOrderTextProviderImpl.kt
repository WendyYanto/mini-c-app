package com.dev.data.order

import com.dev.core.CoreTextProvider
import javax.inject.Inject

class DataOrderTextProviderImpl @Inject constructor(
    private val coreTextProvider: CoreTextProvider
) : DataOrderTextProvider {
    override fun getOrderText(): String {
        return "order text with core: ${coreTextProvider.getText()}"
    }
}