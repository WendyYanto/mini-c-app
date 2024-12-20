package com.dev.feature.cart.bottomsheet

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.dev.annotation.InjectWith
import com.dev.core.injector.injectComponentWithDependency
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import javax.inject.Inject

@InjectWith(
    viewModels = [CartBottomSheetViewModel::class],
    dependency = CartBottomSheetArg::class
)
class CartBottomSheet : BottomSheetDialogFragment() {

    @Inject
    lateinit var viewModel: CartBottomSheetViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponentWithDependency {
            CartBottomSheetArg("CartBottomSheetArg")
        }
        context?.let {
            Toast.makeText(context, viewModel.loadText(), Toast.LENGTH_SHORT).show()
        }
    }
}