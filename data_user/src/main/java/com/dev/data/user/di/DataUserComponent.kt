package com.dev.data.user.di

import com.dev.core.scope.AppScope
import com.dev.data.user.DataJavaTextProvider
import com.squareup.anvil.annotations.ContributesTo

@ContributesTo(AppScope::class)
interface DataUserComponent {

    fun getDataJavaTextProvider(): DataJavaTextProvider
}