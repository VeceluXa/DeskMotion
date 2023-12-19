plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("org.jetbrains.compose")
    id("dev.icerock.mobile.multiplatform-resources")
    id("app.cash.sqldelight")
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()

    jvm("desktop") {
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

                with(compose) {
                    implementation(ui)
                    implementation(runtime)
                    implementation(foundation)
                    implementation(material)
                    implementation(material3)
                    implementation(uiTooling)
                    implementation(preview)
                    implementation(materialIconsExtended)
                }

                with(Dependencies.MviKotlin) {
                    implementation(mviKotlin)
                    implementation(main)
                    implementation(logging)
                    implementation(coroutinesExtensions)
                }

                with(Dependencies.SQLDelight) {
                    implementation(runtime)
                    implementation(coroutinesExtensions)
                    implementation(primitiveAdapters)
                }

                implementation(Dependencies.aayChart)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val desktopMain by getting {
            dependsOn(commonMain)
            dependencies {
                implementation(Dependencies.SQLDelight.jvmDriver)
                implementation(Dependencies.apacheCommons)
            }
        }
        val desktopTest by getting
    }
}

sqldelight {
    databases {
        create("DeskMotionDatabase") {
            packageName.set("com.danilovfa.deskmotion.receiver.deskMotionDatabase")
        }
    }
}