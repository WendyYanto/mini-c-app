package com.dev.wenn.main.screen

import androidx.appcompat.app.AppCompatActivity
import com.dev.core.CoreTextProvider
import com.dev.core.injector.FeatureInjector
import com.dev.core.mvvm.viewModel
import com.dev.core.scope.ActivityScope
import com.dev.core.scope.AppScope
import com.dev.core.scope.FeatureScope
import com.dev.feature.cart.screen.CartActivity
import com.dev.feature.cart.screen.CartViewModel
import com.dev.feature.cart.screen.DataArgsProvider
import com.squareup.anvil.annotations.ContributesTo
import com.squareup.anvil.annotations.MergeSubcomponent
import dagger.Binds
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import javax.inject.Inject

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

//    @Subcomponent.Factory
//    interface Factory {
//        fun create(
//            @BindsInstance activity: AppCompatActivity,
//            // supplied from dependency factory
//            @BindsInstance dependency
//        ): CartComponent
//    }
}

class CartComponentInjector @Inject constructor(
    private val componentFactory: CartComponent.Factory
) : FeatureInjector<CartActivity, Unit> {

    override fun inject(
        injectTarget: CartActivity,
        dependencyFactory: () -> Unit
    ) {
//        componentFactory
//            .create(injectTarget, dependencyFactory())
//            .inject(injectTarget)

        componentFactory
            .create(injectTarget)
            .inject(injectTarget)
    }
}

@Module
@ContributesTo(AppScope::class)
interface CartFeatureInjectorComponent {

    @IntoMap
    @ClassKey(CartActivity::class)
    @Binds
    fun bindCartComponentInjector(injector: CartComponentInjector): FeatureInjector<*, *>
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
    }
}