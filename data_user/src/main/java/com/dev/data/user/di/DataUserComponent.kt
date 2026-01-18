package com.dev.data.user.di

import android.content.Context
import com.dev.core.CoreComponentInjector
import com.dev.core.di.CoreComponent
import com.dev.core.scope.AppScope
import com.dev.core.scope.DataScope
import com.dev.data.user.DataUserTextProvider
import com.squareup.anvil.annotations.ContributesTo
import dagger.Component

//@Component(
//    dependencies = [CoreComponent::class],
////    modules = [DataUserModule::class]
//)
//@DataUserScope
//@ContributesTo(AppScope::class)
//interface DataUserComponent {

//    @Component.Factory
//    interface Factory {
//
//        fun build(
//            coreComponent: CoreComponent
//        ): DataUserComponent
//    }
//
//    companion object Initializer {
//
//        fun init(context: Context): DataUserComponent =
//            DaggerDataUserComponent.factory()
//                .build(
//                    coreComponent = CoreComponentInjector.getCoreComponent(context)
//                )
//    }

//    fun getDataUserTextProvider(): DataUserTextProvider
//}