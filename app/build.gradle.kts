import java.util.Properties
import java.io.FileInputStream

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
if (keystorePropertiesFile.exists()) {
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
}

android {
    configureAndroid(this)
    configureBuildConfig(this)
    signingConfigs {
        create("release") {
            keystoreProperties["storeFile"]?.let {
                keyAlias = keystoreProperties["keyAlias"].toString()
                keyPassword = keystoreProperties["keyPassword"].toString()
                storeFile = file(it)
                storePassword = keystoreProperties["storePassword"].toString()
            }
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
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

    implementation(Libs.androidx_multidex_multidex)

    implementation(Libs.kotlin_stdlib_jdk8)
    implementation(Libs.browser)

    implementation(Libs.kotlinx_coroutines_core)
    implementation(Libs.kotlinx_coroutines_android)

    implementation(Libs.eventbus)
    kapt(Libs.eventbus_annotation_processor)

    implementation(Libs.exoplayer_core)
    implementation(Libs.exoplayer_ui)
    implementation(Libs.exoplayer_hls)
    implementation(Libs.extension_mediasession)
    implementation(Libs.firebase_messaging)
    implementation(Libs.push_notifications_android)
    implementation(Libs.gson)

    api(Libs.appcompat)
    api(Libs.constraintlayout)
    api(Libs.lifecycle_extensions)
    api(Libs.lifecycle_viewmodel)

    testImplementation(Libs.junit)
    androidTestImplementation(Libs.androidx_test_runner)
    androidTestImplementation(Libs.espresso_core)
}

kapt {

    arguments {
        arg("eventBusIndex", "to.dev.dev_android.webclients.EventBusClientIndex")
    }
}

apply(plugin = "com.google.gms.google-services")
