import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "${Config.namespace}.receiver"
version = "1.0-SHAPSHOT"

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = Config.jvmTarget
        }
        withJava()
    }

    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(project(":sharedReceiver"))
                implementation(compose.desktop.currentOs)
            }


        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "DeskMotion"
            packageVersion = "1.0.0"
        }
    }
}