package com.dev.core

import com.dev.core.scope.AppScope
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import javax.inject.Inject

@Module
@ContributesTo(AppScope::class)
interface TestProvidesModule {

    companion object {
        @Provides
        fun providesTestProvidersTest(): TestProvidersTest {
            return TestProvidersTestImpl()
        }
    }
}

interface TestProvidersTest {

    fun getInfo(): String
}

class TestProvidersTestImpl @Inject constructor() : TestProvidersTest {
    override fun getInfo(): String {
        return "getInfo"
    }
}