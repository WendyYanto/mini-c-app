package com.dev.domain.cart

import com.dev.core.CoreTextProvider
import com.dev.data.order.DataOrderTextProvider
import com.dev.data.product.DataProductTextProvider
import javax.inject.Inject

class DomainCartTextProviderImpl @Inject constructor(
    private val coreTextProvider: CoreTextProvider,
    private val dataOrderTextProvider: DataOrderTextProvider,
    private val dataProductTextProvider: DataProductTextProvider
) : DomainCartTextProvider {
    override fun getDomainCartText(): String {
        return """
            domain cart text
            core: ${coreTextProvider.getText()},
            order: ${dataOrderTextProvider.getOrderText()},
            product: ${dataProductTextProvider.getProductText()}
        """.trimIndent()
    }


}