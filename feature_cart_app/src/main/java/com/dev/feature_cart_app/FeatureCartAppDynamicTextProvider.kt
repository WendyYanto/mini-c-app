package com.dev.feature_cart_app

import com.dev.core.DynamicTextProvider
import com.dev.core.scope.AppScope
import com.dev.core.scope.ApplicationScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@ApplicationScope
@ContributesBinding(AppScope::class)
class FeatureCartAppDynamicTextProvider @Inject constructor() : DynamicTextProvider {
    override fun loadText(): String {
        return "from App"
    }
}