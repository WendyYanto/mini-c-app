package com.dev.wenn.main.screen

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.dev.core.ComponentHolder
import com.dev.core.DynamicTextProvider
import com.dev.domain.cart.DomainCartTextProvider
import com.dev.feature.cart.R
import com.dev.feature.cart.screen.DataArgsProvider
import javax.inject.Inject

class CartActivity : AppCompatActivity() {

    @Inject
    lateinit var domainCartTextProvider: DomainCartTextProvider

    @Inject
    lateinit var dataArgsProvider: DataArgsProvider

    @Inject
    lateinit var dynamicTextProvider: DynamicTextProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ComponentHolder.component<CartComponent.ParentComponent>()
            .createCartComponent()
            .inject(this)

        setContentView(R.layout.activity_cart)

        val domainCartTextView = findViewById<TextView>(R.id.tv_domain_cart)
        domainCartTextView.text =
            "${domainCartTextProvider.getDomainCartText()} ${dataArgsProvider.loadArgs().hi} , ${dynamicTextProvider.loadText()}"
    }
}