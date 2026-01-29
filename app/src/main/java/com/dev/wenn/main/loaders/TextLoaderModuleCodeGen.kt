package com.dev.wenn.main.loaders

import com.dev.annotation.DependencyModule
import com.dev.core.CoreTextProvider
import com.dev.core.TextLoaderProvider
import com.dev.data.user.DataUserTextProvider

@DependencyModule
interface TextLoaderModuleCodeGen {

    /**
     * @com.dev.annotation.TextLoaderKey(CoreTextLoader::class)
     */
    fun provideCoreTextLoader(
        coreTextProvider: CoreTextProvider
    ): TextLoaderProvider<*>

    /**
     * @com.dev.annotation.TextLoaderKey(DataUserTextLoader::class)
     */
    fun provideDataUserTextLoader(
        dataUserTextProvider: DataUserTextProvider
    ): TextLoaderProvider<*>
}