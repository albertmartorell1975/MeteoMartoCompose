plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
}
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}

dependencies {
    implementation((project(":domain")))
    implementation(libs.javax.inject.v1)
    //Arrow
    implementation(libs.arrow.core)
    implementation(libs.arrow.fx.coroutines)
    //Retrofit
    implementation(libs.retofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)
}
