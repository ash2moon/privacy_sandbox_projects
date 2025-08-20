pluginManagement {
    val flutterSdkPath =
        run {
            val properties = java.util.Properties()
            file("local.properties").inputStream().use { properties.load(it) }
            val flutterSdkPath = properties.getProperty("flutter.sdk")
            require(flutterSdkPath != null) { "flutter.sdk not set in local.properties" }
            flutterSdkPath
        }

    includeBuild("$flutterSdkPath/packages/flutter_tools/gradle")

    repositories {
        maven("https://androidx.dev/snapshots/builds/11514708/artifacts/repository/")
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("dev.flutter.flutter-plugin-loader") version "1.0.0"
    id("com.android.application") version "8.11.1" apply false
    id("com.android.library") version "8.11.1" apply false
    id("androidx.privacysandbox.library") version "1.0.0-alpha02" apply false
    id("com.google.devtools.ksp") version "2.1.20-1.0.31" apply false
    id("org.jetbrains.kotlin.jvm") version "2.1.20" apply false
    id("org.jetbrains.kotlin.android") version "2.1.20" apply false
}

include(":app")
include(":runtime-enabled-sdk")
include(":runtime-enabled-sdk-bundle")
include(":inapp-mediatee-sdk")
include(":inapp-mediatee-sdk-adapter")
include(":mediatee-sdk")
include(":mediatee-sdk-bundle")
include(":mediatee-sdk-adapter")
include(":mediatee-sdk-adapter-bundle")
include(":runtime-aware-sdk")
