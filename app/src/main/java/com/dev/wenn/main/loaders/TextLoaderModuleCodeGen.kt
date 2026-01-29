package com.dev.wenn.main.loaders

import com.dev.annotation.TextLoaderModuleCodeGenAnnotation
import com.dev.core.CoreTextProvider
import com.dev.core.TextLoaderProvider
import com.dev.data.user.DataUserTextProvider

@TextLoaderModuleCodeGenAnnotation
interface TextLoaderModuleCodeGen {

    fun provideCoreTextLoader(
        coreTextProvider: CoreTextProvider
    ): TextLoaderProvider<*>

    fun provideDataUserTextLoader(
        dataUserTextProvider: DataUserTextProvider
    ): TextLoaderProvider<*>
}