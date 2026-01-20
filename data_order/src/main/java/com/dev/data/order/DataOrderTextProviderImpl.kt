package com.dev.data.order

import com.dev.core.CoreTextProvider
import com.dev.core.scope.AppScope
import com.dev.core.scope.ApplicationScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@ApplicationScope
@ContributesBinding(AppScope::class)
class DataOrderTextProviderImpl @Inject constructor(
    private val coreTextProvider: CoreTextProvider
) : DataOrderTextProvider {
    override fun getOrderText(): String {
        return "order text with core: ${coreTextProvider.getText()}"
    }
}