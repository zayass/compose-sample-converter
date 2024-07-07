plugins {
    kotlin("jvm")
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.javax.inject)

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.junit)
}