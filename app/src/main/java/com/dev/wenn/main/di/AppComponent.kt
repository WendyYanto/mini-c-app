package com.dev.wenn.main.di

import com.dev.core.di.CoreComponent
import com.dev.core.scope.ApplicationScope
import com.dev.data.user.di.DataUserComponent
import com.dev.wenn.main.App
import dagger.Component

@ApplicationScope
@Component(
    dependencies = [
        CoreComponent::class,
        DataUserComponent::class
    ]
)
interface AppComponent : CoreComponent, DataUserComponent {

    companion object Initializer {

        fun init(app: App): AppComponent {
            return DaggerAppComponent.builder()
                .coreComponent(app.getCoreComponent())
                .dataUserComponent(app.getDataUserComponent())
                .build()
        }
    }
}