package com.dev.wenn.main.di

import com.dev.core.di.CoreComponentApiProvider
import com.dev.data.misc.di.DataMiscComponentProvider
import com.dev.data.order.di.DataOrderComponentProvider
import com.dev.data.product.di.DataProductComponentProvider
import com.dev.data.user.di.DataUserComponentProvider
import com.dev.domain.cart.di.DomainCartComponentProvider

interface ComponentProvider :
    CoreComponentApiProvider,
    DataUserComponentProvider,
    DataProductComponentProvider,
    DataOrderComponentProvider,
    DataMiscComponentProvider,
    DomainCartComponentProvider