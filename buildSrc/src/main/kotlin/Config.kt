import org.gradle.api.JavaVersion

object Config {
    const val majorVersion = 1
    const val minorVersion = 0
    const val patchVersion = 0
    const val version = "$majorVersion.$minorVersion.$patchVersion"
    const val versionCode = majorVersion * 100 + minorVersion * 10 + patchVersion

    const val namespace = "com.danilovfa.deskmotion"
    const val packageSender = "$namespace.sender"
    const val packageReceiver = "$namespace.receiver"

    object Android {
        const val compileSdk = 34
        const val targetSdk = 34
        const val minSdk = 26
    }

    const val jvmTarget = "17"
    val javaVersion = JavaVersion.VERSION_17
}