package com.dev.domain.cart

import javax.inject.Inject

interface CartModuleTest {

    fun load(): String
}

class CartModuleTestImpl @Inject constructor(): CartModuleTest {

    override fun load(): String {
        return "CartModuleTest"
    }
}