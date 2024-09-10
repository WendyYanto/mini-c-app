package com.dev.core

object ComponentHolder {
    val components = mutableSetOf<Any>()

    inline fun <reified T> component(): T = components
        .filterIsInstance<T>()
        .single()
}