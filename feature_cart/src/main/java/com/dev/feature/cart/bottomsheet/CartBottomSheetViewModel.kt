package com.dev.feature.cart.bottomsheet

import androidx.lifecycle.ViewModel
import com.dev.core.scope.ActivityScope
import com.dev.domain.cart.DomainCartTextProvider
import com.squareup.anvil.annotations.ContributesBinding

@ContributesBinding(
    scope = ActivityScope::class
)
class CartBottomSheetViewModel(
    private val textProvider: DomainCartTextProvider
) : ViewModel() {

    fun loadText(): String {
        return "viewModel - ${textProvider.getDomainCartText()}"
    }
}
