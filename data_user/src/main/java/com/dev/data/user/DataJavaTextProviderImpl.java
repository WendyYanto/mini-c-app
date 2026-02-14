package com.dev.data.user;

import com.dev.core.CoreTextProvider;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

public class DataJavaTextProviderImpl implements DataJavaTextProvider {

    @NotNull
    private final CoreTextProvider coreTextProvider;

    @Inject
    public DataJavaTextProviderImpl(
            @NotNull final CoreTextProvider coreTextProvider
    ) {
        this.coreTextProvider = coreTextProvider;
    }

    @Override
    public String getText() {
        return "data user text provider java !! with core: " + coreTextProvider.getText();
    }
}
