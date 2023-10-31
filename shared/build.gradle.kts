plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
    id("dev.icerock.mobile.multiplatform-resources")
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()

    java {
        sourceCompatibility = Config.javaVersion
        targetCompatibility = Config.javaVersion
    }

    jvm("desktop") {
        compilations.all {
            kotlinOptions {
                jvmTarget = Config.jvmTarget
            }
        }
    }

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

                api(Dependencies.kermit)
            }
        }
        val commonTest by getting

        val androidMain by getting {
            dependsOn(commonMain)
        }
        val androidInstrumentedTest by getting
        val androidUnitTest by getting

        val desktopMain by getting {
            dependsOn(commonMain)
        }
        val desktopTest by getting
    }
}

android {
    namespace = Config.namespace
    compileSdk = Config.Android.compileSdk
    defaultConfig {
        minSdk = Config.Android.minSdk
    }

    compileOptions {
        sourceCompatibility = Config.javaVersion
        targetCompatibility = Config.javaVersion
    }
}

multiplatformResources {
    multiplatformResourcesPackage = Config.namespace
    multiplatformResourcesClassName = "SharedRes"
}

task("testClasses").doLast {
    println("This is a workaround to skip testClasses gradle task")
}
