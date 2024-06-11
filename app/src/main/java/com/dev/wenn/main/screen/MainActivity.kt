package com.dev.wenn.main.screen

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.dev.core.CoreTextProvider
import com.dev.data.user.DataUserTextProvider
import com.dev.wenn.R
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var coreTextProvider: CoreTextProvider

    @Inject
    lateinit var dataUserTextProvider: DataUserTextProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainComponent.init(this).inject(this)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.tv_core_text)
        textView.text = coreTextProvider.getText()

        val userTextView = findViewById<TextView>(R.id.tv_data_user)
        userTextView.text = dataUserTextProvider.getUserText()
    }
}