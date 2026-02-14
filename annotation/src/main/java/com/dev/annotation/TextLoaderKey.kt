package com.dev.annotation

import dagger.MapKey
import kotlin.reflect.KClass

@dev.zacsweers.metro.MapKey(unwrapValue = false)
@MapKey(unwrapValue = false)
annotation class TextLoaderKey(
    val value: KClass<*>
)

/**
 * Required by dagger
 */
object TextLoaderKeyCreator {

    @JvmStatic
    fun createTextLoaderKey(value: Class<*>): TextLoaderKey {
        return TextLoaderKey(
            value = value.kotlin
        )
    }
}
