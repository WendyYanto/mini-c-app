package com.dev.core

import android.content.Context
import com.dev.core.di.CoreComponentProvider

object CoreComponentInjector {

    @JvmStatic
    fun getCoreComponent(context: Context) = context.applicationContext.run {
        if (this is CoreComponentProvider) {
            getCoreComponent()
        } else {
            throw RuntimeException("Cannot find CoreComponent - the Application class must implement CoreComponentProvider")
        }
    }
}