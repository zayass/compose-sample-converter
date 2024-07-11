package org.zayass.assessment.exchange.buildlogic

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.withType

internal fun Project.configureTestLogging() {
    tasks.withType<Test> {
        testLogging {
            showStandardStreams = true
            events("standardOut", "passed", "skipped", "failed")
        }
    }
}

internal fun includeAndroidResourcesInUnitTests(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        @Suppress("UnstableApiUsage")
        testOptions {
            unitTests {
                // For Robolectric
                isIncludeAndroidResources = true
            }
        }
    }
}
