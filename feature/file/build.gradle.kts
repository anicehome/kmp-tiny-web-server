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
            implementation(projects.core.database)
            implementation(projects.core.system)
            implementation(projects.core.resource)
            implementation(libs.kotlinx.datetime)
            implementation(libs.ktor.server.html.builder)
        }
        androidMain.dependencies {
        }
        jvmMain.dependencies {
        }
    }
}

android {
    namespace = "io.github.anicehome.webserver.file"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}