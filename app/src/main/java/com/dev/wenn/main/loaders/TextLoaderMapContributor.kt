package com.dev.wenn.main.loaders

import com.dev.annotation.TextLoaderKey
import com.dev.core.TextLoaderProvider
import com.dev.core.scope.AppScope
import com.squareup.anvil.annotations.ContributesTo
import dev.zacsweers.metro.Multibinds

@ContributesTo(AppScope::class)
interface TextLoaderMapContributor {

    @Multibinds
    fun textLoaders(): Map<TextLoaderKey, TextLoaderProvider<*>>
}