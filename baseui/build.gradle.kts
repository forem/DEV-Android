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
            resValue("string", "baseUrl", "\"https://dev.to/\"")
            buildConfigField("String", "baseUrl", "\"https://dev.to/\"")
        }
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            resValue("string", "baseUrl", "\"https://dev.to/\"")
            buildConfigField("String", "baseUrl", "\"https://dev.to/\"")
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

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${BuildConfig.kotlinVersion}")
    api("androidx.appcompat:appcompat:1.0.2")
    api("androidx.constraintlayout:constraintlayout:1.1.3")
    api("androidx.lifecycle:lifecycle-extensions:2.0.0")
    api("androidx.lifecycle:lifecycle-viewmodel:2.0.0")

    testImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test:runner:1.2.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
}
