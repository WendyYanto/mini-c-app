package com.dev.core.mvvm

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


inline fun <reified T : ViewModel> FragmentActivity.viewModel(
    key: String? = null,
    noinline creator: () -> T
): T {
    val viewModelProvider = ViewModelProvider(
        viewModelStore,
        BaseViewModelFactory(creator)
    )
    return if (key != null) viewModelProvider.get(
        key,
        T::class.java
    ) else viewModelProvider.get(T::class.java)
}

inline fun <reified T : ViewModel> Fragment.viewModel(
    key: String? = null,
    noinline creator: () -> T
): T {
    val viewModelProvider = ViewModelProvider(
        viewModelStore,
        BaseViewModelFactory(creator)
    )
    return if (key != null) viewModelProvider.get(
        key,
        T::class.java
    ) else viewModelProvider.get(T::class.java)
}

@Suppress("UNCHECKED_CAST")
class BaseViewModelFactory<VM : ViewModel>(
    private val creator: () -> VM
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return creator() as T
    }
}
