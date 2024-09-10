package com.dev.feature.cart.screen

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dev.core.scope.ActivityScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

interface ToastLoader {

    fun show()
}

@ContributesBinding(ActivityScope::class)
class ToastLoaderImpl @Inject constructor(
    private val appCompatActivity: AppCompatActivity
) : ToastLoader {

    override fun show() {
        Toast.makeText(appCompatActivity, "Toast loader", Toast.LENGTH_SHORT).show()
    }
}