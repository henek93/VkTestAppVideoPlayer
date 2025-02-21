plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.vktestappvideoplayer"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.vktestappvideoplayer"
        minSdk = 24
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


    //Retrofit
    implementation(libs.retorfit.core)
    implementation(libs.retorfit.gsonConverter)
    implementation(libs.okhttp.logging.interceptor)

// ExoPlayer для воспроизведения видео
    implementation(libs.exoplayer)

//Room
    implementation(libs.room.core)
    ksp(libs.room.compiler)

// Dagger Hilt для управления зависимостями
    implementation(libs.dagger.core)
    ksp(libs.dagger.compiler)

// SwipeRefreshLayout
    implementation(libs.androidx.swiperefreshlayout)

    //Navigation
    implementation(libs.navigation.compose)

    //MaterialIcons
    implementation(libs.icons)

    //Glide
    implementation(libs.glide.compose)

    //coil
    implementation(libs.coil.compose)
}