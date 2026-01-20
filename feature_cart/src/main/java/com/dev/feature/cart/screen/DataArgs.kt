package com.dev.feature.cart.screen

import com.dev.core.scope.ActivityScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

interface DataArgsProvider {
    fun loadArgs(): DataArgs
}

@ContributesBinding(
    scope = ActivityScope::class
)
class DataArgsProviderImpl @Inject constructor(
    private val innerProvider: DataInnerArgsProvider
) : DataArgsProvider {
    override fun loadArgs(): DataArgs {
        return DataArgs("hello - ${innerProvider.loadArgs()}")
    }
}

data class DataArgs(
    val hi: String = "Hi"
)