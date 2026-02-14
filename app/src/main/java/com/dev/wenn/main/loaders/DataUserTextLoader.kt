package com.dev.wenn.main.loaders

import com.dev.core.TextLoader
import com.dev.data.user.DataUserTextProvider

class DataUserTextLoader(
    private val dataUserTextProvider: DataUserTextProvider
) : TextLoader() {

    override fun loadMergedText(): String {
        return "DataUserTextLoader - ${dataUserTextProvider.getUserText()}"
    }
}