package com.dev.ksp_processor

import com.dev.annotation.MetroInjectWith
import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getDeclaredFunctions
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.isConstructor
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.validate
import com.google.devtools.ksp.visitor.KSDefaultVisitor
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STAR
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toAnnotationSpec
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.writeTo
import dagger.Binds
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import javax.inject.Inject

class InjectWithProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {

    private val annotationName = MetroInjectWith::class.qualifiedName.toString()
    private val visitedSymbols = mutableSetOf<Any>()

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
                it.accept(InjectWithVisitor(codeGenerator, logger, visitedSymbols), null)
            }
        return unresolvedSymbols
    }
}

class InjectWithVisitor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val visitedSymbols: MutableSet<Any>
) : KSDefaultVisitor<FileSpec.Builder?, Unit>() {

    // Carousell core annotation
    private val featureScope by lazy {
        ClassName(
            "com.thecarousell.core.di",
            "FeatureScope",
        )
    }

    private val subcomponentScope by lazy {
        ClassName(
            "com.thecarousell.core.di.injector",
            "ActivityScope"
        )
    }

    private val appScope by lazy {
        ClassName(
            "com.thecarousell.core.di.injector",
            "AppScope",
        )
    }

    private val lifecycleExtensionImport by lazy {
        ClassName(
            "com.thecarousell.base.architecture.mvvm",
            "viewModel"
        )
    }

    private val featureInjectorClass by lazy {
        ClassName(
            "com.thecarousell.core.di.injector",
            "FeatureInjector"
        )
    }

    // metro annotations
    private val anvilMergeSubcomponent by lazy {
        ClassName(
            "com.squareup.anvil.annotations",
            "MergeSubcomponent",
        )
    }

    private val anvilContributesTo by lazy {
        ClassName(
            "com.squareup.anvil.annotations",
            "ContributesTo"
        )
    }

    private val contributesToAppAnnotation by lazy {
        AnnotationSpec.builder(
            anvilContributesTo
        )
            .addMember("scope = ${appScope.simpleName}::class")
            .build()
    }

    private val contributesToSubcomponent by lazy {
        AnnotationSpec.builder(
            anvilContributesTo
        )
            .addMember("scope = ${subcomponentScope.simpleName}::class")
            .build()
    }

    // android sdk lib
    private val appCompatActivity by lazy {
        ClassName(
            "androidx.appcompat.app",
            "AppCompatActivity",
        )
    }

    private val bottomSheetDialogFragment by lazy {
        ClassName(
            "com.google.android.material.bottomsheet",
            "BottomSheetDialogFragment",
        )
    }

    private val fragment by lazy {
        ClassName(
            "androidx.fragment.app",
            "Fragment",
        )
    }

    @OptIn(KspExperimental::class)
    override fun visitClassDeclaration(
        classDeclaration: KSClassDeclaration,
        data: FileSpec.Builder?
    ) {
        if (isVisited(classDeclaration)) return

        val packageName = classDeclaration.packageName.asString()
        if (!classDeclaration.isAnnotationPresent(MetroInjectWith::class)) return

        val fileSpec = FileSpec.builder(
            packageName,
            "${classDeclaration.simpleName.getShortName()}Component"
        )

        generateComponentContent(classDeclaration, fileSpec)
            .writeTo(
                codeGenerator = codeGenerator,
                aggregating = true,
                originatingKSFiles =
                    classDeclaration.containingFile?.let {
                        listOf(it)
                    }.orEmpty()
            )
    }

    fun generateComponentContent(
        classDeclaration: KSClassDeclaration,
        fileBuilder: FileSpec.Builder,
    ): FileSpec {
        val componentName = fileBuilder.name
        val resolveComponent = resolveInjectorComponent(classDeclaration)

        return fileBuilder
            .addImport(lifecycleExtensionImport.packageName, lifecycleExtensionImport.simpleName)
            .addImport(appScope.packageName, appScope.simpleName)
            .addImport(subcomponentScope.packageName, subcomponentScope.simpleName)
            .addSubcomponent(classDeclaration, componentName, resolveComponent)
            .addInjectorClass(classDeclaration, componentName)
            .addInjectorContributor(classDeclaration, componentName)
            .addViewModelModule(classDeclaration, componentName, resolveComponent)
            .build()
    }

    @Suppress("LongMethod")
    private fun FileSpec.Builder.addInjectorClass(
        clazz: KSClassDeclaration,
        componentName: String,
    ): FileSpec.Builder {
        val componentDependency = getComponentDependency(clazz)

        val dependency = componentDependency.className
        val injectTarget = ClassName(clazz.packageName.asString(), clazz.simpleName.getShortName())

        val factoryCreateFunction = if (componentDependency.isUnit) {
            "create(injectTarget)"
        } else {
            "create(injectTarget, dependencyFactory())"
        }

        addType(
            TypeSpec.classBuilder(
                ClassName(clazz.packageName.asString(), "${componentName}Injector")
            )
                .primaryConstructor(
                    FunSpec.constructorBuilder()
                        .addParameter(
                            ParameterSpec.builder(
                                "componentFactory",
                                ClassName(
                                    clazz.packageName.asString(),
                                    "$componentName.Factory"
                                )
                            )
                                .build()
                        )
                        .addAnnotation(Inject::class)
                        .build()
                )
                .addProperty(
                    PropertySpec.builder(
                        "componentFactory",
                        ClassName(
                            clazz.packageName.asString(),
                            "$componentName.Factory"
                        )
                    )
                        .initializer("componentFactory")
                        .addModifiers(KModifier.PRIVATE)
                        .build()
                )
                .addSuperinterface(
                    featureInjectorClass
                        .parameterizedBy(
                            injectTarget,
                            dependency
                        )
                )
                .addFunction(
                    FunSpec.builder("inject")
                        .addModifiers(KModifier.OVERRIDE)
                        .addParameter(ParameterSpec("injectTarget", injectTarget))
                        .addParameter(
                            ParameterSpec(
                                "dependencyFactory",
                                LambdaTypeName.get(
                                    returnType = dependency
                                )
                            )
                        )
                        .addStatement(
                            """
                            componentFactory
                            .$factoryCreateFunction
                            .inject(injectTarget)
                        """.trimIndent()
                        )
                        .build()
                )
                .build()
        )
        return this
    }

    private fun FileSpec.Builder.addInjectorContributor(
        clazz: KSClassDeclaration,
        componentName: String
    ): FileSpec.Builder {
        val injectComponent = "${componentName}InjectorComponent"
        addType(
            TypeSpec.interfaceBuilder(injectComponent)
                .addAnnotation(dagger.Module::class)
                .addAnnotation(contributesToAppAnnotation)
                .addFunction(
                    FunSpec.builder("bind$injectComponent")
                        .addAnnotation(IntoMap::class)
                        .addAnnotation(Binds::class)
                        .addAnnotation(
                            AnnotationSpec.builder(ClassKey::class)
                                .addMember("value = ${clazz.simpleName.getShortName()}::class")
                                .build()
                        )
                        .addParameter(
                            ParameterSpec.builder(
                                "injector",
                                ClassName(
                                    clazz.packageName.asString(),
                                    "${componentName}Injector"
                                )
                            )
                                .build()
                        )
                        .returns(
                            featureInjectorClass
                                .parameterizedBy(STAR, STAR)
                        )
                        .addModifiers(KModifier.ABSTRACT)
                        .build()
                )
                .build()
        )
        return this
    }

    private fun FileSpec.Builder.addViewModelModule(
        clazz: KSClassDeclaration,
        componentName: String,
        resolveComponent: ClassName,
    ): FileSpec.Builder {
        val annotation = clazz.annotations.firstOrNull()
        val viewModels = annotation?.classArrayArgument("viewModels").orEmpty()

        if (viewModels.isEmpty()) return this

        val viewModelModule = TypeSpec.classBuilder("${componentName}ViewModelModule")
            .addAnnotation(contributesToSubcomponent)
            .addAnnotation(Module::class)

        viewModels.forEach { viewModel ->
            viewModelModule
                .addViewModels(
                    resolveComponent = resolveComponent,
                    viewModel = viewModel
                )
        }

        addType(
            viewModelModule
                .build()
        )

        return this
    }

    private fun TypeSpec.Builder.addViewModels(
        resolveComponent: ClassName,
        viewModel: KSClassDeclaration
    ): TypeSpec.Builder {
        val viewModelDependencies =
            viewModel.getDeclaredFunctions()
                .filter { it.isConstructor() }
                .flatMap { it.parameters }
                .mapNotNull { parameter ->
                    val classReference =
                        parameter.type.resolve().resolveKSClassDeclaration()
                            ?: return@mapNotNull null
                    val propertyName = parameter.name?.getShortName() ?: return@mapNotNull null
                    ClazzReference(
                        packageName = classReference.packageName.asString(),
                        clazzName = classReference.simpleName.getShortName(),
                        propertyName = propertyName
                    ) to parameter.annotations
                }

        val dependenciesParameters = viewModelDependencies.map { (dependency, annotations) ->
            ParameterSpec.builder(
                name = dependency.propertyName.decapitalize(),
                type = dependency.className
            )
                .addAnnotations(
                    annotations.toList().map { it.toAnnotationSpec() }
                )
                .build()
        }

        val dependenciesFunction = dependenciesParameters.joinToString(",") { dependency ->
            "${dependency.name} = ${dependency.name}"
        }
        val dependencyCodeBlock = """
            return ${resolveComponent.simpleName.decapitalize()}
              .viewModel {
              ${viewModel.simpleName.getShortName()}(
                $dependenciesFunction
              )
            }"""
            .trimIndent()

        addFunction(
            FunSpec.builder("provide${viewModel.simpleName.getShortName()}")
                .addParameter(
                    ParameterSpec(
                        resolveComponent.simpleName.decapitalize(),
                        resolveComponent
                    )
                )
                .addAnnotation(Provides::class)
                .addAnnotation(featureScope)
                .addParameters(dependenciesParameters.toList())
                .returns(viewModel.toClassName())
                .addStatement(
                    dependencyCodeBlock
                )
                .build()
        )
        return this
    }

    private fun resolveInjectorComponent(
        classDeclaration: KSClassDeclaration,
    ): ClassName {
        val clazzParents = classDeclaration.getAllParentClasses().map {
            it.toClassName()
        }

        return when {
            clazzParents.contains(appCompatActivity) -> appCompatActivity
            clazzParents.contains(bottomSheetDialogFragment) -> bottomSheetDialogFragment
            clazzParents.contains(fragment) -> fragment
            else -> throw Exception(
                "cannot find mappings of $clazzParents"
            )
        }
    }

    private fun FileSpec.Builder.addSubcomponent(
        clazz: KSClassDeclaration,
        componentName: String,
        resolveComponent: ClassName,
    ): FileSpec.Builder {
        val annotation = clazz.annotations.firstOrNull()

        val modules = annotation?.classArrayArgument("modules").orEmpty()
        val moduleValues = "[" + modules.joinToString(",") { module ->
            "${module}::class"
        } + "]"

        modules.forEach { module ->
            addImport(
                module.packageName.asString(),
                module.simpleName.getShortName()
            )
        }

        addType(
            TypeSpec.interfaceBuilder(componentName)
                // add @FeatureScope
                .addAnnotation(
                    AnnotationSpec.builder(
                        featureScope,
                    ).build()
                )
                // @MergeSubcomponent
                .addAnnotation(
                    AnnotationSpec.builder(
                        anvilMergeSubcomponent
                    )
                        .addMember("scope = ${subcomponentScope.simpleName}::class")
                        .addMember("modules = $moduleValues")
                        .build()
                )
                .addFunction(
                    FunSpec.builder("inject")
                        .addModifiers(KModifier.ABSTRACT)
                        .addParameter(
                            ParameterSpec.builder(
                                clazz.simpleName.getShortName().decapitalize(),
                                clazz.toClassName(),
                            ).build()
                        )
                        .build()
                )
                .addSubcomponentFactory(resolveComponent, clazz, componentName)
                .addParentComponent(componentName)
                .build()
        )

        return this
    }

    private fun TypeSpec.Builder.addParentComponent(
        componentName: String
    ): TypeSpec.Builder {
        addType(
            TypeSpec.interfaceBuilder("ParentComponent")
                .addAnnotation(contributesToAppAnnotation)
                .addFunction(
                    FunSpec.builder("create$componentName")
                        .addModifiers(KModifier.ABSTRACT)
                        .returns(ClassName("", "Factory"))
                        .build()
                )
                .build()
        )
        return this
    }

    private fun TypeSpec.Builder.addSubcomponentFactory(
        resolveComponent: ClassName,
        clazz: KSClassDeclaration,
        componentName: String
    ): TypeSpec.Builder {
        val componentDependency = getComponentDependency(clazz)
        val functionBuilder = FunSpec.builder("create")
            .returns(ClassName(clazz.packageName.asString(), componentName))
            .addModifiers(KModifier.ABSTRACT)
            .addParameter(
                ParameterSpec.builder(
                    clazz.simpleName.getShortName().decapitalize(),
                    resolveComponent
                )
                    .addAnnotation(BindsInstance::class)
                    .build()
            )

        if (componentDependency.isUnit.not()) {
            functionBuilder.addParameter(
                ParameterSpec.builder(
                    componentDependency.clazzName.decapitalize(),
                    componentDependency.className
                )
                    .addAnnotation(BindsInstance::class)
                    .build()
            )
        }

        addType(
            TypeSpec.interfaceBuilder("Factory")
                .addAnnotation(Subcomponent.Factory::class)
                .addFunction(
                    functionBuilder.build()
                )
                .build()
        )
        return this
    }

    private fun getComponentDependency(
        clazz: KSClassDeclaration
    ): ClazzReference {
        val annotation = clazz.annotations.firstOrNull() ?: return ClazzReference(
            clazzName = "Unit",
            packageName = clazz.packageName.asString()
        )

        return annotation.argumentOfTypeWithMapperAt<KSType, KSClassDeclaration?>("dependency") { arg, value ->
            value.resolveKSClassDeclaration()
        }?.let { kSClassDeclaration ->
            ClazzReference(
                packageName = kSClassDeclaration.packageName.asString(),
                clazzName = kSClassDeclaration.simpleName.getShortName(),
            )
        } ?: ClazzReference(
            clazzName = "Unit",
            packageName = clazz.packageName.asString()
        )
    }

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
}