object Versions {
    /**
     * Best practice is that each block below should have its own separated Versions.
     * The reason is that sometimes libraries depend on the same dependency, however,
     * but different versions.
     */
    const val androidAppVersion = "7.1.3"
    const val androidLibraryVersion = "7.1.3"
    const val jetbrainsKotlinVersion = "1.6.21"
    const val ktlintVersion = "3.10.0"
    const val kotlinVersion = "1.6.20"

    const val coreKtx = "1.7.0"
    const val jetpack = "1.4.1"
    const val constraintLayout = "2.1.3"
    const val junit4 = "4.13.2"
    const val junitExt = "1.1.3"
    const val espressoCore = "3.4.0"
    const val jacoco = "0.8.8"
    const val hilt = "2.38.1"
    const val retrofit = "2.9.0"
    const val moshiConverter = "2.9.0"
    const val room = "2.4.2"
    const val roomPaging = "2.5.0-alpha01"
    const val paging = "3.1.1"
    const val materialDesign = "1.7.0-alpha01"
    const val recyclerView = "1.2.1"
    const val swipeRefresh = "1.1.0"

    const val activityKtx = "1.4.0"
    const val fragmentKtx = "1.4.1"
    const val lifecycleRuntime = "2.5.0-beta01"
    const val lifecycleViewModel = "2.5.0-beta01"
    const val pagingCommonKtx = "2.1.2"
    const val preferenceKtx = "1.2.0"
    const val glide = "4.13.0"
    const val robolectric = "4.6"
    const val truth = "1.1.3"
    const val junitKtx = "1.1.3"
    const val mockito = "4.0.0"
    const val coroutineKtx = "1.6.1"
    const val coreTest = "2.1.0"
    const val navigation = "2.4.2"
}

object BuildPlugins {
    const val androidApplication = "com.android.application"
    const val androidLibrary = "com.android.library"
    const val jetbrainsKotlin = "org.jetbrains.kotlin.android"
    const val kotlinAndroid = "kotlin-android"
    const val ktlint = "org.jmailen.kotlinter"
    const val hilt = "dagger.hilt.android.plugin"
    const val parcelize = "kotlin-parcelize"
    const val safeArgs = "androidx.navigation.safeargs.kotlin"
}

object AndroidSdk {
    const val min = 23
    const val compile = 32
    const val target = 32
}

object Libraries {
    const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
    const val appCompat = "androidx.appcompat:appcompat:${Versions.jetpack}"
    const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlinVersion}"
    const val kotlinGradlePlugin =
        "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlinVersion}"
    const val jacoco = "org.jacoco:org.jacoco.core:${Versions.jacoco}"
    const val hilt = "com.google.dagger:hilt-android-gradle-plugin:${Versions.hilt}"
    const val hiltAndroid = "com.google.dagger:hilt-android:${Versions.hilt}"
    const val hiltKapt = "com.google.dagger:hilt-android-compiler:${Versions.hilt}"
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val moshiConverter = "com.squareup.retrofit2:converter-moshi:${Versions.moshiConverter}"
    const val roomRuntime = "androidx.room:room-runtime:${Versions.room}"
    const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"
    const val roomKtx = "androidx.room:room-ktx:${Versions.room}"
    const val roomPaging = "androidx.room:room-paging:${Versions.roomPaging}"
    const val paging = "androidx.paging:paging-runtime:${Versions.paging}"
    const val materialDesign3 = "com.google.android.material:material:${Versions.materialDesign}"
    const val recyclerView = "androidx.recyclerview:recyclerview:${Versions.recyclerView}"
    const val activityKtx = "androidx.activity:activity-ktx:${Versions.activityKtx}"
    const val fragmentKtx = "androidx.fragment:fragment-ktx:${Versions.fragmentKtx}"
    const val lifecycleRuntime =
        "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycleRuntime}"
    const val lifecycleViewModel =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycleViewModel}"
    const val pagingCommonKtx = "androidx.paging:paging-common-ktx:${Versions.pagingCommonKtx}"
    const val pagingRuntimeKtx = "androidx.paging:paging-runtime-ktx:${Versions.paging}"
    const val preferenceKtx = "androidx.preference:preference-ktx:${Versions.preferenceKtx}"
    const val swipeRefresh =
        "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swipeRefresh}"
    const val glideRuntime = "com.github.bumptech.glide:glide:${Versions.glide}"
    const val glideCompiler = "com.github.bumptech.glide:compiler:${Versions.glide}"
    const val navigationFragment =
        "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val navigationUi = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
    const val safeArgs = "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}"
}

object TestLibraries {
    const val junit4 = "junit:junit:${Versions.junit4}"
    const val junitExt = "androidx.test.ext:junit:${Versions.junitExt}"
    const val espresso = "androidx.test.espresso:espresso-core:${Versions.espressoCore}"
    const val room = "androidx.room:room-testing:${Versions.room}"
    const val paging = "androidx.paging:paging-common:${Versions.paging}"
    const val fragmentKtx = "androidx.fragment:fragment-testing:${Versions.fragmentKtx}"
    const val robolectric = "org.robolectric:robolectric:${Versions.robolectric}"
    const val truth = "com.google.truth:truth:${Versions.truth}"
    const val junitKtx = "androidx.test.ext:junit-ktx:${Versions.junitKtx}"
    const val mockito = "org.mockito.kotlin:mockito-kotlin:${Versions.mockito}"
    const val coroutineKtx =
        "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutineKtx}"
    const val coreTest = "androidx.arch.core:core-testing:${Versions.coreTest}"
    const val navigation = "androidx.navigation:navigation-testing:${Versions.navigation}"
}

object BuildTaskGroups {
    const val verification = "verification"
}
