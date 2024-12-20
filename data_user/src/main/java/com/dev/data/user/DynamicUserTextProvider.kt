package com.dev.data.user

import android.util.Log
import com.dev.core.CoreTextProvider
import com.dev.core.DynamicTextProvider
import com.dev.core.scope.AppScope
import com.dev.core.scope.ApplicationScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@ApplicationScope
@ContributesBinding(AppScope::class)
class DynamicUserTextProvider @Inject constructor(
    private val coreTextProvider: CoreTextProvider
) : DynamicTextProvider {

    init {
        Log.v("LOGY", "created DynamicUserTextProvider")
    }

    override fun loadText(): String {
        return "DynamicUserTextProvider + ${coreTextProvider.getText()}"
    }
}