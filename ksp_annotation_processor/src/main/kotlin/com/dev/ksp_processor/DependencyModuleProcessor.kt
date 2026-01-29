package com.dev.ksp_processor

import com.dev.annotation.ProvidesDependencyModule
import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.symbol.KSType
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
import kotlin.collections.mutableSetOf

// this is magic number, adjust based on the required
private const val VALID_NESTED_BRACE_COUNT = 8

class DependencyModuleProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val useMetro: Boolean,
) : SymbolProcessor {

    private val visitedSymbols = mutableSetOf<Any>()
    private val annotationName = ProvidesDependencyModule::class.qualifiedName.toString()

    private val visitedFunctions = mutableSetOf<String>()

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
                    DependencyModuleProcessorVisitor(
                        codeGenerator,
                        logger,
                        visitedSymbols,
                        useMetro,
                        visitedFunctions
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
    private val useMetro: Boolean,
    private val visitedFunctions: MutableSet<String>
) : KSDefaultVisitor<FileSpec.Builder?, Unit>() {

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

        // Get all functions from the interface
        val functions = classDeclaration.getAllFunctions()
            .filter { it.parentDeclaration == classDeclaration }
            .toList()

        // Generate companion object with the functions
        val companionObject = TypeSpec.companionObjectBuilder()

        val fileContent = try {
            val containingFile = classDeclaration.containingFile
            val sourceFile = containingFile?.filePath.orEmpty()
            java.io.File(sourceFile).readText()
        } catch (_: Exception) {
            throw Exception("")
        }

        functions.forEach { function ->
            val functionName = function.simpleName.asString()
            if (visitedFunctions.contains(functionName)) {
                throw IllegalArgumentException(
                    "duplicated function is not allowed even though different parameters, functionName: $functionName"
                )
            }
            val generatedFunction = generateFunction(function, fileContent)
            companionObject.addFunction(generatedFunction)
            visitedFunctions.add(functionName)
        }

        // Generate the extended interface with companion object
        val interfaceSpec = TypeSpec.interfaceBuilder("${className}Generated")
            .addType(companionObject.build())

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

    private fun generateFunction(
        function: KSFunctionDeclaration,
        fileContent: String
    ): FunSpec {
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

        // Extract and add function body from source code
        val functionBody = extractFunctionBody(function, fileContent)
        funSpec.addCode(functionBody)

        return funSpec.build()
    }

    private fun extractAnnotationsFromDoc(docString: String): List<AnnotationSpec> {
        val annotations = mutableListOf<AnnotationSpec>()
        // Updated regex to handle annotations like @Key("value") or @Key(Class::class)
        val annotationPattern = """@([\w.]+)\(([^)]+)\)""".toRegex()

        annotationPattern.findAll(docString).forEach { match ->
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

    private fun extractFunctionBody(function: KSFunctionDeclaration, fileContent: String): String {
        val functionName = function.simpleName.asString()

        // Create regex pattern to find the specific function by function name
        val pattern =
            """fun\s+$functionName\s*\([^)]*\)[^{]*\{""".toRegex(RegexOption.DOT_MATCHES_ALL)

        val match = pattern.find(fileContent)
            ?: throw RuntimeException("Cannot find matching function body of $functionName")

        // Find matching closing brace
        val startIndex = match.range.last + 1
        var braceCount = 1
        var endIndex = startIndex

        // traverse until we find balanced '{' & '}'
        while (endIndex < fileContent.length && braceCount > 0) {
            when (fileContent[endIndex]) {
                '{' -> braceCount++
                '}' -> braceCount--
            }
            if (braceCount > 0) endIndex++
            if (braceCount > VALID_NESTED_BRACE_COUNT) {
                throw IllegalArgumentException("Too nested braces '{', adjust VALID_NESTED_BRACE_COUNT if required")
            }
        }

        return if (braceCount == 0) {
            fileContent.substring(startIndex, endIndex).trimIndent()
        } else {
            throw IllegalArgumentException("cannot find end of '{' of the current function $functionName, please always wrap with enclosing braces {}")
        }
    }

    override fun defaultHandler(
        node: KSNode,
        data: FileSpec.Builder?
    ) {
        // No Implementation
    }

}
