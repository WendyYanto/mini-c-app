package com.dev.data.misc

import com.dev.core.CoreTextProvider
import javax.inject.Inject

class DataMiscTextProviderImpl @Inject constructor(
    private val coreTextProvider: CoreTextProvider
) : DataMiscTextProvider {
    override fun getMiscText(): String {
        return "misc text with core: ${coreTextProvider.getText()}"
    }
}