package com.dev.feature.cart.screen

import com.dev.core.scope.FeatureScope
import dagger.Module
import dagger.Provides

@Module
interface CartModule {

    companion object {

        @Provides
        @FeatureScope
        fun provideCartModuleTest(): CartModuleTest {
            return CartModuleTest()
        }
    }
}