import org.jetbrains.kotlin.gradle.utils.`is`
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.dagger)
    kotlin("kapt")
}

android {
    namespace = "com.example.aichat"
    compileSdk = 35

    val localProperties = Properties()
    val localPropertiesFile = File(rootDir,"Secret.properties")
    if(localPropertiesFile.exists() && localPropertiesFile.isFile){
        localPropertiesFile.inputStream().use {
            localProperties.load(it)
        }
    }


    defaultConfig {
        applicationId = "com.example.aichat"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String","API_KEY",localProperties.getProperty("API_KEY"))
        }
        debug {
            buildConfigField("String","API_KEY",localProperties.getProperty("API_KEY"))
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.retrofit)
    implementation(libs.gson)
    implementation(libs.okhttp3)
    implementation(libs.coroutine)
    implementation(libs.coroutines)

    implementation(libs.dagger.hilt)
    implementation(libs.hilt.compose.navigation)
    kapt(libs.dagger.kapt)
    implementation(libs.navigation)
    implementation(libs.generativeai)

}