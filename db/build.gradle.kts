plugins {
    alias(libs.plugins.ksp)
    alias(libs.plugins.android.library)
    alias(libs.plugins.hilt)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.room)
}

android {
    namespace = "org.zayass.assessment.exchange.db"
    compileSdk = 34

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    api(project(":domain"))

    implementation(libs.hilt.android)
    implementation(libs.androidx.core.ktx)
    ksp(libs.hilt.compiler)
    implementation(libs.kotlinx.coroutines.core)

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.junit)
}