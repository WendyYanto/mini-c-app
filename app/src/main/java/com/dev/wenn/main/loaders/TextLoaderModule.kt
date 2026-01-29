package com.dev.wenn.main.loaders

import com.dev.annotation.MetroProvides
import com.dev.annotation.TextLoaderKey
import com.dev.core.CoreTextProvider
import com.dev.core.TextLoaderProvider
import com.dev.core.scope.AppScope
import com.dev.data.user.DataUserTextProvider
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dev.zacsweers.metro.Multibinds

@ContributesTo(AppScope::class)
interface TextLoaderMapContributor {

    @Multibinds
    fun textLoaders(): Map<TextLoaderKey, TextLoaderProvider<*>>
}

@Module
@ContributesTo(AppScope::class)
interface TextLoaderModule {

    companion object {
//        @dagger.multibindings.IntoMap
//        @dagger.Provides
        @dev.zacsweers.metro.IntoMap
        @MetroProvides
        @TextLoaderKey(CoreTextLoader::class)
        fun provideCoreTextLoader(
            coreTextProvider: CoreTextProvider
        ): TextLoaderProvider<*> =
            object : TextLoaderProvider<CoreTextLoader>() {
                override fun create(): CoreTextLoader {
                    return CoreTextLoader(coreTextProvider)
                }
            }


//        @dagger.multibindings.IntoMap
//        @dagger.Provides
        @dev.zacsweers.metro.IntoMap
        @MetroProvides
        @TextLoaderKey(DataUserTextLoader::class)
        fun provideDataUserTextLoader(
            dataUserTextProvider: DataUserTextProvider
        ): TextLoaderProvider<*> =
            object : TextLoaderProvider<DataUserTextLoader>() {
                override fun create(): DataUserTextLoader {
                    return DataUserTextLoader(dataUserTextProvider)
                }
            }
    }
}