apply(from = "../jacoco.gradle.kts")

plugins {
    id(BuildPlugins.androidApplication)
    id(BuildPlugins.kotlinAndroid)
    kotlin("android")
    kotlin("kapt")
    id(BuildPlugins.parcelize)
    id(BuildPlugins.safeArgs)
    id(BuildPlugins.ktlint)
    id(BuildPlugins.hilt)
    jacoco
}

jacoco {
    toolVersion = Versions.jacoco
}

android {
    compileSdk = AndroidSdk.compile

    defaultConfig {
        applicationId = "com.v.panatchai.cryptocurrency"
        minSdk = AndroidSdk.min
        targetSdk = AndroidSdk.target
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "COIN_API", "\"https://api.coinstats.app/\"")
        buildConfigField("String", "COIN_DATABASE", "\"CoinDatabase\"")

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.schemaLocation" to "$projectDir/schemas",
                    "room.incremental" to "true",
                    "room.expandProjection" to "true"
                )
            }
        }
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            isShrinkResources = false
        }
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs += listOf("-opt-in=kotlin.RequiresOptIn")
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }
    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
}

tasks {
    register<Delete>("deleteGitHook") {
        group = "utils"
        description = "Deleting githook"

        val preCommit = "${rootProject.rootDir}/.git/hooks/pre-commit"
        if (file(preCommit).exists()) {
            delete(preCommit)
        }
    }

    register<Copy>("installGitHook") {
        group = "utils"
        description = "Adding githook"

        from("${rootProject.rootDir}/scripts/pre-commit")
        into("${rootProject.rootDir}/.git/hooks")
        fileMode = 0b111101101
    }

    getByName("build").dependsOn("installGitHook")
    getByName("clean").dependsOn("deleteGitHook")
}

dependencies {
    implementation(Libraries.coreKtx)
    implementation(Libraries.appCompat)
    implementation(Libraries.constraintLayout)
    implementation(Libraries.kotlinStdlib)
    implementation(Libraries.jacoco)
    implementation(Libraries.hiltAndroid)
    implementation(TestLibraries.junitKtx)
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    kapt(Libraries.hiltKapt)
    implementation(Libraries.retrofit)
    implementation(Libraries.moshiConverter)
    implementation(Libraries.roomRuntime)
    annotationProcessor(Libraries.roomCompiler)
    kapt(Libraries.roomCompiler)
    kapt(Libraries.glideCompiler)
    implementation(Libraries.glideRuntime)
    implementation(Libraries.roomKtx)
    implementation(Libraries.roomPaging)
    implementation(Libraries.paging)
    implementation(Libraries.materialDesign3)
    implementation(Libraries.recyclerView)
    implementation(Libraries.activityKtx)
    implementation(Libraries.fragmentKtx)
    implementation(Libraries.lifecycleRuntime)
    implementation(Libraries.lifecycleViewModel)
    implementation(Libraries.pagingCommonKtx)
    implementation(Libraries.pagingRuntimeKtx)
    implementation(Libraries.preferenceKtx)
    implementation(Libraries.swipeRefresh)
    implementation(Libraries.navigationFragment)
    implementation(Libraries.navigationUi)
    implementation(TestLibraries.fragmentKtx)
    testImplementation(TestLibraries.junit4)
    testImplementation(TestLibraries.room)
    testImplementation(TestLibraries.paging)
    testImplementation(TestLibraries.robolectric)
    testImplementation(TestLibraries.truth)
    testImplementation(TestLibraries.mockito)
    testImplementation(TestLibraries.coroutineKtx)
    testImplementation(TestLibraries.coreTest)
    testImplementation(TestLibraries.navigation)
    androidTestImplementation(TestLibraries.junitExt)
    androidTestImplementation(TestLibraries.espresso)
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}
