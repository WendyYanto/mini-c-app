package com.dev.feature.cart.screen

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.dev.core.ComponentHolder
import com.dev.core.DynamicTextProvider
import com.dev.domain.cart.DomainCartTextProvider
import com.dev.feature.cart.R
import com.dev.wenn.main.screen.CartComponent
import javax.inject.Inject

class CartActivity : AppCompatActivity() {

    @Inject
    lateinit var domainCartTextProvider: DomainCartTextProvider

    @Inject
    lateinit var dataArgsProvider: DataArgsProvider

    @Inject
    lateinit var dynamicTextProvider: DynamicTextProvider

    @Inject
    lateinit var toastLoader: ToastLoader

    @Inject
    lateinit var cartCallback: CartCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.v("FEATURE_CART", "activity created and loading components")
        val component = ComponentHolder.component<CartComponent.ParentComponent>()
            .createCartComponent()
            .create(this)

        component.inject(this)

        setContentView(R.layout.activity_cart)

        val domainCartTextView = findViewById<TextView>(R.id.tv_domain_cart)
        domainCartTextView.text =
            "${domainCartTextProvider.getDomainCartText()} ${dataArgsProvider.loadArgs().hi} , ${dynamicTextProvider.loadText()}"

        toastLoader.show(cartCallback.loadText())
    }
}