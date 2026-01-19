package com.dev.annotation

@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.FIELD,
)
annotation class MetroInject