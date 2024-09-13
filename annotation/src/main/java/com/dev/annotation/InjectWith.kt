package com.dev.annotation

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class InjectWith(
    val dependencies: KClass<*> = Unit::class,
    val viewModels: Array<KClass<*>> = []
)