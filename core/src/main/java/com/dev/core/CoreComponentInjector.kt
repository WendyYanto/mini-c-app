package com.dev.core

import android.content.Context
import com.dev.core.di.CoreComponentApiProvider

object CoreComponentInjector {

    @JvmStatic
    fun getCoreComponent(context: Context) = context.applicationContext.run {
        if (this is CoreComponentApiProvider) {
            getCoreComponent()
        } else {
            throw RuntimeException("Cannot find CoreComponent - the Application class must implement CoreComponentProvider")
        }
    }
}