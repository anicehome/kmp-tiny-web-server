import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
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
    
    jvm("desktop")
    
    sourceSets {
        val desktopMain by getting
        
        androidMain.dependencies {
        }
        commonMain.dependencies {
            implementation(projects.core.domain)
            implementation(projects.feature.home)
            implementation(projects.feature.setting)
        }
        desktopMain.dependencies {
        }
    }
}

android {
    namespace = "io.github.anicehome.webserver"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "io.github.anicehome.webserver"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "io.github.anicehome.webserver.MainKt"

        buildTypes.release.proguard {
            isEnabled.set(true)
            configurationFiles.from(project.file("compose-desktop.pro"))
        }
        nativeDistributions {
            modules("jdk.unsupported")
            packageVersion = "1.0.0"
            packageName = "TinyWebServer"
            description = "Web Server"
            appResourcesRootDir.set(project.layout.projectDirectory.dir("resources"))
            linux {
                iconFile.set(project.file("assets/icon.png"))
            }

            windows {
                iconFile.set(project.file("assets/icon.ico"))
                dirChooser = false
                perUserInstall = true
                shortcut = true
            }

            macOS {
                bundleID = "io.github.anicehome.webserver"
                iconFile.set(project.file("assets/icon.icns"))
            }

            targetFormats(
                TargetFormat.Exe,
                TargetFormat.Rpm,
                TargetFormat.Deb,
                TargetFormat.Msi,
                TargetFormat.Pkg,
                TargetFormat.Dmg,
            )
        }
    }
}
