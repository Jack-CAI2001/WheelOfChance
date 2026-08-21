plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.jetbrains.kotlin.plugin.serialization)
    alias(libs.plugins.androidx.room)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.androidx.room.ktx)
            implementation(libs.material)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(compose.materialIconsExtended)
            
            implementation(libs.androidx.lifecycle.viewmodel.compose)
            implementation(libs.androidx.lifecycle.runtime.compose)
            
            // Navigation 3
            implementation(libs.androidx.navigation3.runtime)
            implementation(libs.androidx.navigation3.ui)
            implementation(libs.androidx.compose.adaptive)
            implementation(libs.androidx.compose.adaptive.layout)
            implementation(libs.androidx.compose.adaptive.navigation3)
            implementation(libs.androidx.lifecycle.viewmodel.navigation3)

            implementation(libs.androidx.room.runtime)
            implementation(libs.sqlite.bundled)
            
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.core)
            
            implementation(libs.coil.compose) // Check if Coil is KMP ready (Coil 3 is)
            
            implementation(libs.androidx.datastore.preferences)
        }
        iosMain.dependencies {
            // iOS specific dependencies
        }
    }
}

android {
    namespace = "com.example.wheelofchance"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.example.wheelofchance"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    "kspAndroid"(libs.androidx.room.compiler)
    "kspIosArm64"(libs.androidx.room.compiler)
    "kspIosSimulatorArm64"(libs.androidx.room.compiler)
}
