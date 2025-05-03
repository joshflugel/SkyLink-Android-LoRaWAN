// Enforce consistent Kotlin version across all subprojects
subprojects {
    configurations.all {
        resolutionStrategy.eachDependency {
            if (requested.group == "org.jetbrains.kotlin") {
                useVersion("1.9.23")
            }
        }
    }
}

// Top-level plugin declarations
plugins {
    id("com.android.application") version "8.8.2" apply false
    id("com.android.library") version "8.8.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.23" apply false
    id("org.jetbrains.kotlin.jvm") version "1.9.23" apply false
    id("androidx.navigation.safeargs") version "2.8.9" apply false
    id("androidx.navigation.safeargs.kotlin") version "2.8.9" apply false
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
}
