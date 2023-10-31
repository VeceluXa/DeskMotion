plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
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

        val desktopMain by getting {
            dependsOn(commonMain)
        }
        val desktopTest by getting
    }
}

multiplatformResources {
    multiplatformResourcesPackage = "${Config.namespace}.receiver"
    multiplatformResourcesClassName = "ReceiverRes"
}