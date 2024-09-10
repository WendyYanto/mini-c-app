package com.dev.feature.cart.screen

import com.dev.core.scope.ActivityScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

data class DataInnerArgs(
    val lorem: String = "Lorem"
)

interface DataInnerArgsProvider {
    fun loadArgs(): DataInnerArgs
}

@ContributesBinding(
    scope = ActivityScope::class
)
class DataInnerArgsProviderImpl @Inject constructor() : DataInnerArgsProvider {
    override fun loadArgs(): DataInnerArgs {
        return DataInnerArgs("Lorem11112312313")
    }
}