package com.dev.data.user

import com.dev.core.CoreTextProvider
import javax.inject.Inject

class DataUserTextProviderImpl @Inject constructor(
    private val coreTextProvider: CoreTextProvider
) : DataUserTextProvider {
    override fun getUserText(): String {
        return "user text with core: ${coreTextProvider.getText()}"
    }
}