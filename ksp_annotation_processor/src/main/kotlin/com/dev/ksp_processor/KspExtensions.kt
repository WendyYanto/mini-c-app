package com.dev.ksp_processor

import com.google.devtools.ksp.isDefault
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSTypeAlias
import com.google.devtools.ksp.symbol.KSTypeParameter
import com.google.devtools.ksp.symbol.KSValueArgument

// Reference: https://github.com/ZacSweers/anvil/blob/749e9742f5de8697ad5b31f48a4d67137d7455cb/compiler-utils/src/main/java/com/squareup/anvil/compiler/internal/ksp/KSAnnotationExtensions.kt
fun KSAnnotation.classArrayArgument(name: String): List<KSClassDeclaration> =
    argumentOfTypeWithMapperAt<List<KSType>, List<KSClassDeclaration>>(
        name,
    ) { arg, value ->
        value.map {
            it.resolveKSClassDeclaration()
                ?: throw RuntimeException("Could not resolve $name")
        }
    }.orEmpty()

/**
 * Resolves the [KSClassDeclaration] for this type, including following typealiases as needed.
 */
fun KSType.resolveKSClassDeclaration(): KSClassDeclaration? =
    declaration.resolveKSClassDeclaration()

/**
 * Resolves the [KSClassDeclaration] representation of this declaration, including following
 * typealiases as needed.
 *
 * [KSTypeParameter] types will return null. If you expect one here, you should check the
 * declaration directly.
 */
fun KSDeclaration.resolveKSClassDeclaration(): KSClassDeclaration? {
    return when (val declaration = unwrapTypealiases()) {
        is KSClassDeclaration -> declaration
        is KSTypeParameter -> null
        else -> error("Unexpected declaration type: $this")
    }
}

/**
 * Returns the resolved declaration following any typealiases.
 */
tailrec fun KSDeclaration.unwrapTypealiases(): KSDeclaration = when (this) {
    is KSTypeAlias -> type.resolve().declaration.unwrapTypealiases()
    else -> this
}

fun KSAnnotation.argumentAt(
    name: String,
): KSValueArgument? {
    return arguments.find { it.name?.asString() == name }
        ?.takeUnless { it.isDefault() }
}

inline fun <reified T, R> KSAnnotation.argumentOfTypeWithMapperAt(
    name: String,
    mapper: (arg: KSValueArgument, value: T) -> R,
): R? {
    return argumentAt(name)
        ?.let { arg ->
            val value = arg.value
            if (value !is T) {
                throw IllegalArgumentException(
                    "Expected argument '$name' of type '${T::class.qualifiedName} but was '${arg.javaClass.name}'.",
                )
            } else {
                value?.let { mapper(arg, it) }
            }
        }
}

fun KSClassDeclaration.getAllParentClasses(): Set<KSClassDeclaration> {
    val parents = mutableSetOf<KSClassDeclaration>()

    for (typeReference in superTypes.toList()) {
        val type = typeReference.resolve() // Resolve KSTypeReference to KSType
        val declaration = type.declaration

        if (declaration is KSClassDeclaration) {
            parents.add(declaration)
            parents.addAll(declaration.getAllParentClasses())
        }
    }

    return parents
}