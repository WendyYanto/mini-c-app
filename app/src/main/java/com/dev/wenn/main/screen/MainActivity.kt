package com.dev.wenn.main.screen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dev.annotation.InjectWith
import com.dev.core.CoreTextProvider
import com.dev.core.injector.injectComponent
import com.dev.data.misc.DataMiscTextProvider
import com.dev.data.order.DataOrderTextProvider
import com.dev.data.product.DataProductTextProvider
import com.dev.data.user.DataJavaTextProvider
import com.dev.data.user.DataUserTextProvider
import com.dev.domain.cart.DomainCartTextProvider
import com.dev.feature.cart.screen.CartActivity
import com.dev.wenn.R
import com.dev.core.BaseActivity
import com.dev.core.TextLoaderProvider
import com.dev.wenn.main.loaders.CoreTextLoader
import com.dev.wenn.main.loaders.DataUserTextLoader
import javax.inject.Inject

@InjectWith
class MainActivity : BaseActivity() {

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

//    @Inject
//    lateinit var coreTextProvidersTest: TestProvidersTest

    @Inject
    lateinit var dataJavaTextProvider: DataJavaTextProvider

    @Inject
    lateinit var textLoaderMap: Map<Class<*>, @JvmSuppressWildcards TextLoaderProvider<*>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injectComponent()

        setContentView(R.layout.activity_main)

        Log.v(
            "WEE",
            textLoaderMap[CoreTextLoader::class.java]?.create()?.loadMergedText().orEmpty(),
        )

        Log.v(
            "WEE",
            textLoaderMap[DataUserTextLoader::class.java]?.create()?.loadMergedText().orEmpty(),
        )

        val hiText = findViewById<TextView>(R.id.tv_hi)
        hiText.setOnClickListener {
            val intent = Intent(this, DaggerOnlyActivity::class.java)
            startActivity(intent)
        }

        val textView = findViewById<TextView>(R.id.tv_core_text)
        textView.text = loadCore()

        val userTextView = findViewById<TextView>(R.id.tv_data_user)
        userTextView.text = dataUserTextProvider.getUserText()

        val productTextView = findViewById<TextView>(R.id.tv_data_product)
        productTextView.text = dataProductTextProvider.getProductText()

        val orderTextView = findViewById<TextView>(R.id.tv_data_order)
        orderTextView.text = dataOrderTextProvider.getOrderText()

        val domainCartTextView = findViewById<TextView>(R.id.tv_domain_cart)
        domainCartTextView.text = domainCartTextProvider.getDomainCartText()

        domainCartTextView.setOnClickListener {
            Toast.makeText(this, dataJavaTextProvider.text, Toast.LENGTH_SHORT).show()

            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }
    }
}