// app/build.gradle.kts (Module :app)

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.kiprono.randomquote"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.kiprono.randomquote"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        // KEEP THIS: Explicitly setting Compose Compiler Extension version to match Kotlin 1.9.22
        kotlinCompilerExtensionVersion = "1.5.10"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.3")

    // !!! IMPORTANT CHANGE: Downgrade activity-compose to a version compatible with Compose BOM 2024.02.01
    // 1.8.2 is generally compatible with Compose 1.6.x
    implementation("androidx.activity:activity-compose:1.8.2") // CHANGED from 1.9.0

    // IMPORTANT: Keep compose-bom 2024.02.01
    implementation(platform("androidx.compose:compose-bom:2024.02.01"))

    // Core Compose UI libraries (versions managed by the BOM)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")

    // Material 3 and Icons (versions managed by the BOM)
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-core")
    implementation("androidx.compose.material:material-icons-extended")

    // Keep this explicit declaration, as the BOM might still be tricky
    // This should now be honored since the 'strictly' constraint from other libraries will be gone.
    implementation("androidx.compose.foundation:foundation:1.6.2")


    implementation("javax.inject:javax.inject:1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.3")

    // Retrofit and Room dependencies remain unchanged
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    ksp("androidx.room:room-compiler:$room_version")

    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // !!! IMPORTANT CHANGE: Downgrade navigation-compose to a stable version compatible with Compose BOM 2024.02.01
    // 2.7.7 is the latest stable 2.7.x release, which aligns better.
    implementation("androidx.navigation:navigation-compose:2.7.7") // CHANGED from 2.8.0-beta02

    // Testing dependencies remain unchanged
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    // KEEP THIS: Make sure test BOM also matches
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.02.01"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
}