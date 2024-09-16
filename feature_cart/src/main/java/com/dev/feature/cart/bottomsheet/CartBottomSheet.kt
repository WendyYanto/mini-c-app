package com.dev.feature.cart.bottomsheet

import android.os.Bundle
import android.widget.Toast
import com.dev.annotation.InjectWith
import com.dev.core.injector.injectComponent
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import javax.inject.Inject

@InjectWith(
    viewModels = [CartBottomSheetViewModel::class]
)
class CartBottomSheet : BottomSheetDialogFragment() {

    @Inject
    lateinit var viewModel: CartBottomSheetViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponent()
        context?.let {
            Toast.makeText(context, viewModel.loadText(), Toast.LENGTH_SHORT).show()
        }
    }
}