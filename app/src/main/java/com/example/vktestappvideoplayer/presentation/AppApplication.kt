package com.example.vktestappvideoplayer.presentation

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.vktestappvideoplayer.di.ApplicationComponent
import com.example.vktestappvideoplayer.di.DaggerApplicationComponent

/**
 * Application class for DI initialization.
 */
class AppApplication : Application() {
    val component: ApplicationComponent by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}

@Composable
fun getApplicationComponent(): ApplicationComponent =
    (LocalContext.current.applicationContext as AppApplication).component