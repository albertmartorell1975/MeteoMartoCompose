// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.dokka)
}

dependencies {
    dokka(project(":app"))
    dokka(project(":domain"))
    dokka(project(":data"))
    dokka(project(":usecases"))
}

// Dokka Optimization: Tasks are disabled by default to speed up regular compilation.
// Use -PgenerateDocs to enable them.
val isDocGenRequested = project.hasProperty("generateDocs")

tasks.withType<org.jetbrains.dokka.gradle.tasks.DokkaGenerateTask>().configureEach {
    enabled = isDocGenRequested
    if (isDocGenRequested) {
        generator.moduleName.set("MeteoMarto Technical Reference")
        outputDirectory.set(layout.buildDirectory.dir("dokka/technical-reference"))
    }
}
