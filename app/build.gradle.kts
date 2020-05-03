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

    implementation(Libs.kotlin_stdlib_jdk8)
    implementation(Libs.browser)

    implementation("com.google.android.exoplayer:exoplayer-core:2.11.4")
    implementation("com.google.android.exoplayer:exoplayer-ui:2.11.4")
    implementation("com.google.android.exoplayer:extension-mediasession:2.11.4")
    implementation("com.google.firebase:firebase-messaging:18.0.0")
    implementation("com.pusher:push-notifications-android:1.6.2")

    testImplementation(Libs.junit)
    androidTestImplementation(Libs.androidx_test_runner)
    androidTestImplementation(Libs.espresso_core)
}

apply(plugin = "com.google.gms.google-services")