package com.dev.domain.cart

import com.dev.core.CoreTextProvider
import com.dev.core.scope.AppScope
import com.dev.core.scope.ApplicationScope
import com.dev.data.order.DataOrderTextProvider
import com.dev.data.product.DataProductTextProvider
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@ApplicationScope
@ContributesBinding(AppScope::class)
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