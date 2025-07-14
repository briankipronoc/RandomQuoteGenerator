// build.gradle.kts (Project level)

plugins {
    id("com.android.application") version "8.2.2" apply false
    // Use Kotlin version 1.9.22 consistently
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    // Use Kotlinx Serialization plugin version compatible with Kotlin 1.9.22
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.22" apply false
    // Use KSP version compatible with Kotlin 1.9.22
    id("com.google.devtools.ksp") version "1.9.22-1.0.17" apply false
}