package org.zayass.assessment.exchange.buildlogic

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project

private const val MIN_SDK = 26
private const val COMPILE_SDK = 34

internal fun Project.configureAndroid(
    applicationExtension: ApplicationExtension,
) {
    configureKotlinAndroid(applicationExtension)

    applicationExtension.apply {
        compileSdk = COMPILE_SDK

        defaultConfig {
            minSdk = MIN_SDK
            targetSdk = COMPILE_SDK
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }

        @Suppress("UnstableApiUsage")
        testOptions.animationsDisabled = true
    }
}


internal fun Project.configureAndroid(
    libraryExtension: LibraryExtension,
) {
    configureKotlinAndroid(libraryExtension)

    libraryExtension.apply {
        compileSdk = COMPILE_SDK

        defaultConfig {
            minSdk = MIN_SDK
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }

        @Suppress("UnstableApiUsage")
        testOptions.animationsDisabled = true
    }
}