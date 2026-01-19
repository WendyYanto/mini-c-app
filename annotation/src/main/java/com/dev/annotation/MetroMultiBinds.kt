package com.dev.annotation

@MustBeDocumented
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY, AnnotationTarget.PROPERTY_GETTER)
@Retention(AnnotationRetention.RUNTIME)
public annotation class MetroMultiBinds(val allowEmpty: Boolean = false)