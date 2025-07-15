// build.gradle.kts (Project-level, in your project's root directory)

// Repositories for dependencies used by *all* modules in your project.
allprojects {
    repositories {
        google() // For AndroidX libraries, Google Play Services, etc.
        mavenCentral() // For general Maven Central libraries
    }
}

// Configuration for tasks that run after project evaluation (e.g., cleaning build directories)
tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}