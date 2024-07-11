plugins {
    alias(libs.plugins.ksp)
    alias(libs.plugins.jetbrains.kotlin.jvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.core)
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(libs.bundles.test)
}