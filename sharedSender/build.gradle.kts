plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.android.library")
    id("dev.icerock.mobile.multiplatform-resources")
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()

    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = Config.jvmTarget
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":shared"))

                with(Dependencies.MviKotlin) {
                    implementation(mviKotlin)
                    implementation(main)
                    implementation(logging)
                    implementation(coroutinesExtensions)
                }
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val androidMain by getting {
            dependsOn(commonMain)
            dependencies {
                implementation(Dependencies.Koin.android)
            }
        }
        val androidUnitTest by getting
        val androidInstrumentedTest by getting
    }
}

android {
    namespace = Config.packageSender
    compileSdk = Config.Android.compileSdk
    defaultConfig {
        minSdk = Config.Android.minSdk
    }
    compileOptions {
        sourceCompatibility = Config.javaVersion
        targetCompatibility = Config.javaVersion
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.composeCompiler
    }
}