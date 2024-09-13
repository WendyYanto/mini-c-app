package com.dev.annotation_processor

import com.dev.annotation.InjectWith
import com.google.auto.service.AutoService
import com.squareup.anvil.annotations.ContributesTo
import com.squareup.anvil.annotations.ExperimentalAnvilApi
import com.squareup.anvil.annotations.MergeSubcomponent
import com.squareup.anvil.compiler.api.AnvilContext
import com.squareup.anvil.compiler.api.CodeGenerator
import com.squareup.anvil.compiler.api.GeneratedFile
import com.squareup.anvil.compiler.api.createGeneratedFile
import com.squareup.anvil.compiler.internal.decapitalize
import com.squareup.anvil.compiler.internal.reference.ClassReference
import com.squareup.anvil.compiler.internal.reference.argumentAt
import com.squareup.anvil.compiler.internal.reference.asClassName
import com.squareup.anvil.compiler.internal.reference.classAndInnerClassReferences
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
import dagger.Binds
import dagger.BindsInstance
import dagger.Module
import dagger.Subcomponent
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import org.jetbrains.kotlin.descriptors.ModuleDescriptor
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.psi.KtFile
import java.io.File
import javax.inject.Inject

@OptIn(ExperimentalAnvilApi::class)
@AutoService(CodeGenerator::class)
class InjectWithProcessor : CodeGenerator {

    companion object {

        private const val INJECT_WITH_ANNOTATION = "com.dev.annotation.InjectWith"
    }

    private val featureScope by lazy {
        AnnotationReference(
            annotationName = "FeatureScope",
            packageName = "com.dev.core.scope"
        )
    }

    private val subcomponentScope by lazy {
        ClazzReference(
            clazzName = "ActivityScope",
            packageName = "com.dev.core.scope"
        )
    }

    private val appScope by lazy {
        ClazzReference(
            clazzName = "AppScope",
            packageName = "com.dev.core.scope"
        )
    }

    private val lifecycleExtensionImport by lazy {
        ExtensionReference(
            functionName = "viewModel",
            packageName = "com.dev.core.mvvm"
        )
    }

    private val appCompatActivity by lazy {
        ClazzReference(
            clazzName = "AppCompatActivity",
            packageName = "androidx.appcompat.app"
        )
    }

    private val contributesToAppAnnotation by lazy {
        AnnotationSpec.builder(
            ContributesTo::class
        )
            .addMember("scope = ${appScope.clazzName}::class")
            .build()
    }

    private val featureInjectorClass by lazy {
        ClazzReference(
            clazzName = "FeatureInjector",
            packageName = "com.dev.core.injector"
        )
    }

    override fun isApplicable(context: AnvilContext): Boolean = true

    override fun generateCode(
        codeGenDir: File,
        module: ModuleDescriptor,
        projectFiles: Collection<KtFile>
    ): List<GeneratedFile> {
        return projectFiles
            .classAndInnerClassReferences(module)
            .filter { it.isAnnotatedWith(FqName(INJECT_WITH_ANNOTATION)) }
            .map { clazz ->
                createGeneratedFile(
                    codeGenDir = codeGenDir,
                    packageName = clazz.packageFqName.asString(),
                    fileName = "${clazz.shortName}Component",
                    content = generateComponentContent(clazz)
                )
            }.toList()
    }

    private fun generateComponentContent(clazz: ClassReference.Psi): String {
        val componentName = "${clazz.shortName}Component"
        val fileBuilder = FileSpec.builder(clazz.packageFqName.asString(), componentName)

        return fileBuilder
            .addImport(lifecycleExtensionImport.packageName, lifecycleExtensionImport.functionName)
            .addImport(appScope.packageName, appScope.clazzName)
            .addType(createSubcomponent(clazz, componentName))
            .addInjectorClass(clazz, componentName)
            .addInjectorContributor(clazz, componentName)
            .addViewModelModule(clazz, componentName)
            .build()
            .toString()
    }

