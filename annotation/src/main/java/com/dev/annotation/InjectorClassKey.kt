package com.dev.annotation

import dagger.MapKey
import kotlin.reflect.KClass

@dev.zacsweers.metro.MapKey(unwrapValue = false)
@MapKey(unwrapValue = false)
annotation class InjectorClassKey(
    val value: KClass<*>
)

/**
 * Required by dagger
 */
object InjectorClassKeyCreator {

    @JvmStatic
    fun createInjectorClassKey(value: Class<*>): InjectorClassKey {
        return InjectorClassKey(
            value = value.kotlin
        )
    }
}
