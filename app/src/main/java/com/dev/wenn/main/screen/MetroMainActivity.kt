package com.dev.wenn.main.screen

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dev.core.ComponentHolder
import com.dev.data.misc.DataMiscTextProvider
import com.dev.domain.cart.DomainCartTextProvider
import com.dev.wenn.R
import com.dev.wenn.main.di.MetroAppComponent
import dev.zacsweers.metro.Inject

class MetroMainActivity : AppCompatActivity() {

    @Inject
    lateinit var domainCartTextProvider: DomainCartTextProvider

    @Inject
    lateinit var dataMiscTextProvider: DataMiscTextProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_metro_main)

        ComponentHolder.component<MetroAppComponent>()
            .injectMetroMainActivity(this)

        val metroMain = findViewById<TextView>(R.id.tv_metro_main)
        metroMain.text =
            "${domainCartTextProvider.getDomainCartText()} \n\n ${dataMiscTextProvider.getMiscText()}"

        Toast.makeText(this, domainCartTextProvider.getDomainCartText(), Toast.LENGTH_SHORT).show()
    }
}