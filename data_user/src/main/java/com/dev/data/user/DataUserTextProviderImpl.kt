package com.dev.data.user

import com.dev.core.CoreTextProvider
import com.dev.core.scope.AppScope
import dev.zacsweers.metro.ContributesBinding
import javax.inject.Inject

@ContributesBinding(AppScope::class)
@dev.zacsweers.metro.Inject
class DataUserTextProviderImpl @Inject constructor(
    private val coreTextProvider: CoreTextProvider
) : DataUserTextProvider {
    override fun getUserText(): String {
        return "user text with core: ${coreTextProvider.getText()}"
    }
}