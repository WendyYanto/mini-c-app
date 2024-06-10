package com.dev.wenn.main.screen

import androidx.appcompat.app.AppCompatActivity
import com.dev.core.CoreComponentInjector
import com.dev.core.di.CoreComponent
import com.dev.core.scope.FeatureScope
import dagger.Component

@FeatureScope
@Component(
    dependencies = [CoreComponent::class]
)
interface MainComponent {

    fun inject(activity: MainActivity)

    @Component.Factory
    interface Factory {

        fun build(
            coreComponent: CoreComponent
        ): MainComponent
    }

    companion object Initializer {

        fun init(activity: AppCompatActivity) = DaggerMainComponent.factory()
            .build(
                coreComponent = CoreComponentInjector.getCoreComponent(activity)
            )
    }
}