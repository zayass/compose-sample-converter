plugins {
    alias(libs.plugins.convention.jvm.library)
    alias(libs.plugins.convention.hilt)
}

dependencies {
    api(project(":core:domain"))

    implementation(libs.kotlinx.coroutines.core)

    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
}