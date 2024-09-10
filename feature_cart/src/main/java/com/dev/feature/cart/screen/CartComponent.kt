package com.dev.wenn.main.screen

import androidx.appcompat.app.AppCompatActivity
import com.dev.core.scope.ActivityScope
import com.dev.core.scope.AppScope
import com.dev.core.scope.FeatureScope
import com.squareup.anvil.annotations.ContributesTo
import com.squareup.anvil.annotations.MergeSubcomponent
import dagger.BindsInstance
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