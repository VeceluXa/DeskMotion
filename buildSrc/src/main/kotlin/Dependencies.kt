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

    const val kermit = "co.touchlab:kermit:${Versions.kermit}"
}