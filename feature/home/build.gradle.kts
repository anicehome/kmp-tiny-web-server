import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    jvm()
    sourceSets {
        commonMain.dependencies {
            api(projects.feature.base)
            implementation(projects.core.resource)
            implementation(projects.feature.file)
            implementation(projects.feature.setting)
            implementation(projects.feature.service)
            implementation(libs.org.jetbrinas.compose.material3.adaptive)
            implementation(libs.org.jetbrinas.compose.material3.adaptive.layout)
            implementation(libs.org.jetbrinas.compose.material3.adaptive.navigation)
            implementation(libs.org.jetbrinas.compose.material3.adaptive.navigation.suite)
        }
        androidMain.dependencies {
        }
        jvmMain.dependencies {
        }
    }
}

android {
    namespace = "io.github.anicehome.webserver.home"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}