package com.dev.wenn.main.loaders

import com.dev.core.CoreTextProvider
import com.dev.core.TextLoader

class CoreTextLoader(
    private val coreTextLoader: CoreTextProvider
) : TextLoader() {

    override fun loadMergedText(): String {
        return "CoreTextLoader - ${coreTextLoader.getText()}"
    }
}