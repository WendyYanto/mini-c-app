package com.dev.wenn.main.di

import com.dev.core.di.CoreComponent
import com.dev.core.scope.LegacyApplicationScope
import com.dev.data.misc.di.DataMiscComponent
import com.dev.wenn.main.App
import dagger.Component

@LegacyApplicationScope
@Component(
    dependencies = [
        CoreComponent::class,
        DataMiscComponent::class
    ]
)
interface AppComponent :
    CoreComponent,
    DataMiscComponent {

    fun inject(app: App)

    @Component.Factory
    interface Factory {

        fun build(
            coreComponent: CoreComponent,
            dataMiscComponent: DataMiscComponent,
        ): AppComponent
    }

    companion object Initializer {

        fun init(app: App): AppComponent {
            ComponentsRegistry.init(app)
            return DaggerAppComponent.factory()
                .build(
                    coreComponent = app.getCoreComponent(),
                    dataMiscComponent = app.getDataMiscComponent(),
                )
        }
    }
}