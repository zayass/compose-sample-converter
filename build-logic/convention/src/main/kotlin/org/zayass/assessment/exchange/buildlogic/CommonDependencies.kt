package org.zayass.assessment.exchange.buildlogic

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

internal fun Project.kotlinTestDependencies() {
    dependencies {
        add("testImplementation", kotlin("reflect"))
        add("testImplementation", kotlin("test"))
    }

    pluginManager.withPlugin("com.android.base") {
        dependencies {
            add("androidTestImplementation", kotlin("reflect"))
            add("androidTestImplementation", kotlin("test"))
        }
    }
}
