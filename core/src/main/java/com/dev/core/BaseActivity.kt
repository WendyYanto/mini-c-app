package com.dev.core

import androidx.appcompat.app.AppCompatActivity
import dev.zacsweers.metro.HasMemberInjections
import javax.inject.Inject

@HasMemberInjections
open class BaseActivity: AppCompatActivity() {

    @Inject
    lateinit var coreTextProvider: CoreTextProvider

    fun loadCore(): String {
        return coreTextProvider.getText()
    }
}