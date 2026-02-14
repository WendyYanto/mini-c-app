package com.dev.ksp_processor

import com.google.auto.service.AutoService
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

@AutoService(SymbolProcessorProvider::class)
class BindsDependencyModuleProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        val useMetro = environment.options["useMetro"]?.toBoolean() ?: false
        return BindsDependencyModuleProcessor(
            codeGenerator = environment.codeGenerator,
            logger = environment.logger,
            useMetro = useMetro,
        )
    }
}