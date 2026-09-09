@file:OptIn(com.github.takahirom.roborazzi.ExperimentalRoborazziApi::class)

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.google.services)
    alias(libs.plugins.roborazzi)
    alias(libs.plugins.dokka)
    alias(libs.plugins.jacoco)
}

apply(from = "../flavors.gradle")
android {
    namespace = "com.martorell.albert.meteomartocompose"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.martorell.albert.meteomartocompose"
        minSdk = 30
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            all {
                it.systemProperties["robolectric.pixelCopyRenderMode"] = "hardware"
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

roborazzi {
    outputDir.set(file("src/test/snapshots"))
    generateComposePreviewRobolectricTests {
        enable = true
        // Following ADR 09 (Pragmatic Snapshots), we only scan the design system package
        // to avoid instantiation issues with legacy feature screens.
        packages = listOf("${android.namespace}.ui.designsystem.components")
        // Enables scanning of private @Preview functions to keep the internal component 
        // API clean while still ensuring full visual regression coverage.
        includePrivatePreviews = true
    }
}

// Dokka Optimization: Tasks are disabled by default to speed up regular compilation.
// Use -PgenerateDocs to enable them.
val isDocGenRequested = project.hasProperty("generateDocs")
val isDesignSystemMode = project.hasProperty("designSystemDocs")

tasks.withType<org.jetbrains.dokka.gradle.tasks.DokkaGenerateTask>().configureEach {
    enabled = isDocGenRequested
    
    if (isDocGenRequested) {
        if (isDesignSystemMode) {
            outputDirectory.set(layout.buildDirectory.dir("dokka/design-system"))
            generator.moduleName.set("MeteoMartoCompose Design System Reference")
            generator.dokkaSourceSets.configureEach {
                perPackageOption {
                    matchingRegex.set(".*")
                    suppress.set(true)
                }
                perPackageOption {
                    matchingRegex.set("com\\.martorell\\.albert\\.meteomartocompose\\.ui\\.components\\.designsystem.*")
                    suppress.set(false)
                }
                perPackageOption {
                    matchingRegex.set("com\\.martorell\\.albert\\.meteomartocompose\\.ui\\.theme.*")
                    suppress.set(false)
                }
            }
        } else {
            // Default App Layer documentation (used in full technical reference)
            outputDirectory.set(layout.buildDirectory.dir("dokka/technical-reference"))
            generator.moduleName.set("MeteoMartoCompose Technical Reference")
        }
    }
}

/**
 * To improve your build time by telling Dagger not to format the generated code
 */
ksp {
    arg("dagger.formatGeneratedSource", "disabled")
}

// Add or modify this block
kotlin {
    jvmToolchain(21) // Required for Roborazzi and Robolectric when targeting SDK 36+
}

dependencies {
    // Project dependencies
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":usecases"))

    // Core AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.process)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.constraintlayout.compose)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)

    // Dependency Injection (Hilt)
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.androidx.hilt.work)
    ksp(libs.hilt.compiler)
    ksp(libs.androidx.hilt.compiler)

    // Background Work
    implementation(libs.androidx.work)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)

    // Data Handling & Networking
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.bundles.arrow)
    implementation(libs.bundles.retrofit)
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    // Local Storage (Room)
    implementation(libs.bundles.room)
    ksp(libs.androidx.room.compiler)

    // Location & Permissions
    implementation(libs.google.accompanist.permissions)
    implementation(libs.google.play.services.location)

    // Design System Infrastructure
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.compose.material3.adaptive.navigation.suite)

    // Local Unit Tests
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.turbine)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.androidx.work.testing)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.robolectric)

    // Screenshot Testing (Roborazzi)
    testImplementation(libs.roborazzi.core)
    testImplementation(libs.roborazzi.compose)
    testImplementation(libs.roborazzi.rule)
    testImplementation(libs.roborazzi.scanner)
    testImplementation(libs.roborazzi.composable.scanner)

    // Instrumented Tests
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // Roborazzi also needs these in unit tests for Preview scanning
    testImplementation(libs.androidx.ui.test.junit4)

    // Debugging Tools
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

/**
 * The below section goal is to config the Jacoco test coverage plugin.
 */
val fileFilter = mutableSetOf(
    "**/R.class",
    "**/R$*.class",
    "**/BuildConfig.*",
    "**/Manifest*.*",
    "**/*Test*.*",
    "android/**/*.*",
    "**/*$[0-9]*.*",
    "**/*_Factory*.*",
    "**/*_MembersInjector*.*",
    "**/*_Component*.*",
    "**/*_Module*.*",
    "**/*Hilt*.*",
    "hilt_aggregated_deps/**",
    "**/*Screen*.*",
    "**/*Preview*.*"
)

tasks.withType<Test> {
    configure<JacocoTaskExtension> {
        isIncludeNoLocationClasses = true
        excludes = listOf("jdk.internal.*")
    }
}

tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn("testPreDebugUnitTest")
    group = "Reporting"
    description = "Generate Jacoco coverage reports for the preDebug build."

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    val debugTree = fileTree("${layout.buildDirectory.get()}/tmp/kotlin-classes/preDebug") {
        exclude(fileFilter)
    }
    
    val mainSrc = "${project.projectDir}/src/main/java"

    sourceDirectories.setFrom(files(mainSrc))
    classDirectories.setFrom(files(debugTree))
    executionData.setFrom(fileTree(layout.buildDirectory.get()) {
        include("jacoco/testPreDebugUnitTest.exec")
    })
}
