object Versions {
    /**
     * Best practice is that each block below should have its own separated Versions.
     * The reason is that sometimes libraries depend on the same dependency, however,
     * but different versions.
     */
    const val androidAppVersion = "7.1.3"
    const val androidLibraryVersion = "7.1.3"
    const val jetbrainsKotlinVersion = "1.5.30"
    const val ktlintVersion = "3.10.0"
    const val kotlinVersion = "1.6.20"

    const val coreKtx = "1.7.0"
    const val jetpack = "1.4.1"
    const val material = "1.5.0"
    const val constraintLayout = "2.1.3"
    const val junit4 = "4.13.2"
    const val junitExt = "1.1.3"
    const val espressoCore = "3.4.0"
    const val jacoco = "0.8.8"
    const val hilt = "2.38.1"
}

object BuildPlugins {
    const val androidApplication = "com.android.application"
    const val androidLibrary = "com.android.library"
    const val jetbrainsKotlin = "org.jetbrains.kotlin.android"
    const val kotlinAndroid = "kotlin-android"
    const val ktlint = "org.jmailen.kotlinter"
    const val hilt = "dagger.hilt.android.plugin"
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
    const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlinVersion}"
    const val kotlinGradlePlugin =
        "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlinVersion}"
    const val jacoco = "org.jacoco:org.jacoco.core:${Versions.jacoco}"
    const val hilt = "com.google.dagger:hilt-android-gradle-plugin:${Versions.hilt}"
    const val hiltAndroid = "com.google.dagger:hilt-android:${Versions.hilt}"
    const val hiltKapt = "com.google.dagger:hilt-android-compiler:${Versions.hilt}"
}

object TestLibraries {
    const val junit4 = "junit:junit:${Versions.junit4}"
    const val junitExt = "androidx.test.ext:junit:${Versions.junitExt}"
    const val espresso = "androidx.test.espresso:espresso-core:${Versions.espressoCore}"
}

object BuildTaskGroups {
    const val verification = "verification"
}
