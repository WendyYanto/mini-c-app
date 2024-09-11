package com.dev.wenn.main.screen

import androidx.appcompat.app.AppCompatActivity
import com.dev.core.CoreTextProvider
import com.dev.core.mvvm.viewModel
import com.dev.core.scope.ActivityScope
import com.dev.core.scope.AppScope
import com.dev.core.scope.FeatureScope
import com.dev.feature.cart.screen.CartActivity
import com.dev.feature.cart.screen.CartCallback
import com.dev.feature.cart.screen.CartViewModel
import com.dev.feature.cart.screen.DataArgsProvider
import com.squareup.anvil.annotations.ContributesTo
import com.squareup.anvil.annotations.MergeSubcomponent
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@FeatureScope
@MergeSubcomponent(
    scope = ActivityScope::class
)
interface CartComponent {

    fun inject(activity: CartActivity)

    @ContributesTo(AppScope::class)
    interface ParentComponent {

        fun createCartComponent(): Factory
    }

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance activity: AppCompatActivity
        ): CartComponent
    }
}

@Module
@ContributesTo(ActivityScope::class)
interface ComponentModule {

    companion object {
        @Provides
        @FeatureScope
        fun provideViewModel(
            activity: AppCompatActivity,
            coreTextProvider: CoreTextProvider,
            dataArgsProvider: DataArgsProvider
        ): CartViewModel = activity.viewModel {
            CartViewModel(
                coreTextProvider = coreTextProvider,
                dataArgsProvider = dataArgsProvider
            )
        }

        // this feels useless
        @Provides
        @FeatureScope
        fun provideCallbacks(viewModel: CartViewModel): CartCallback = viewModel.callback
    }
}