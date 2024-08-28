package com.dev.feature.cart.screen

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.dev.domain.cart.DomainCartTextProvider
import com.dev.feature.cart.R
import javax.inject.Inject

class CartActivity : AppCompatActivity() {

    @Inject
    lateinit var domainCartTextProvider: DomainCartTextProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CartComponent.init(this).inject(this)
        setContentView(R.layout.activity_cart)
        val domainCartTextView = findViewById<TextView>(R.id.tv_domain_cart)
        domainCartTextView.text = domainCartTextProvider.getDomainCartText()
    }
}