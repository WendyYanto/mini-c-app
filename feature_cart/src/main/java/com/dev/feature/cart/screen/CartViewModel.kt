package com.dev.feature.cart.screen

import androidx.lifecycle.ViewModel
import com.dev.core.CoreTextProvider

class CartViewModel(
    private val coreTextProvider: CoreTextProvider,
    private val dataArgsProvider: DataArgsProvider
) : ViewModel() {

    val callback = Callback()

    inner class Callback : CartCallback {
        override val loadText: () -> String = {
            "cartViewModel: ${coreTextProvider.getText()} & ${dataArgsProvider.loadArgs()}"
        }
    }
}