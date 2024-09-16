package com.dev.feature.cart.bottomsheet

import androidx.lifecycle.ViewModel
import com.dev.core.scope.ActivityScope
import com.dev.domain.cart.DomainCartTextProvider
import com.squareup.anvil.annotations.ContributesBinding

@ContributesBinding(
    scope = ActivityScope::class
)
class CartBottomSheetViewModel(
    private val domainCartTextProvider: DomainCartTextProvider
) : ViewModel() {

    fun loadText(): String {
        return "viewModel - ${domainCartTextProvider.getDomainCartText()}"
    }
}
