import org.gradle.api.JavaVersion

object Config {
    const val namespace = "com.danilovfa.deskmotion"

    object Android {
        const val compileSdk = 34
        const val targetSdk = 34
        const val minSdk = 26
    }

    const val jvmTarget = "17"
    val javaVersion = JavaVersion.VERSION_17
}