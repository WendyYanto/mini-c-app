package com.dev.feature.cart.screen

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.dev.annotation.AnvilComponent
import com.dev.core.di.CoreComponentApi
import com.dev.domain.cart.DomainCartTextProvider
import com.dev.domain.cart.di.DomainCartComponent
import com.dev.domain.cart.di.DomainCartModule
import com.dev.feature.cart.R
import javax.inject.Inject

@AnvilComponent(
    dependencies = [CoreComponentApi::class, DomainCartComponent::class],
    modules = [DomainCartModule::class]
)
class CartActivity : AppCompatActivity() {

    @Inject
    lateinit var domainCartTextProvider: DomainCartTextProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CartActivityComponent.init(this).inject(this)
        setContentView(R.layout.activity_cart)
        val domainCartTextView = findViewById<TextView>(R.id.tv_domain_cart)
        domainCartTextView.text = domainCartTextProvider.getDomainCartText()
    }
}