package com.dev.annotation

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Repeatable
public annotation class AnvilComponent(
    val componentName: String,
    val modules: Array<KClass<*>> = [],
    val dependencies: Array<KClass<*>> = [],
)