import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = Config.packageReceiver
version = Config.version

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
            packageVersion = Config.version
        }
    }
}