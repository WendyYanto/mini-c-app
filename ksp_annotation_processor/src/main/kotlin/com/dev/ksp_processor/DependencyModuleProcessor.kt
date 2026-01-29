package com.dev.ksp_processor

import com.dev.annotation.DependencyModule
import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.validate
import com.google.devtools.ksp.visitor.KSDefaultVisitor
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo

class DependencyModuleProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val useMetro: Boolean,
) : SymbolProcessor {

    private val visitedSymbols = mutableSetOf<Any>()
    private val annotationName = DependencyModule::class.qualifiedName.toString()

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
                    DependencyModuleProcessorVisitor(
                        codeGenerator,
                        logger,
                        visitedSymbols,
                        useMetro
                    ),
                    null
                )
            }

        return unresolvedSymbols
    }
}

class DependencyModuleProcessorVisitor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val visitedSymbols: MutableSet<Any>,
    private val useMetro: Boolean
) : KSDefaultVisitor<FileSpec.Builder?, Unit>() {

    private fun isVisited(symbol: KSNode): Boolean {
        if (visitedSymbols.contains(symbol)) return true
        visitedSymbols.add(symbol)
        return false
    }

    @OptIn(KspExperimental::class)
    override fun visitClassDeclaration(
        classDeclaration: KSClassDeclaration,
        data: FileSpec.Builder?
    ) {
        if (isVisited(classDeclaration)) return
        val packageName = classDeclaration.packageName.asString()
        val className = classDeclaration.simpleName.asString()

        // Get all functions from the interface
        val functions = classDeclaration.getAllFunctions()
            .filter { it.parentDeclaration == classDeclaration }
            .toList()

        // Generate companion object with the functions
        val companionObject = TypeSpec.companionObjectBuilder()

        functions.forEach { function ->
            val generatedFunction = generateFunction(function)
            companionObject.addFunction(generatedFunction)
        }

        // Generate the extended interface with companion object
        val interfaceSpec = TypeSpec.interfaceBuilder("${className}Generated")
            .addType(companionObject.build())
            .build()

        // Generate the module file
        val fileSpec = FileSpec.builder(packageName, "${className}Generated")
            .addType(interfaceSpec)
            .build()

        fileSpec.writeTo(
            codeGenerator = codeGenerator,
            aggregating = false,
            originatingKSFiles = listOf(classDeclaration.containingFile!!)
        )
    }

    private fun generateFunction(function: KSFunctionDeclaration): FunSpec {
        val functionName = function.simpleName.asString()
        val returnType = function.returnType?.resolve()?.toTypeName()

        val funSpec = FunSpec.builder(functionName)

        // Add return type if present
        if (returnType != null) {
            funSpec.returns(returnType)
        }

        // Add parameters
        function.parameters.forEach { param ->
            val paramName = param.name?.asString() ?: ""
            val paramType = param.type.resolve().toTypeName()
            funSpec.addParameter(
                ParameterSpec.builder(paramName, paramType).build()
            )
        }

        // Extract annotations from doc comments
        val docString = function.docString

        if (docString != null) {
            val annotations = extractAnnotationsFromDoc(docString)
            annotations.forEach { annotation ->
                funSpec.addAnnotation(annotation)
            }

            if (useMetro) {
                funSpec.addAnnotation(
                    AnnotationSpec.builder(dev.zacsweers.metro.Provides::class)
                        .build()
                )
                funSpec.addAnnotation(
                    AnnotationSpec.builder(dev.zacsweers.metro.IntoMap::class)
                        .build()
                )
            } else {
                funSpec.addAnnotation(
                    AnnotationSpec.builder(
                        ClassName("dagger", "Provides")
                    )
                        .build()
                )
                funSpec.addAnnotation(
                    AnnotationSpec.builder(
                        ClassName("dagger.multibindings", "IntoMap")
                    )
                        .build()
                )
            }
        }

        // Add a simple return statement (can be customized based on needs)
        funSpec.addStatement("TODO(\"Not yet implemented\")")

        return funSpec.build()
    }

    private fun extractAnnotationsFromDoc(docString: String): List<AnnotationSpec> {
        val annotations = mutableListOf<AnnotationSpec>()
        // Updated regex to handle annotations like @Key("value") or @Key(Class::class)
        val annotationPattern = """@([\w.]+)\(([^)]+)\)""".toRegex()

        println("ASDASd")
        println(docString)

        annotationPattern.findAll(docString).forEach { match ->
            println("ASDASdAAAS")
            println(match)

            val annotationName = match.groupValues[1]
            val annotationParams = match.groupValues[2].trim()

            // Parse the annotation parameters
            val annotationSpec = when {
                annotationParams.contains("::class") -> {
                    // Handle KClass parameters like @TextLoaderKey(CoreTextLoader::class)
                    AnnotationSpec.builder(ClassName.bestGuess(annotationName))
                        .addMember("%L", annotationParams)
                        .build()
                }

                annotationParams.startsWith("\"") && annotationParams.endsWith("\"") -> {
                    // Handle string parameters like @Key("value")
                    AnnotationSpec.builder(ClassName.bestGuess(annotationName))
                        .addMember("%S", annotationParams.removeSurrounding("\""))
                        .build()
                }

                annotationParams.isNotEmpty() -> {
                    // Handle other parameters
                    AnnotationSpec.builder(ClassName.bestGuess(annotationName))
                        .addMember("%L", annotationParams)
                        .build()
                }

                else -> {
                    // No parameters
                    AnnotationSpec.builder(ClassName.bestGuess(annotationName))
                        .build()
                }
            }

            annotations.add(annotationSpec)
        }

        return annotations
    }

    override fun defaultHandler(
        node: KSNode,
        data: FileSpec.Builder?
    ) {
        // No Implementation
    }

}
