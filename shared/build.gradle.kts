plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
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
                    api(ui)
                    api(runtime)
                    api(foundation)
                    api(material)
                    api(material3)
                    api(uiTooling)
                    api(preview)
                    api(materialIconsExtended)
                }

                with(Dependencies.Koin) {
                    api(compose)
                    api(core)
                }

                with(Dependencies.MokoResources) {
                    api(resources)
                    api(resourcesCompose)
                }

                with(Dependencies.Decompose) {
                    api(decompose)
                    api(composeExtensions)
                }

                with(Dependencies.MviKotlin) {
                    api(mviKotlin)
                    api(main)
                    api(logging)
                    api(coroutinesExtensions)
                }

                with(Dependencies.Preferences) {
                    api(core)
                    api(noArgs)
                    api(coroutines)
                }

                api(Dependencies.kermit)
                api(Dependencies.kotlinxSerialization)
                api(Dependencies.kotlinxDateTime)
                api(Dependencies.dataStore)
                implementation(Dependencies.ktor)
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
            dependencies {
                api(Dependencies.apacheCommons)
            }
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
}

//task("testClasses").doLast {
//    println("This is a workaround to skip testClasses gradle task")
//}
