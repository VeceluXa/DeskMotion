plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("dev.icerock.mobile.multiplatform-resources")
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

                with(Dependencies.MokoResources) {
                    implementation(resources)
                    implementation(resourcesCompose)
                }

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

        val desktopMain by getting {
            dependsOn(commonMain)
        }
        val desktopTest by getting
    }
}

multiplatformResources {
    multiplatformResourcesPackage = Config.packageReceiver
    multiplatformResourcesClassName = "ReceiverRes"
}