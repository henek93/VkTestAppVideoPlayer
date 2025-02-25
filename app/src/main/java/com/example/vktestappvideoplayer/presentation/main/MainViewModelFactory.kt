package com.example.vktestappvideoplayer.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

/**
 * Factory for creating ViewModels with Dagger.
 */
class ViewModelFactory @Inject constructor(
    private val providers: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        providers[modelClass]?.get() as T ?: throw IllegalArgumentException("Unknown ViewModel: $modelClass")
}