package com.dev.core

import javax.inject.Inject

class CoreTextProviderImpl @Inject constructor() : CoreTextProvider {
    override fun getText(): String {
        return "core-text"
    }
}