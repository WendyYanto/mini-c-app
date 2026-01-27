package com.dev.feature.cart.screen

import com.dev.annotation.MetroProvides
import com.dev.core.scope.ActivityScope
import com.dev.core.scope.FeatureScope
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides

@Module
interface CartModule {

    companion object {

        @Provides
        // this is required for metro interops
        @MetroProvides
        @FeatureScope
        fun provideCartModuleTest(
        ): CartModuleTest {
            return CartModuleTest()
        }
    }
}