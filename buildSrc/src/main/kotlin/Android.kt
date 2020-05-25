import com.android.build.gradle.BaseExtension
import com.android.build.gradle.internal.dsl.BuildType
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.ExtraPropertiesExtension
import org.gradle.kotlin.dsl.extra


fun ExtraPropertiesExtension.string(key: String): String = (get(key) as String)

fun ExtraPropertiesExtension.int(key: String): Int = (get(key) as String).toInt()

fun BuildType.buildConfigString(name: String, value: String) =
    buildConfigField("String", name, "\"$value\"")


fun Project.configureAndroid(android: BaseExtension) {
    val properties = listOf("android.compileSdkVersion", "android.minSdkVersion", "android.targetSdkVersion", "android.versionCode")
    val found = properties.map { extra.has(it) }
    require(found.all { it }) { "Missing some properties=$properties => found=$found" }
    val versions = properties.map { p -> extra.int(p) }

    android.compileSdkVersion(versions[0])
    android.defaultConfig {
        minSdkVersion(versions[1])
        targetSdkVersion(versions[2])
        versionCode = versions[3]
        versionName = extra.string("android.versionName")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        multiDexEnabled = true
    }


    android.compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

fun Project.configureBuildConfig(android: BaseExtension) {
    android.buildTypes.all {
        resValue("string", "baseUrl", extra.string("chrome.baseUrl"))
        buildConfigString("baseUrl", extra.string("chrome.baseUrl"))
        resValue("string", "baseHostname", extra.string("chrome.baseHostname"))
        buildConfigString("baseHostname", extra.string("chrome.baseHostname"))
        resValue("string", "pusherInstanceId", extra.string("pusher.instanceId"))
        buildConfigString("pusherInstanceId", extra.string("pusher.instanceId"))
        resValue("string", "userAgent", extra.string("chrome.userAgent"))
        buildConfigString("userAgent", extra.string("chrome.userAgent"))
    }
}


