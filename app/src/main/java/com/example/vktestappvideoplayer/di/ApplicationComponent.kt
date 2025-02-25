package com.example.vktestappvideoplayer.di

import android.content.Context
import com.example.vktestappvideoplayer.presentation.main.ViewModelFactory
import dagger.BindsInstance
import dagger.Component

/**
 * Defines the root Dagger component for the application.
 */
@ApplicationScope
@Component(modules = [DataModule::class, ViewModelModule::class])
interface ApplicationComponent {
    /**
     * Provides the ViewModelFactory for creating ViewModels.
     */
    fun getViewModelFactory(): ViewModelFactory

    /**
     * Factory interface for creating the ApplicationComponent.
     */
    @Component.Factory
    interface Factory {
        /**
         * Creates an instance of ApplicationComponent with the given context.
         */
        fun create(@BindsInstance context: Context): ApplicationComponent
    }
}