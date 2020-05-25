plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
}

android {
    configureAndroid(this)

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
        getByName("debug") {
            applicationIdSuffix = ".debug"
            isDebuggable = true
        }
    }
    dataBinding {
        isEnabled = true
    }

}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(project(path = ":baseui"))

    implementation(Libs.androidx_multidex_multidex)

    implementation(Libs.kotlin_stdlib_jdk8)
    implementation(Libs.browser)

    implementation(Libs.kotlinx_coroutines_core)
    implementation(Libs.kotlinx_coroutines_android)

    implementation(Libs.eventbus)

    implementation(Libs.exoplayer)
    implementation(Libs.extension_mediasession)
    implementation(Libs.firebase_messaging)
    implementation(Libs.push_notifications_android)

    testImplementation(Libs.junit)
    androidTestImplementation(Libs.androidx_test_runner)
    androidTestImplementation(Libs.espresso_core)
}

apply(plugin = "com.google.gms.google-services")