package com.dev.wenn.main.screen

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dev.domain.cart.DomainCartTextProvider
import com.dev.wenn.R
import jakarta.inject.Inject

class DaggerOnlyActivity : AppCompatActivity() {

    @Inject
    lateinit var domainCartTextProvider: DomainCartTextProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DaggerOnlyComponent.init(this).inject(this)

        setContentView(R.layout.activity_dagger_only)

        Toast.makeText(
            this,
            domainCartTextProvider.getDomainCartText(),
            Toast.LENGTH_SHORT
        ).show()
    }

}