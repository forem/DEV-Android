plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
    jcenter()
}

dependencies {
    implementation("com.android.tools.build:gradle:3.5.0") // keep in sync with gradle.properties
}

kotlinDslPluginOptions {
    experimentalWarning.set(false)
}