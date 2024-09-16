package com.dev.feature.cart.bottomsheet

import androidx.lifecycle.ViewModel
import com.dev.domain.cart.DomainCartTextProvider

class CartBottomSheetViewModel(
    private val textProvider: DomainCartTextProvider
) : ViewModel() {

    fun loadText(): String {
        return "viewModel - ${textProvider.getDomainCartText()}"
    }
}
