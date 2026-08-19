plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.google.services)
}

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
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
    jvmToolchain(11) // Set your desired JVM target version here
}

dependencies {
    implementation((project(":usecases")))
    implementation((project(":data")))
    implementation((project(":domain")))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    
    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
    
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    
    // Navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
    
    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.hilt.work)
    ksp(libs.androidx.hilt.compiler)

    // WorkManager
    implementation(libs.androidx.work)
    
    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)
    
    // Arrow
    implementation(libs.bundles.arrow)
    
    // Room (the below order is mandatory)
    implementation(libs.bundles.room)
    ksp(libs.androidx.room.compiler)
    
    // Location
    implementation(libs.google.accompanist.permissions)
    implementation(libs.google.play.services.location)
    
    // Retrofit
    implementation(libs.bundles.retrofit)
    
    // Coil
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    
    // Constraint layout
    implementation(libs.androidx.constraintlayout.compose)
}