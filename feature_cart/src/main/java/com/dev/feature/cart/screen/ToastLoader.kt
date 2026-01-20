package com.dev.feature.cart.screen

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dev.core.scope.ActivityScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

interface ToastLoader {

    fun show(text: String)
}

@ContributesBinding(ActivityScope::class)
class ToastLoaderImpl @Inject constructor(
    private val appCompatActivity: AppCompatActivity
) : ToastLoader {

    override fun show(text: String) {
        Toast.makeText(appCompatActivity, text, Toast.LENGTH_SHORT).show()
    }
}