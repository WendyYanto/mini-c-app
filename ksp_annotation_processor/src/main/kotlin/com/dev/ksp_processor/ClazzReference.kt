package com.dev.ksp_processor

import com.squareup.kotlinpoet.ClassName

data class ClazzReference(
    val clazzName: String,
    val packageName: String,
    val propertyName: String = ""
) {

    val className
        get() = ClassName(packageName, clazzName)

    val isUnit =
        clazzName == "Unit"
}