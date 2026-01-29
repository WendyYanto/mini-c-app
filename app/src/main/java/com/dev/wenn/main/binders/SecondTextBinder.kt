package com.dev.wenn.main.binders

import javax.inject.Inject

class SecondTextBinder @Inject constructor() : TextBinder {
    override fun hi(): String {
        return "second text"
    }
}