package com.dev.feature.cart.bottomsheet

import androidx.lifecycle.ViewModel
import com.dev.domain.cart.DomainCartTextProvider

class CartBottomSheetViewModel(
    private val textProvider: DomainCartTextProvider,
    private val arg: CartBottomSheetArg
) : ViewModel() {

    fun loadText(): String {
        return "${arg.text} - viewModel - ${textProvider.getDomainCartText()}"
    }
}
