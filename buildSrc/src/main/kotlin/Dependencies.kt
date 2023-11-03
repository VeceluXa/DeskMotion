object Dependencies {
    object Koin {
        private const val nameSpace = "io.insert-koin"
        const val android = "$nameSpace:koin-android:${Versions.Koin.koin}"
        const val core = "$nameSpace:koin-core:${Versions.Koin.koin}"
        const val ktor = "$nameSpace:koin-ktor:${Versions.Koin.koin}"
        const val compose = "$nameSpace:koin-compose:${Versions.Koin.compose}"
    }

    object MokoResources {
        private const val nameSpace = "dev.icerock.moko"
        const val gradlePlugin = "$nameSpace:resources-generator:${Versions.mokoResources}"
        const val resources = "$nameSpace:resources:${Versions.mokoResources}"
        const val resourcesCompose = "$nameSpace:resources-compose:${Versions.mokoResources}"
    }

    object Decompose {
        private const val nameSpace = "com.arkivanov.decompose"
        const val decompose = "$nameSpace:decompose:${Versions.decompose}"
        const val composeExtensions = "$nameSpace:extensions-compose-jetbrains:${Versions.decompose}"
    }

    object MviKotlin {
        private const val nameSpace = "com.arkivanov.mvikotlin"
        const val mviKotlin = "$nameSpace:mvikotlin:${Versions.mviKotlin}"
        const val main = "$nameSpace:mvikotlin-main:${Versions.mviKotlin}"
        const val logging = "$nameSpace:mvikotlin-logging:${Versions.mviKotlin}"
        const val coroutinesExtensions = "$nameSpace:mvikotlin-extensions-coroutines:${Versions.mviKotlin}"
    }

    const val kotlinxSerialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.kotlinxSerialization}"
    const val kermit = "co.touchlab:kermit:${Versions.kermit}"
}