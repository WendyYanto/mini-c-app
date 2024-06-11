package com.dev.wenn.main.di

import com.dev.core.di.CoreComponentProvider
import com.dev.data.product.di.DataProductComponentProvider
import com.dev.data.user.di.DataUserComponentProvider

interface ComponentProvider :
    CoreComponentProvider,
    DataUserComponentProvider,
    DataProductComponentProvider