package com.dev.ksp_processor

import com.google.auto.service.AutoService
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

@AutoService(SymbolProcessorProvider::class)
class InjectWithProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return InjectWithProcessor(
            codeGenerator = environment.codeGenerator,
            logger = environment.logger,
            // set from BuildFeaturesExtensions
            useMetro = environment.options["useMetro"]?.toBoolean() ?: false
        )
    }
}
