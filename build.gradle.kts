// Top-level build file where you can add configuration options common to all sub-projects/modules.
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id(BuildPlugins.androidApplication) version (Versions.androidAppVersion) apply (false)
    id(BuildPlugins.androidLibrary) version (Versions.androidLibraryVersion) apply (false)
    id(BuildPlugins.jetbrainsKotlin) version (Versions.jetbrainsKotlinVersion) apply (false)
    id(BuildPlugins.ktlint) version (Versions.ktlintVersion) apply (false)
}

buildscript {
    dependencies {
        classpath (Libraries.kotlinGradlePlugin)
    }
}

tasks.register("clean", Delete::class).configure {
    delete(rootProject.buildDir)
}
