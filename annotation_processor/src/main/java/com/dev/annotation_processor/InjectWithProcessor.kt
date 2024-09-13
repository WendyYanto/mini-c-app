package com.dev.annotation_processor

import com.google.auto.service.AutoService
import com.squareup.anvil.annotations.ExperimentalAnvilApi
import com.squareup.anvil.annotations.MergeSubcomponent
import com.squareup.anvil.compiler.api.AnvilContext
import com.squareup.anvil.compiler.api.CodeGenerator
import com.squareup.anvil.compiler.api.GeneratedFile
import com.squareup.anvil.compiler.api.createGeneratedFile
import com.squareup.anvil.compiler.internal.decapitalize
import com.squareup.anvil.compiler.internal.reference.ClassReference
import com.squareup.anvil.compiler.internal.reference.classAndInnerClassReferences
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.TypeSpec
import dagger.BindsInstance
import dagger.Subcomponent
import org.jetbrains.kotlin.descriptors.ModuleDescriptor
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.psi.KtFile
import java.io.File

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
        val annotation = clazz.annotations[0]

        val componentName = "${clazz.shortName}Component"
        val fileBuilder = FileSpec.builder(clazz.packageFqName.asString(), componentName)

        return fileBuilder
            .addImport(lifecycleExtensionImport.packageName, lifecycleExtensionImport.functionName)
            .addImport(appScope.packageName, appScope.clazzName)
            .addType(createSubcomponent(clazz, componentName))
            .build()
            .toString()
    }

    private fun createSubcomponent(
        clazz: ClassReference.Psi,
        componentName: String
    ): TypeSpec {
        val clazzParent = clazz.clazz.getSuperTypeList()?.text
        val resolveComponent = when (clazzParent) {
            appCompatActivity.superClassName -> appCompatActivity
            // ToDo: add fragment
            // ToDo: add bottomSheetDialog
            else -> throw Exception("cannot resolve parent $clazzParent")
        }

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
                            clazz.shortName,
                            ClassName(
                                clazz.packageFqName.asString(),
                                clazz.shortName
                            ),
                        ).build()
                    )
                    .build()
            )
            .addSubcomponentFactory(resolveComponent, clazz, componentName)
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

    data class ClazzReference(
        val clazzName: String,
        val packageName: String
    ) {

        val poetClassName
            get() = ClassName(packageName, clazzName)

        val superClassName
            get() = "$clazzName()"
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