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

    java {
        sourceCompatibility = Config.javaVersion
        targetCompatibility = Config.javaVersion
    }

    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(project(":sharedReceiver"))
//                implementation(project(":shared"))
                implementation("org.jetbrains.skiko:skiko-awt-runtime-windows-x64:0.7.85.4")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.7.3")
                implementation(compose.desktop.currentOs)
            }
        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "com.danilovfa.deskmotion.receiver.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "DeskMotion"
            packageVersion = Config.version
        }
    }
}