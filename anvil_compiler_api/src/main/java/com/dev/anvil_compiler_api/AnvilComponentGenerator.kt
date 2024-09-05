package com.dev.anvil_compiler_api

import com.google.auto.service.AutoService
import com.squareup.anvil.annotations.ExperimentalAnvilApi
import com.squareup.anvil.compiler.api.AnvilContext
import com.squareup.anvil.compiler.api.CodeGenerator
import com.squareup.anvil.compiler.api.GeneratedFile
import com.squareup.anvil.compiler.api.createGeneratedFile
import com.squareup.anvil.compiler.internal.decapitalize
import com.squareup.anvil.compiler.internal.reference.AnnotationArgumentReference
import com.squareup.anvil.compiler.internal.reference.ClassReference
import com.squareup.anvil.compiler.internal.reference.argumentAt
import com.squareup.anvil.compiler.internal.reference.classAndInnerClassReferences
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.joinToCode
import dagger.Component
import org.jetbrains.kotlin.descriptors.ModuleDescriptor
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.psi.KtClassLiteralExpression
import org.jetbrains.kotlin.psi.KtCollectionLiteralExpression
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtNameReferenceExpression
import java.io.File

@OptIn(ExperimentalAnvilApi::class)
@AutoService(CodeGenerator::class)
class AnvilComponentGenerator : CodeGenerator {

    override fun generateCode(
        codeGenDir: File,
        module: ModuleDescriptor,
        projectFiles: Collection<KtFile>
    ): Collection<GeneratedFile> {
        return projectFiles
            .classAndInnerClassReferences(module)
            .filter { it.isAnnotatedWith(FqName("com.dev.annotation.AnvilComponent")) }
            .map { clazz ->
                createGeneratedFile(
                    codeGenDir = codeGenDir,
                    packageName = clazz.packageFqName.asString(),
                    fileName = "${clazz.shortName}Component",
                    content = generateComponent(clazz)
//                    """
//                        package com.dev.feature.cart.screen
//
//                        import androidx.appcompat.app.AppCompatActivity
//                        import com.dev.core.CoreComponentInjector
//                        import com.dev.core.di.CoreComponentApi
//                        import com.dev.core.scope.FeatureScope
//                        import com.dev.domain.cart.di.DomainCartComponent
//                        import com.dev.domain.cart.di.DomainCartComponentProvider
//                        import dagger.Component
//
//                        @FeatureScope
//                        @Component(
//                            dependencies = [
//                                CoreComponentApi::class,
//                                DomainCartComponent::class,
//                            ]
//                        )
//                        interface CartComponent {
//
//                            fun inject(activity: CartActivity)
//
//                            @Component.Factory
//                            interface Factory {
//
//                                fun build(
//                                    coreComponent: CoreComponentApi,
//                                    domainCartComponent: DomainCartComponent
//                                ): CartComponent
//                            }
//
//                            companion object Initializer {
//
//                                fun init(activity: AppCompatActivity) = DaggerCartComponent.factory()
//                                    .build(
//                                        coreComponent = CoreComponentInjector.getCoreComponent(activity),
//                                        domainCartComponent = (activity.applicationContext as DomainCartComponentProvider).getDomainCartComponent(),
//                                    )
//                            }
//                        }
//                    """.trimIndent()
                )
            }.toList()
    }

    override fun isApplicable(context: AnvilContext): Boolean = true

    private fun generateComponent(clazz: ClassReference.Psi): String {
        val annotation = clazz.annotations[0]

        val componentName = "${clazz.shortName}Component"
        val fileBuilder = FileSpec.builder(clazz.packageFqName.asString(), componentName)

        val componentAnnotation = AnnotationSpec.builder(Component::class)
        val componentClazzDependencies =
            annotation.argumentAt("dependencies", -1)?.value<List<ClassReference>>()
                .orEmpty().map {
                    ClazzReference(
                        clazzName = it.shortName,
                        packageName = it.packageFqName.asString()
                    )
                }

        componentAnnotation.addMember(
            "dependencies = [${
                componentClazzDependencies.joinToString(separator = " ,") { "${it.clazzName}::class" }
            }]"
        )

        componentClazzDependencies.forEach {
            fileBuilder.addImport(it.packageName, it.clazzName)
        }
        componentClazzDependencies.forEach {
            fileBuilder.addImport(it.packageName, "${it.clazzName}Provider")
        }

        return fileBuilder
            .addImport("dagger", "Component")
            .addImport("com.dev.core.scope", "FeatureScope")
            .addType(
                TypeSpec.interfaceBuilder(componentName)
                    .addAnnotation(
                        componentAnnotation.build()
                    )
                    .addAnnotation(
                        AnnotationSpec.builder(
                            ClassName(
                                "com.dev.core.scope",
                                "FeatureScope"
                            )
                        )
                            .build()
                    )
                    .addFunction(
                        FunSpec.builder("inject")
                            .addModifiers(KModifier.ABSTRACT)
                            .addParameter(
                                ParameterSpec.builder(
                                    "activity",
                                    ClassName(clazz.packageFqName.asString(), clazz.shortName),
                                ).build(),
                            )
                            .build()
                    )
                    .addType(
                        TypeSpec.interfaceBuilder("Factory")
                            .addAnnotation(
                                AnnotationSpec.builder(Component.Factory::class)
                                    .build()
                            )
                            .addFunction(
                                FunSpec.builder("build")
                                    .addModifiers(KModifier.ABSTRACT)
                                    .addParameters(componentClazzDependencies.map {
                                        ParameterSpec.builder(
                                            it.clazzName.decapitalize(),
                                            ClassName(it.packageName, it.clazzName)
                                        )
                                            .build()
                                    })
                                    .build()
                            )
                            .build()
                    )
                    .build()
            )
            .build()
            .toString()
    }
}


data class ClazzReference(
    val clazzName: String,
    val packageName: String
)