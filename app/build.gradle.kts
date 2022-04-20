plugins {
    id(BuildPlugins.androidApplication)
    id(BuildPlugins.kotlinAndroid)
    kotlin("android")
    id(BuildPlugins.ktlint)
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
    implementation(Libraries.materialDesign)
    implementation(Libraries.constraintLayout)
    implementation(Libraries.kotlinStdlib)
    testImplementation(TestLibraries.junit4)
    androidTestImplementation(TestLibraries.junitExt)
    androidTestImplementation(TestLibraries.espresso)
}
