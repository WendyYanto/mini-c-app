package com.dev.wenn.main.screen

import androidx.appcompat.app.AppCompatActivity
import com.dev.core.CoreComponentInjector
import com.dev.core.di.CoreComponent
import com.dev.core.scope.FeatureScope
import com.dev.data.user.di.DataUserComponent
import com.dev.data.user.di.DataUserComponentProvider
import dagger.Component

@FeatureScope
@Component(
    dependencies = [
        CoreComponent::class,
        DataUserComponent::class
    ]
)
interface MainComponent {

    fun inject(activity: MainActivity)

    @Component.Factory
    interface Factory {

        fun build(
            coreComponent: CoreComponent,
            dataUserComponent: DataUserComponent
        ): MainComponent
    }

    companion object Initializer {

        fun init(activity: AppCompatActivity) = DaggerMainComponent.factory()
            .build(
                coreComponent = CoreComponentInjector.getCoreComponent(activity),
                dataUserComponent = (activity.applicationContext as DataUserComponentProvider).getDataUserComponent()
            )
    }
}