    private fun resolveInjectorComponent(
        clazz: ClassReference.Psi
    ): ClazzReference {
        return when (val clazzParent = clazz.clazz.getSuperTypeList()?.text) {
            appCompatActivity.superClassName -> appCompatActivity
            // ToDo: add fragment
            // ToDo: add bottomSheetDialog
            else -> throw Exception("cannot resolve parent $clazzParent")
        }
    }

    private fun createSubcomponent(
        clazz: ClassReference.Psi,
        componentName: String
    ): TypeSpec {
        val resolveComponent = resolveInjectorComponent(clazz)
        return TypeSpec.interfaceBuilder(componentName)
            // add @FeatureScope
            .addAnnotation(
                AnnotationSpec.builder(
                    featureScope.className
                ).build()
            )
            // @MergeSubcomponent
            .addAnnotation(
                AnnotationSpec.builder(
                    MergeSubcomponent::class
                )
                    .addMember("scope = ${appScope.clazzName}::class")
                    .build()
            )
            .addFunction(
                FunSpec.builder("inject")
                    .addModifiers(KModifier.ABSTRACT)
                    .addParameter(
                        ParameterSpec.builder(
                            clazz.shortName.decapitalize(),
                            ClassName(
                                clazz.packageFqName.asString(),
                                clazz.shortName
                            ),
                        ).build()
                    )
                    .build()
            )
            .addSubcomponentFactory(resolveComponent, clazz, componentName)
            .addParentComponent(componentName)
            .build()
    }

    private fun TypeSpec.Builder.addSubcomponentFactory(
        resolveComponent: ClazzReference,
        clazz: ClassReference.Psi,
        componentName: String
    ): TypeSpec.Builder {
        addType(
            TypeSpec.interfaceBuilder("Factory")
                .addAnnotation(Subcomponent.Factory::class)
                .addFunction(
                    FunSpec.builder("create")
                        .returns(ClassName("", componentName))
                        .addModifiers(KModifier.ABSTRACT)
                        .addParameter(
                            ParameterSpec.builder(
                                clazz.shortName.decapitalize(),
                                resolveComponent.poetClassName
                            )
                                .addAnnotation(BindsInstance::class)
                                .build()
                        )
                        .build()
                )
                .build()
        )
        return this
    }

    private fun TypeSpec.Builder.addParentComponent(
        componentName: String
    ): TypeSpec.Builder {
        addType(
            TypeSpec.interfaceBuilder("ParentComponent")
//                .addAnnotation(contributesToAppAnnotation)
                .addFunction(
                    FunSpec.builder("create${componentName}")
                        .addModifiers(KModifier.ABSTRACT)
                        .returns(ClassName("", "Factory"))
                        .build()
                )
                .build()
        )
        return this
    }

