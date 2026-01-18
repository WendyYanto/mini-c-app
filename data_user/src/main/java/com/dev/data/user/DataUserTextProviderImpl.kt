package com.dev.data.user

import com.dev.annotation.MetroInject
import com.dev.core.CoreTextProvider
import com.dev.core.scope.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@ContributesBinding(AppScope::class)
@MetroInject
class DataUserTextProviderImpl @Inject constructor(
    private val coreTextProvider: CoreTextProvider
) : DataUserTextProvider {
    override fun getUserText(): String {
        return "user text with core: ${coreTextProvider.getText()}"
    }
}