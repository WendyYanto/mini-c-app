package com.dev.wenn.main.screen

import androidx.appcompat.app.AppCompatActivity
import com.dev.core.CoreComponentInjector
import com.dev.core.di.CoreComponentApi
import com.dev.core.scope.AppFeatureScope
import com.dev.core.scope.FeatureScope
import com.dev.data.misc.di.DataMiscComponent
import com.dev.data.misc.di.DataMiscComponentProvider
import com.dev.data.order.di.DataOrderComponent
import com.dev.data.order.di.DataOrderComponentProvider
import com.dev.data.product.di.DataProductComponent
import com.dev.data.product.di.DataProductComponentProvider
import com.dev.data.user.di.DataUserComponent
import com.dev.data.user.di.DataUserComponentProvider
import com.dev.domain.cart.di.DomainCartComponent
import com.dev.domain.cart.di.DomainCartComponentProvider
import dagger.Component

@FeatureScope
@Component(
    dependencies = [
        CoreComponentApi::class,
        DataUserComponent::class,
        DataProductComponent::class,
        DataOrderComponent::class,
        DataMiscComponent::class,
        DomainCartComponent::class
    ]
)
interface MainComponent {

    fun inject(activity: MainActivity)

    @Component.Factory
    interface Factory {

        fun build(
            coreComponent: CoreComponentApi,
            dataUserComponent: DataUserComponent,
            dataProductComponent: DataProductComponent,
            dataOrderComponent: DataOrderComponent,
            dataMiscComponent: DataMiscComponent,
            domainCartComponent: DomainCartComponent
        ): MainComponent
    }

    companion object Initializer {

        fun init(activity: AppCompatActivity) = DaggerMainComponent.factory()
            .build(
                coreComponent = CoreComponentInjector.getCoreComponent(activity),
                dataUserComponent = (activity.applicationContext as DataUserComponentProvider).getDataUserComponent(),
                dataProductComponent = (activity.applicationContext as DataProductComponentProvider).getDataProductComponent(),
                dataOrderComponent = (activity.applicationContext as DataOrderComponentProvider).getDataOrderComponent(),
                dataMiscComponent = (activity.applicationContext as DataMiscComponentProvider).getDataMiscComponent(),
                domainCartComponent = (activity.applicationContext as DomainCartComponentProvider).getDomainCartComponent(),
            )
    }
}