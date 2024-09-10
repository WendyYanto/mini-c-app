package com.dev.data.user

import com.dev.core.CoreTextProvider
import com.dev.core.DynamicTextProvider
import com.dev.core.scope.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class DynamicUserTextProvider @Inject constructor(
    private val coreTextProvider: CoreTextProvider
) : DynamicTextProvider {
    override fun loadText(): String {
        return "DynamicUserTextProvider + ${coreTextProvider.getText()}"
    }
}