package com.dev.feature.cart.screen

import androidx.lifecycle.ViewModel
import com.dev.core.CoreTextProvider

class CartViewModel(
    private val textProvider: CoreTextProvider
) : ViewModel() {

    init {
        println(textProvider.getText())
    }
}