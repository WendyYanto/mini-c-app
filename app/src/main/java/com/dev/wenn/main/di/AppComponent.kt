package com.dev.wenn.main.di

import com.dev.core.di.CoreComponent
import com.dev.core.injector.FeatureInjector
import com.dev.core.scope.AppScope
import com.dev.core.scope.ApplicationScope
import com.dev.data.misc.di.DataMiscComponent
//import com.dev.data.user.di.DataUserComponent
import com.dev.wenn.main.App
import com.squareup.anvil.annotations.MergeComponent
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import kotlin.reflect.KClass

@ApplicationScope
@MergeComponent(
    scope = AppScope::class,
    dependencies = [
        CoreComponent::class,
//        DataUserComponent::class,
        DataMiscComponent::class
    ]
)
@Component(
    dependencies = [
        CoreComponent::class,
//        DataUserComponent::class,
        DataMiscComponent::class
    ]
)
interface AppComponent :
    CoreComponent,
//    DataUserComponent,
    DataMiscComponent {

    fun inject(app: App)

    @Component.Factory
    interface Factory {

        fun build(
            coreComponent: CoreComponent,
            dataMiscComponent: DataMiscComponent,
//            @BindsInstance featureInjectorMap: Map<KClass<*>,  @JvmSuppressWildcards FeatureInjector<*, *>>
        ): AppComponent
    }

    companion object Initializer {

        fun init(app: App): AppComponent {
            ComponentsRegistry.init(app)
            return DaggerAppComponent.factory()
                .build(
                    coreComponent = app.getCoreComponent(),
                    dataMiscComponent = app.getDataMiscComponent(),
//                    featureInjectorMap = emptyMap()
                )
//                .dataUserComponent(app.getDataUserComponent())
        }
    }
}