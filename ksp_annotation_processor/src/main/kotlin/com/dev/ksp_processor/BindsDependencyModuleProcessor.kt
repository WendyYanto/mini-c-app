package com.dev.ksp_processor

import com.dev.annotation.BindsDependencyModule
import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.validate
import com.google.devtools.ksp.visitor.KSDefaultVisitor
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo
import dev.zacsweers.metro.StringKey

class BindsDependencyModuleProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val useMetro: Boolean,
) : SymbolProcessor {

    private val visitedSymbols = mutableSetOf<Any>()
    private val annotationName = BindsDependencyModule::class.qualifiedName.toString()

    private val visitedProperties = mutableSetOf<String>()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        visitedSymbols.clear()
        val (resolvedSymbols, unresolvedSymbols) = resolver.getSymbolsWithAnnotation(annotationName)
            .partition { it.validate() }

        resolvedSymbols
            .mapNotNull { ksAnnotated ->
                when (ksAnnotated) {
                    is KSClassDeclaration -> ksAnnotated
                    else -> null
                }
            }
            .forEach {
                it.accept(
                    BindsDependencyModuleProcessorVisitor(
                        codeGenerator,
                        logger,
                        visitedSymbols,
                        useMetro,
                        visitedProperties
                    ),
                    null
                )
            }

        return unresolvedSymbols
    }
}

