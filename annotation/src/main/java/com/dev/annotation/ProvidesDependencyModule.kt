package com.dev.annotation

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ProvidesDependencyModule(
    val scope: KClass<*>
)