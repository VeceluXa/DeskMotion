plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
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
                implementation(project(":shared"))

                with(compose) {
                    implementation(ui)
                    implementation(runtime)
                    implementation(foundation)
                    implementation(material3)
                    implementation(uiTooling)
                    implementation(preview)
                }

                with(Dependencies.Koin) {
                    implementation(compose)
                    implementation(core)
                }

                with(Dependencies.MokoResources) {
                    implementation(resources)
                    implementation(resourcesCompose)
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
        }
        val androidUnitTest by getting
        val androidInstrumentedTest by getting
    }
}

android {
    namespace = "${Config.namespace}.sender"
    compileSdk = Config.Android.compileSdk
    defaultConfig {
        minSdk = Config.Android.minSdk
    }
}

multiplatformResources {
    multiplatformResourcesPackage = "${Config.namespace}.sender"
    multiplatformResourcesClassName = "SenderRes"
}