    private fun FileSpec.Builder.addInjectorClass(
        clazz: ClassReference.Psi,
        componentName: String,
    ): FileSpec.Builder {
        val annotation = clazz.annotations[0]
        val componentDependency =
            annotation
                .argumentAt("dependency", -1)?.value<List<ClassReference>>()
                .orEmpty()
                .filter { it.shortName != "Unit" }
                .map {
                    ClazzReference(
                        clazzName = it.shortName,
                        packageName = it.packageFqName.asString()
                    )
                }.firstOrNull() ?: ClazzReference(
                clazzName = "Unit",
                packageName = clazz.packageFqName.asString()
            )

        val dependency = componentDependency.poetClassName
        val injectTarget = ClassName(clazz.packageFqName.asString(), clazz.shortName)

        val factoryCreateFunction = if (componentDependency.isUnit) {
            "create(injectTarget)"
        } else {
            "create(injectTarget, dependencyFactory())"
        }

        addType(
            TypeSpec.classBuilder(
                ClassName("", "${componentName}Injector")
            )
                .primaryConstructor(
                    FunSpec.constructorBuilder()
                        .addParameter(
                            ParameterSpec.builder(
                                "componentFactory",
                                ClassName(
                                    clazz.packageFqName.asString(),
                                    "${componentName}.Factory"
                                )
                            )
                                .build()
                        )
                        .addAnnotation(Inject::class)
                        .build()
                )
                .addProperty(
                    PropertySpec.builder(
                        "componentFactory", ClassName(
                            clazz.packageFqName.asString(),
                            "${componentName}.Factory"
                        )
                    )
                        .initializer("componentFactory")
                        .addModifiers(KModifier.PRIVATE)
                        .build()
                )
                .addSuperinterface(
                    featureInjectorClass.poetClassName
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
                            .${factoryCreateFunction}
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
        clazz: ClassReference.Psi,
        componentName: String
    ): FileSpec.Builder {
        val injectComponent = "${componentName}InjectorComponent"
        addType(
            TypeSpec.interfaceBuilder(injectComponent)
                .addAnnotation(Module::class)
//                .addAnnotation(contributesToAppAnnotation)
                .addFunction(
                    FunSpec.builder("bind${injectComponent}")
                        .addAnnotation(IntoMap::class)
                        .addAnnotation(Binds::class)
                        .addAnnotation(
                            AnnotationSpec.builder(ClassKey::class)
                                .addMember("value = ${clazz.shortName}::class")
                                .build()
                        )
                        .addParameter(
                            ParameterSpec.builder(
                                "injector", ClassName(
                                    clazz.packageFqName.asString(),
                                    "${componentName}Injector"
                                )
                            )
                                .build()
                        )
                        .returns(
                            featureInjectorClass.poetClassName
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
        clazz: ClassReference.Psi,
        componentName: String
    ): FileSpec.Builder {
        val annotation = clazz.annotations.firstOrNull()
        val viewModel = annotation
            ?.argumentAt("viewModels", -1)?.value<List<ClassReference>>()
            ?.firstOrNull()

        if (viewModel == null) return this

        val viewModelDependencies =
            viewModel.constructors
                .flatMap { it.parameters }
                .map { parameter ->
                    val classReference = parameter.type().asClassReference()
                    ClazzReference(
                        packageName = classReference.packageFqName.asString(),
                        clazzName = classReference.shortName
                    )
                }

        val resolveComponent = resolveInjectorComponent(clazz)
        val dependenciesParameters = viewModelDependencies.map { dependency ->
            ParameterSpec(dependency.clazzName.decapitalize(), dependency.poetClassName)
        }
        val dependenciesFunction = dependenciesParameters.map { dependency ->
            "${dependency.name} = ${dependency.name}"
        }.joinToString(",")
        val dependencyCodeBlock = """
            return ${resolveComponent.clazzName.decapitalize()}.viewModel {
              ${viewModel.shortName}(
                $dependenciesFunction
              )
            }""".trimIndent()

        addType(
            TypeSpec.classBuilder("${componentName}ViewModelModule")
//                .addAnnotation(contributesToAppAnnotation)
                .addAnnotation(Module::class)
                .addFunction(
                    FunSpec.builder("provideViewModel")
                        .addParameter(
                            ParameterSpec(
                                resolveComponent.clazzName.decapitalize(),
                                resolveComponent.poetClassName
                            )
                        )
                        .addParameters(dependenciesParameters)
                        .returns(viewModel.asClassName())
                        .addStatement(
                            dependencyCodeBlock
                        )
                        .build()
                )
                .build()
        )
        return this
    }

    data class ClazzReference(
        val clazzName: String,
        val packageName: String
    ) {

        val poetClassName
            get() = ClassName(packageName, clazzName)

        val superClassName
            get() = "$clazzName()"

        val isUnit =
            clazzName == "Unit"
    }

    data class AnnotationReference(
        val annotationName: String,
        val packageName: String
    ) {

        val className
            get() = ClassName(packageName, annotationName)
    }

    data class ExtensionReference(
        val functionName: String,
        val packageName: String
    )
}