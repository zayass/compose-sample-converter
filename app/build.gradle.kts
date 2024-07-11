plugins {
    alias(libs.plugins.convention.android.application)
    alias(libs.plugins.convention.android.application.compose)
    alias(libs.plugins.convention.hilt)
}

android {
    namespace = "org.zayass.assessment.exchange"

    defaultConfig {
        applicationId = "org.zayass.assessment.exchange"
        versionCode = 1
        versionName = "1.0"
    }
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:network"))
    implementation(project(":core:db"))

    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.compose.android)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.hilt.navigation.compose)

    testImplementation(libs.bundles.test)
}