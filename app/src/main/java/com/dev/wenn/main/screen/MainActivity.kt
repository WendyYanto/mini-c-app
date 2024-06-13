package com.dev.wenn.main.screen

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.dev.core.CoreTextProvider
import com.dev.data.misc.DataMiscTextProvider
import com.dev.data.misc.di.DataMiscComponentProvider
import com.dev.data.order.DataOrderTextProvider
import com.dev.data.product.DataProductTextProvider
import com.dev.data.user.DataUserTextProvider
import com.dev.domain.cart.DomainCartTextProvider
import com.dev.wenn.R
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var coreTextProvider: CoreTextProvider

    @Inject
    lateinit var dataUserTextProvider: DataUserTextProvider

    @Inject
    lateinit var dataProductTextProvider: DataProductTextProvider

    @Inject
    lateinit var dataOrderTextProvider: DataOrderTextProvider

    @Inject
    lateinit var dataMiscTextProvider: DataMiscTextProvider

    @Inject
    lateinit var domainCartTextProvider: DomainCartTextProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainComponent.init(this).inject(this)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.tv_core_text)
        textView.text = coreTextProvider.getText()

        val userTextView = findViewById<TextView>(R.id.tv_data_user)
        userTextView.text = dataUserTextProvider.getUserText()

        val productTextView = findViewById<TextView>(R.id.tv_data_product)
        productTextView.text = dataProductTextProvider.getProductText()

        val orderTextView = findViewById<TextView>(R.id.tv_data_order)
        orderTextView.text = dataOrderTextProvider.getOrderText()

        val domainCartTextView = findViewById<TextView>(R.id.tv_domain_cart)
        domainCartTextView.text = domainCartTextProvider.getDomainCartText()

        domainCartTextView.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }
    }
}