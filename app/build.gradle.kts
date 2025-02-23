plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.vktestappvideoplayer"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.vktestappvideoplayer"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"


    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.exoplayer)
    implementation(libs.androidx.ui.text.google.fonts)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Material 3
    implementation(libs.androidx.material3)

    // Room
    implementation(libs.room.core)
    ksp(libs.room.compiler)

    // Dagger
    implementation(libs.dagger.core)
    ksp(libs.dagger.compiler)

    // Retrofit
    implementation(libs.retorfit.core)
    implementation(libs.retorfit.gsonConverter)
    implementation(libs.okhttp.logging.interceptor)

    // ExoPlayer
    implementation(libs.androidx.media3.ui)

    // SwipeRefreshLayout
    implementation(libs.androidx.swiperefreshlayout)

    // Navigation
    implementation(libs.navigation.compose)

    // Material Icons
    implementation(libs.icons)

    // Coil
    implementation(libs.coil.compose)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))

    //Shimmer
    implementation(libs.compose.shimmer)
}