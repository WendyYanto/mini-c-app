package com.dev.anvil_compiler_api

import com.google.auto.service.AutoService
import com.squareup.anvil.annotations.ExperimentalAnvilApi
import com.squareup.anvil.compiler.api.AnvilContext
import com.squareup.anvil.compiler.api.CodeGenerator
import com.squareup.anvil.compiler.api.GeneratedFile
import com.squareup.anvil.compiler.api.createGeneratedFile
import com.squareup.anvil.compiler.internal.reference.classAndInnerClassReferences
import org.jetbrains.kotlin.descriptors.ModuleDescriptor
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.psi.KtFile
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
                    content = """
                        package com.dev.feature.cart.screen
                        
                        import androidx.appcompat.app.AppCompatActivity
                        import com.dev.core.CoreComponentInjector
                        import com.dev.core.di.CoreComponentApi
                        import com.dev.core.scope.FeatureScope
                        import com.dev.domain.cart.di.DomainCartComponent
                        import com.dev.domain.cart.di.DomainCartComponentProvider
                        import dagger.Component
                        
                        @FeatureScope
                        @Component(
                            dependencies = [
                                CoreComponentApi::class,
                                DomainCartComponent::class,
                            ]
                        )
                        interface CartComponent {
                        
                            fun inject(activity: CartActivity)
                        
                            @Component.Factory
                            interface Factory {
                        
                                fun build(
                                    coreComponent: CoreComponentApi,
                                    domainCartComponent: DomainCartComponent
                                ): CartComponent
                            }
                        
                            companion object Initializer {
                        
                                fun init(activity: AppCompatActivity) = DaggerCartComponent.factory()
                                    .build(
                                        coreComponent = CoreComponentInjector.getCoreComponent(activity),
                                        domainCartComponent = (activity.applicationContext as DomainCartComponentProvider).getDomainCartComponent(),
                                    )
                            }
                        }
                    """.trimIndent()
                )
            }.toList()
    }

    override fun isApplicable(context: AnvilContext): Boolean = true


}