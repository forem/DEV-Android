import to.dev.dev_android.build.BuildConfig

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-android-extensions")
}

android {
    compileSdkVersion(BuildConfig.compileSdkVersion)
    defaultConfig {
        minSdkVersion(BuildConfig.minSdkVersion)
        targetSdkVersion(BuildConfig.targetSdkVersion)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("debug") {
            resValue("string", "baseUrl", "\"https://dev.to\"")
            buildConfigField("String", "baseUrl", "\"https://dev.to\"")
        }
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            resValue("string", "baseUrl", "\"https://dev.to\"")
            buildConfigField("String", "baseUrl", "\"https://dev.to\"")
        }
    }

    dataBinding {
        isEnabled = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(Libs.kotlin_stdlib_jdk8)
    api(Libs.appcompat)
    api(Libs.constraintlayout)
    api(Libs.lifecycle_extensions)
    api(Libs.lifecycle_viewmodel)

    testImplementation(Libs.junit)
    androidTestImplementation(Libs.androidx_test_runner)
    androidTestImplementation(Libs.espresso_core)
}
