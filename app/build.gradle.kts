plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.kiprono.randomquotegenerator"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.kiprono.randomquotegenerator"
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

    dependencies {
        // Jetpack Compose dependencies
        implementation("androidx.compose.ui:ui:1.5.1")
        implementation("androidx.compose.material3:material3:1.1.1")
        implementation("androidx.compose.ui:ui-tooling-preview:1.5.1")
        debugImplementation("androidx.compose.ui:ui-tooling:1.5.1")
        implementation("androidx.activity:activity-compose:1.7.2")

        // Navigation Compose
        implementation("androidx.navigation:navigation-compose:2.5.3")

        // Lifecycle & ViewModel
        implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
        implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")

        // Retrofit for network calls
        implementation("com.squareup.retrofit2:retrofit:2.9.0")
        implementation("com.squareup.retrofit2:converter-gson:2.9.0")

        // Coroutines for async work
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")
    }
}