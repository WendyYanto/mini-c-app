package com.dev.wenn.main.binders

import javax.inject.Inject

class FirstTextBinder @Inject constructor() : TextBinder {
    override fun hi(): String {
        return "first text"
    }
}