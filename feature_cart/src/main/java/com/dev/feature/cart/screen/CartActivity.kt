package com.dev.feature.cart.screen

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.dev.annotation.InjectWith
import com.dev.core.DynamicTextProvider
import com.dev.core.injector.injectComponentWithDependency
import com.dev.domain.cart.DomainCartTextProvider
import com.dev.feature.cart.R
import com.dev.feature.cart.bottomsheet.CartBottomSheet
import javax.inject.Inject

@InjectWith(
    viewModels = [CartViewModel::class],
    dependency = CartDependency::class
)
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

    @Inject
    lateinit var cartOtherCallback: CartOtherCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponentWithDependency { CartDependency("hi from cart dependency") }
        setContentView(R.layout.activity_cart)

        val domainCartTextView = findViewById<TextView>(R.id.tv_domain_cart)
        domainCartTextView.text =
            "${domainCartTextProvider.getDomainCartText()} ${dataArgsProvider.loadArgs().hi} , ${dynamicTextProvider.loadText()}"

        toastLoader.show(cartCallback.loadText())
        toastLoader.show(cartOtherCallback.loadOtherText())

        findViewById<TextView>(R.id.tv_domain_cart).setOnClickListener {
            val bottomSheet = CartBottomSheet()
            bottomSheet.show(supportFragmentManager, "CartBottomSheet")
        }
    }
}