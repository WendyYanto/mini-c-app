package com.dev.wenn.main.di

import com.dev.core.di.CoreComponentProvider
import com.dev.data.order.di.DataOrderComponentProvider
import com.dev.data.product.di.DataProductComponentProvider
import com.dev.domain.cart.di.DomainCartComponentProvider

interface ComponentProvider :
    CoreComponentProvider,
    DataProductComponentProvider,
    DataOrderComponentProvider,
    DomainCartComponentProvider