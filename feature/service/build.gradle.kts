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
            implementation(projects.core.resource)
            implementation(projects.core.server)
            implementation(projects.core.system)
            implementation(libs.zxing)
            implementation(libs.org.jetbrinas.compose.material3.adaptive)
        }
        androidMain.dependencies {
            implementation(libs.accompanist.adaptive)
            implementation(libs.accompanist.permissions)
        }
        jvmMain.dependencies {
        }
    }
}

android {
    namespace = "io.github.anicehome.webserver.service"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}