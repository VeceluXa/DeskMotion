plugins {
    id("com.android.application")
    kotlin("android")
    id("org.jetbrains.compose")
}

android {
    namespace = "${Config.namespace}.sender"
    compileSdk = Config.Android.compileSdk
    defaultConfig {
        applicationId = "${Config.namespace}.sender"
        minSdk = Config.Android.minSdk
        targetSdk = Config.Android.targetSdk
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
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
        sourceCompatibility = Config.javaVersion
        targetCompatibility = Config.javaVersion
    }
    kotlinOptions {
        jvmTarget = Config.jvmTarget
    }
}

dependencies {
    implementation(project(":sharedSender"))
    with(compose) {
        implementation(ui)
        implementation(uiTooling)
        implementation(preview)
        implementation(foundation)
        implementation(material3)
    }
    implementation("androidx.activity:activity-compose:1.7.1")
}