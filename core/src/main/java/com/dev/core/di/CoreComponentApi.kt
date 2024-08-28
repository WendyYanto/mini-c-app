package com.dev.core.di

import com.dev.core.CoreTextProvider

interface CoreComponentApi {

    fun provideCoreTextProvider(): CoreTextProvider
}