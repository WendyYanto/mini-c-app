package com.dev.data.order

import com.dev.annotation.MetroInject
import com.dev.core.CoreTextProvider
import com.dev.core.scope.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@ContributesBinding(AppScope::class)
@MetroInject
class DataOrderTextProviderImpl @Inject constructor(
    private val coreTextProvider: CoreTextProvider
) : DataOrderTextProvider {
    override fun getOrderText(): String {
        return "order text with core: ${coreTextProvider.getText()}"
    }
}