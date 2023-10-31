pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "DeskMotion"
include(":sharedSender", ":sharedReceiver", ":shared")
include(":senderAndroidApp")
include(":receiverDesktopApp")
