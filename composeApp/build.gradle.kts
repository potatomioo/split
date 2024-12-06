import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
//    alias(libs.plugins.google.playServices)
    alias(libs.plugins.firebase.crashlytics)
//    alias(libs.plugins.kotlinCocoapods)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    jvm("desktop")

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

//    ios {
//        binaries {
//            framework {
//                baseName = "ComposeApp"
//            }
//        }
//    }

//    cocoapods {
//        summary = "Description of the ComposeApp shared module"
//        homepage = "https://homepage.link" // Replace with your module's homepage
//        ios.deploymentTarget = "14.1"
//        podfile = project.file("../iosApp/Podfile")
//
//        framework {
//            baseName = "ComposeApp"
//        }
//    }
    sourceSets.commonMain {
        kotlin.srcDir("build/generated/ksp/metadata")
    }
    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)

            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.core.splashscreen)
            implementation(libs.androidx.material3.android)  // Use Material3
//            implementation(libs.accompanist.placeholder.material)
            implementation(libs.accompanist.swiperefresh)
            implementation(libs.accompanist.placeholder.material)
            implementation(libs.accompanist.webview)
            implementation(libs.lottie.compose.android)
            implementation(libs.androidx.material3)

            implementation(libs.androidx.paging.compose.v330)
        }
        commonMain.dependencies {
            implementation(compose.runtime)

            implementation(compose.foundation)
            implementation(compose.material3)  // Replace material with material3
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)

            api(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.navigation.compose)

            implementation(libs.bundles.ktor)

            api(libs.datastore.preferences)
            api(libs.datastore)

            api(libs.moko.permissions)
            api(libs.moko.permissions.compose)

            implementation(libs.kmpauth.google) //Google One Tap Sign-In
//            implementation(libs.kmpauth.firebase) //Integrated Authentications with Firebase
            implementation(libs.kmpauth.uihelper) //UiHelper SignIn buttons (AppleSignIn, GoogleSignInButton)

            // Coil
            implementation(libs.coil.compose.core)
            implementation(libs.coil.compose)
            implementation(libs.coil)
            implementation(libs.coil.network.ktor)

            // Room
            implementation(libs.room.runtime)
            implementation(libs.sqlite.bundled)
            implementation(libs.androidx.room.paging)

            // Back Handling
            implementation(libs.foundation)
            implementation(libs.decompose)
            implementation(libs.decompose.extension)
            implementation(libs.paging.common)
            implementation(libs.paging.common.compose)

            implementation(libs.kotlinx.datetime)


            implementation(libs.firebase.bom)
        }
        nativeMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.accompanist.swiperefresh)
            implementation(libs.accompanist.placeholder.material)
            implementation(libs.accompanist.webview)
//            implementation(libs.androidx.paging.common.jvm)
//            implementation("app.cash.paging:paging-runtime-uikit:3.3.0-alpha02-0.5.1")
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.oshi.core)

            implementation(libs.ktor.client.okhttp)
            implementation(libs.lottie.compose.desktop)
        }
    }
}

android {
    namespace = "com.falcon.split"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "com.falcon.split"
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
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
    dependencies {
        debugImplementation(compose.uiTooling)
    }
}
dependencies {
    implementation(libs.androidx.material3.android)
    implementation(libs.androidx.material3)
}

compose.desktop {
    application {
        mainClass = "com.falcon.split.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.split.news"
            packageVersion = "1.0.0"
        }
    }
}


room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
//    implementation(libs.androidx.material3.jvmstubs)
    ksp(libs.room.compiler)
}