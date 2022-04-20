object Versions {
    /**
     * Best practice is that each block below should have its own separated Versions.
     * The reason is that sometimes libraries depend on the same dependency, however,
     * but different versions.
     */
    const val androidAppVersion = "7.1.3"
    const val androidLibraryVersion = "7.1.3"
    const val jetbrainsKotlinVersion = "1.5.30"

    const val coreKtx = "1.7.0"
    const val jetpack = "1.4.1"
    const val material = "1.5.0"
    const val constraintLayout = "2.1.3"
    const val junit4 = "4.13.2"
    const val junitExt = "1.1.3"
    const val espressoCore = "3.4.0"
}

object BuildPlugins {
    const val androidApplication = "com.android.application"
    const val androidLibrary = "com.android.library"
    const val jetbrainsKotlin = "org.jetbrains.kotlin.android"
    const val kotlinAndroid = "kotlin-android"
}

object AndroidSdk {
    const val min = 21
    const val compile = 32
    const val target = 32
}

object Libraries {
    const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
    const val appCompat = "androidx.appcompat:appcompat:${Versions.jetpack}"
    const val materialDesign = "com.google.android.material:material:${Versions.material}"
    const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
}

object TestLibraries {
    const val junit4 = "junit:junit:${Versions.junit4}"
    const val junitExt = "androidx.test.ext:junit:${Versions.junitExt}"
    const val espresso = "androidx.test.espresso:espresso-core:${Versions.espressoCore}"
}
