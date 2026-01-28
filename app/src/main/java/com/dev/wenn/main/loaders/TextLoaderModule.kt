package com.dev.wenn.main.loaders

import com.dev.core.CoreTextProvider
import com.dev.core.TextLoader
import com.dev.core.TextLoaderProvider
import com.dev.core.scope.AppScope
import com.dev.data.user.DataUserTextProvider
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@ContributesTo(AppScope::class)
@Module
class TextLoaderModule {

    @[Provides IntoMap ClassKey(CoreTextLoader::class)]
    fun provideCoreTextLoader(
        coreTextProvider: CoreTextProvider
    ): TextLoaderProvider<*> =
        object : TextLoaderProvider<CoreTextLoader>() {
            override fun create(): CoreTextLoader {
                return CoreTextLoader(coreTextProvider)
            }
        }

    @[Provides IntoMap ClassKey(DataUserTextLoader::class)]
    fun provideDataUserTextLoader(
        dataUserTextProvider: DataUserTextProvider
    ): TextLoaderProvider<*> =
        object : TextLoaderProvider<DataUserTextLoader>() {
            override fun create(): DataUserTextLoader {
                return DataUserTextLoader(dataUserTextProvider)
            }
        }
}