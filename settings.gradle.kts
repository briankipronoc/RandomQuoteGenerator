// settings.gradle.kts (Located in your project's root directory)

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    plugins {
        id("com.android.application") version "8.2.2"
        id("org.jetbrains.kotlin.android") version "1.9.22" // Keep this Kotlin version consistent!
        id("org.jetbrains.kotlin.plugin.serialization") version "1.9.22"
        id("com.google.devtools.ksp") version "1.9.22-1.0.17"
        id("com.google.dagger.hilt.android") version "2.49"
        // --- ADD OR CONFIRM THIS LINE ---
        // Explicitly define the kapt plugin with its version here
        id("org.jetbrains.kotlin.kapt") version "1.9.22"
        // --- END ADDITION/CONFIRMATION ---
    }
}

rootProject.name = "RandomQuoteGenerator"
include(":app")

// ... (rest of your settings.gradle.kts) ...