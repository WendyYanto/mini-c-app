package com.dev.feature.cart.screen

import androidx.lifecycle.ViewModel
import com.dev.core.CoreTextProvider
import com.dev.core.scope.ActivityScope
import com.squareup.anvil.annotations.ContributesBinding

@ContributesBinding(
    scope = ActivityScope::class,
    boundType = CartCallback::class
)
@ContributesBinding(
    scope = ActivityScope::class,
    boundType = CartOtherCallback::class
)
class CartViewModel(
    private val coreTextProvider: CoreTextProvider,
    private val dataArgsProvider: DataArgsProvider
) : ViewModel(), CartCallback, CartOtherCallback {

    override val loadText: () -> String = {
        "Hi ${coreTextProvider.getText()} & ${dataArgsProvider.loadArgs()}"
    }
    override val loadOtherText: () -> String = {
        "otherText - cartViewModel: ${coreTextProvider.getText()} & ${dataArgsProvider.loadArgs()}"
    }
}