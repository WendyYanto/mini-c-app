package com.dev.wenn.main.screen

import androidx.appcompat.app.AppCompatActivity
import com.dev.core.ComponentHolder
import com.dev.core.scope.FeatureScope
import com.dev.merged.MergedAppComponent
import dagger.BindsInstance
import dagger.Component

@Component(dependencies = [MergedAppComponent::class])
@FeatureScope
interface DaggerOnlyComponent {

    fun inject(activity: DaggerOnlyActivity)

    @Component.Factory
    interface Factory {
        fun build(
            @BindsInstance activity: AppCompatActivity,
            mergedAppComponent: MergedAppComponent
        ): DaggerOnlyComponent
    }

    companion object {
        fun init(activity: AppCompatActivity): DaggerOnlyComponent =
            DaggerDaggerOnlyComponent.factory()
                .build(
                    activity = activity,
                    mergedAppComponent = ComponentHolder.component<MergedAppComponent>()
                )
    }
}