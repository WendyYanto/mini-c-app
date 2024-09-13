package com.dev.anvil_compiler_api;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u001e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J,\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0004H\u0016J\u0010\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0002J\u0010\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0016\u00a8\u0006\u0014"}, d2 = {"Lcom/dev/anvil_compiler_api/AnvilComponentGenerator;", "Lcom/squareup/anvil/compiler/api/CodeGenerator;", "()V", "generateCode", "", "Lcom/squareup/anvil/compiler/api/GeneratedFile;", "codeGenDir", "Ljava/io/File;", "module", "Lorg/jetbrains/kotlin/descriptors/ModuleDescriptor;", "projectFiles", "Lorg/jetbrains/kotlin/psi/KtFile;", "generateComponent", "", "clazz", "Lcom/squareup/anvil/compiler/internal/reference/ClassReference$Psi;", "isApplicable", "", "context", "Lcom/squareup/anvil/compiler/api/AnvilContext;", "anvil_compiler_api"})
@kotlin.OptIn(markerClass = {com.squareup.anvil.annotations.ExperimentalAnvilApi.class})
@com.google.auto.service.AutoService(value = {com.squareup.anvil.compiler.api.CodeGenerator.class})
public final class AnvilComponentGenerator implements com.squareup.anvil.compiler.api.CodeGenerator {
    
    public AnvilComponentGenerator() {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.util.Collection<com.squareup.anvil.compiler.api.GeneratedFile> generateCode(@org.jetbrains.annotations.NotNull()
    java.io.File codeGenDir, @org.jetbrains.annotations.NotNull()
    org.jetbrains.kotlin.descriptors.ModuleDescriptor module, @org.jetbrains.annotations.NotNull()
    java.util.Collection<? extends org.jetbrains.kotlin.psi.KtFile> projectFiles) {
        return null;
    }
    
    @java.lang.Override()
    public boolean isApplicable(@org.jetbrains.annotations.NotNull()
    com.squareup.anvil.compiler.api.AnvilContext context) {
        return false;
    }
    
    private final java.lang.String generateComponent(com.squareup.anvil.compiler.internal.reference.ClassReference.Psi clazz) {
        return null;
    }
}