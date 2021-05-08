// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        val kotlinVersion = findProperty("version.org.jetbrains.kotlin") as String
        val agpVersion = findProperty("version.com.android.tools.build..gradle") as String
        classpath("com.android.tools.build:gradle:$agpVersion")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("com.google.gms:google-services:4.2.0")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}
plugins {
  id("io.gitlab.arturbosch.detekt") version "1.1.1"
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}


tasks.register("hello") {
    group = "custom"
    description = "Hello World task - useful to solve build problems"
}
tasks.register("install") {
    group = "custom"
    description = "Build and install the app"
    dependsOn(":app:installDebug")
}
tasks.register("test") {
    group = "custom"
    description = "Run the unit tests"
    dependsOn(":app:testDebugUnitTest")
}
tasks.register("androidTest") {
    group = "custom"
    description = "Run android instrumentation tests"
    dependsOn(":app:connectedDebugAndroidTest")
}

detekt {
    input = files("$projectDir/app/src/main/java")
    config = files("$projectDir/config/detekt/detekt.yml")
    reports {
        xml {
            enabled = true
            destination = file("$projectDir/reports/detekt.xml")
        }
        html {
            enabled = true
            destination = file("$projectDir/reports/detekt.html")
        }
    }
    parallel = true
}


tasks.named<Wrapper>("wrapper") {
    distributionType = Wrapper.DistributionType.ALL
}

tasks.register("runOnGitHub") {
    // Documentation: https://guides.gradle.org/writing-gradle-tasks/
    dependsOn(":app:testDebugUnitTest")
    group = "custom"
    description = "$ ./gradlew runOnGitHub # runs on GitHub Action"
}