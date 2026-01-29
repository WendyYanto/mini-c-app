package com.dev.ksp_processor

import com.dev.annotation.TextLoaderModuleCodeGenAnnotation
import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getDeclaredFunctions
import com.google.devtools.ksp.isConstructor
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.validate
import com.google.devtools.ksp.visitor.KSDefaultVisitor
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ksp.writeTo

class TextLoaderModuleCodeGenProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val useMetro: Boolean,
) : SymbolProcessor {

    private val visitedSymbols = mutableSetOf<Any>()
    private val annotationName = TextLoaderModuleCodeGenAnnotation::class.qualifiedName.toString()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        visitedSymbols.clear()
        val (resolvedSymbols, unresolvedSymbols) = resolver.getSymbolsWithAnnotation(annotationName)
            .partition { it.validate() }

        println("working process - $annotationName ")

        resolvedSymbols
            .mapNotNull { ksAnnotated ->
                println("working ksAnnotated - $ksAnnotated")
                when (ksAnnotated) {
                    is KSClassDeclaration -> ksAnnotated
                    else -> null
                }
            }
            .forEach {
                println("working TextLoaderModuleCodeGenVisitor")
                it.accept(
                    TextLoaderModuleCodeGenVisitor(codeGenerator, logger, visitedSymbols),
                    null
                )
            }

        return unresolvedSymbols
    }
}

class TextLoaderModuleCodeGenVisitor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val visitedSymbols: MutableSet<Any>
) : KSDefaultVisitor<FileSpec.Builder?, Unit>() {

    private fun isVisited(symbol: KSNode): Boolean {
        if (visitedSymbols.contains(symbol)) return true
        visitedSymbols.add(symbol)
        return false
    }

    override fun defaultHandler(
        node: KSNode,
        data: FileSpec.Builder?
    ) {
        // No Implementation
    }


    @OptIn(KspExperimental::class)
    override fun visitClassDeclaration(
        classDeclaration: KSClassDeclaration,
        data: FileSpec.Builder?
    ) {
        if (isVisited(classDeclaration)) return
        val packageName = classDeclaration.packageName.asString()
        val className = classDeclaration.simpleName.asString()

        // Generate the module file
        val fileSpec = FileSpec.builder(packageName, "${className}Generated")
            .build()

        fileSpec.writeTo(
            codeGenerator = codeGenerator,
            aggregating = false,
            originatingKSFiles = listOf(classDeclaration.containingFile!!)
        )
    }
}
