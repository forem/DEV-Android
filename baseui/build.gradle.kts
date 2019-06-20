plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
}

android {
    compileSdkVersion(28)

    defaultConfig {
        minSdkVersion(19)
        targetSdkVersion(28)
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

}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.3.31")
    api("androidx.appcompat:appcompat:1.0.2")
    api("androidx.constraintlayout:constraintlayout:1.1.3")
    api("androidx.lifecycle:lifecycle-extensions:2.0.0")
    api("androidx.lifecycle:lifecycle-viewmodel:2.0.0")

    testImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test:runner:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.1.1")
}
