package com.dev.data.order

import javax.inject.Inject

class DataOrderTextDependency @Inject constructor() {

    fun getInfo(): String {
        return "data-order"
    }
}