class BindsDependencyModuleProcessorVisitor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val visitedSymbols: MutableSet<Any>,
    private val useMetro: Boolean,
    private val visitedProperties: MutableSet<String>
) : KSDefaultVisitor<FileSpec.Builder?, Unit>() {

    private val metroPackageName = "dev.zacsweers.metro"
    private val daggerBindingsPackageName = "dagger.multibindings"

    private val anvilContributesTo by lazy {
        ClassName(
            "com.squareup.anvil.annotations",
            "ContributesTo"
        )
    }

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

        val fileSpec = FileSpec.builder(packageName, "${className}Generated")

        // Copy all imports from the original file
        val fileContent = try {
            val containingFile = classDeclaration.containingFile
            val sourceFile = containingFile?.filePath.orEmpty()
            java.io.File(sourceFile).readText()
        } catch (_: Exception) {
            ""
        }

        if (fileContent.isNotEmpty()) {
            val imports = extractImportsFromFile(fileContent)
            imports.forEach { (packageName, simpleName) ->
                fileSpec.addImport(packageName, simpleName)
            }
        }

        // Get all properties from the interface
        val properties = classDeclaration.getAllProperties()
            .filter { it.parentDeclaration == classDeclaration }
            .filter { it.extensionReceiver != null } // Only extension properties
            .toList()

        // Generate the interface with extension properties or functions
        val interfaceSpec = TypeSpec.interfaceBuilder("${className}Generated")

        if (useMetro) {
            fileSpec.addImport(metroPackageName, "StringKey")
            fileSpec.addImport(metroPackageName, "IntKey")
            // Generate extension properties for Metro
            properties.forEach { property ->
                val generatedProperty = generateProperty(property)
                interfaceSpec.addProperty(generatedProperty)
            }
        } else {
            fileSpec.addImport(daggerBindingsPackageName, "StringKey")
            fileSpec.addImport(daggerBindingsPackageName, "IntKey")
            // Generate abstract functions for Dagger
            properties.forEach { property ->
                val generatedFunction = generateFunction(property)
                interfaceSpec.addFunction(generatedFunction)
            }
        }

        val annotation = classDeclaration.annotations.firstOrNull()
        val scope =
            annotation?.argumentOfTypeWithMapperAt<KSType, KSClassDeclaration?>("scope") { arg, value ->
                value.resolveKSClassDeclaration()
            }?.let { kSClassDeclaration ->
                ClazzReference(
                    packageName = kSClassDeclaration.packageName.asString(),
                    clazzName = kSClassDeclaration.simpleName.getShortName(),
                )
            } ?: throw Exception("Unable to map scope class")

        fileSpec.addImport(scope.packageName, scope.clazzName)

        if (useMetro) {
            interfaceSpec.addAnnotation(
                AnnotationSpec.builder(dev.zacsweers.metro.BindingContainer::class)
                    .build()
            )
            interfaceSpec.addAnnotation(
                AnnotationSpec.builder(dev.zacsweers.metro.ContributesTo::class)
                    .addMember("scope = ${scope.clazzName}::class")
                    .build()
            )
        } else {
            interfaceSpec.addAnnotation(
                AnnotationSpec.builder(dagger.Module::class)
                    .build()
            )
            interfaceSpec.addAnnotation(
                AnnotationSpec.builder(
                    anvilContributesTo
                )
                    .addMember("scope = ${scope.clazzName}::class")
                    .build()
            )
        }

        // Generate the module file
        fileSpec
            .addType(
                interfaceSpec
                    .build()
            )

        fileSpec
            .build()
            .writeTo(
                codeGenerator = codeGenerator,
                aggregating = false,
                originatingKSFiles = listOf(classDeclaration.containingFile!!)
            )
    }

    private fun generateProperty(
        property: KSPropertyDeclaration,
    ): PropertySpec {
        val propertyName = property.simpleName.asString()
        val propertyType = property.type.resolve().toTypeName()
        val extensionReceiver = property.extensionReceiver?.resolve()?.toTypeName()
            ?: throw IllegalArgumentException("Property $propertyName must be an extension property")

        val propertySpec = PropertySpec.builder(propertyName, propertyType)
            .receiver(extensionReceiver)

        // Extract annotations from doc comments
        val docString = property.docString

        if (docString != null) {
            val annotations = extractAnnotationsFromDoc(docString, useMetro)
            annotations.forEach { annotation ->
                propertySpec.addAnnotation(annotation)
            }

            if (useMetro) {
                propertySpec.addAnnotation(
                    AnnotationSpec.builder(dev.zacsweers.metro.Binds::class)
                        .build()
                )
                propertySpec.addAnnotation(
                    AnnotationSpec.builder(dev.zacsweers.metro.IntoMap::class)
                        .build()
                )
            } else {
                propertySpec.addAnnotation(
                    AnnotationSpec.builder(
                        ClassName("dagger", "Binds")
                    )
                        .build()
                )
                propertySpec.addAnnotation(
                    AnnotationSpec.builder(
                        ClassName("dagger.multibindings", "IntoMap")
                    )
                        .build()
                )
            }
        }

        return propertySpec.build()
    }

    private fun generateFunction(
        property: KSPropertyDeclaration,
    ): FunSpec {
        val propertyName = property.simpleName.asString()
        val propertyType = property.type.resolve().toTypeName()
        val extensionReceiver = property.extensionReceiver?.resolve()
            ?: throw IllegalArgumentException("Property $propertyName must be an extension property")

        val extensionReceiverTypeName = extensionReceiver.toTypeName()

        // Extract the simple name from the extension receiver type for the function name
        val extensionReceiverSimpleName = extensionReceiver.declaration.simpleName.asString()

        // Generate function name: binds + capitalized extension receiver type name
        val functionName = "${propertyName}${extensionReceiverSimpleName}"

        val funSpec = FunSpec.builder(functionName)
            .addModifiers(KModifier.ABSTRACT)
            .addParameter(
                ParameterSpec.builder("binder", extensionReceiverTypeName).build()
            )
            .returns(propertyType)

        // Extract annotations from doc comments
        val docString = property.docString

        if (docString != null) {
            val annotations = extractAnnotationsFromDoc(docString, useMetro)
            annotations.forEach { annotation ->
                funSpec.addAnnotation(annotation)
            }

            funSpec.addAnnotation(
                AnnotationSpec.builder(
                    ClassName("dagger", "Binds")
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

        return funSpec.build()
    }

    private fun extractImportsFromFile(fileContent: String): List<Pair<String, String>> {
        val imports = mutableListOf<Pair<String, String>>()
        val importPattern = """import\s+([\w.]+)\.([\w]+)""".toRegex()

        importPattern.findAll(fileContent).forEach { match ->
            val packageName = match.groupValues[1]
            val simpleName = match.groupValues[2]
            imports.add(packageName to simpleName)
        }

        return imports
    }

    private fun extractAnnotationsFromDoc(
        docString: String,
        useMetro: Boolean
    ): List<AnnotationSpec> {
        val annotations = mutableListOf<AnnotationSpec>()
        // Updated regex to handle annotations like @Key("value") or @Key(Class::class)
        val annotationPattern = """@([\w.]+)\(([^)]+)\)""".toRegex()

        annotationPattern.findAll(docString).forEach { match ->
            val annotationName = match.groupValues[1]
            val annotationParams = match.groupValues[2].trim()

            // Parse the annotation parameters
            val annotationSpec = when {
                annotationParams.contains("::class") -> {
                    // Handle KClass parameters like @WorkerKey(LocalPushWorker::class)
                    AnnotationSpec.builder(ClassName.bestGuess(annotationName))
                        .addMember("%L", annotationParams)
                        .build()
                }

                // its StringKey
                annotationParams.startsWith("\"") && annotationParams.endsWith("\"") -> {
                    // Handle string parameters like @Key("value")
                    val annotationSpec = if (useMetro) {
                        AnnotationSpec.builder(StringKey::class)
                    } else {
                        AnnotationSpec.builder(dagger.multibindings.StringKey::class)
                    }

                    annotationSpec.addMember("%S", annotationParams.removeSurrounding("\"")).build()
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

            annotationSpec.let {
                annotations.add(it)
            }
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