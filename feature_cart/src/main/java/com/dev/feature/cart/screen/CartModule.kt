package com.dev.feature.cart.screen

import com.dev.annotation.MetroProvides
import com.dev.core.scope.FeatureScope
import com.dev.domain.cart.CartModuleTest
import com.dev.domain.cart.CartModuleTestImpl
import dagger.Module
import dagger.Provides

@Module
interface CartModule {

    companion object {

        @Provides
        @FeatureScope
        @MetroProvides
        fun provideCartModuleTest(): CartModuleTest {
            return CartModuleTestImpl()
        }
    }
}