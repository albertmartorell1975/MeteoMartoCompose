# MeteoMartoCompose Testing Strategy Guide

This document is the **Single Source of Truth (SSOT)** for the project's testing strategy, infrastructure, and execution standards.

## 1. Pragmatic Testing Stack

The project uses a dual-tiered testing approach designed for speed, reliability, and low maintenance.

| Type | Tools | Runner | Purpose |
| :--- | :--- | :--- | :--- |
| **Unit Testing** | JUnit 4, MockK, Turbine | JUnit | **Cap**: Lògica de negoci, ViewModels, UseCases i Repositories. |
| **Visual Regression** | Roborazzi | Robolectric | **Cos**: Estabilitat visual, disseny i estats de la UI (screenshots). |

> [!NOTE]
> **Simplification Mandate**: To avoid redundancy and fragility, we prioritize ViewModel Unit Tests over UI Behavior tests. If the logic is verified in the ViewModel, we rely on Screenshot tests to ensure the UI renders that state correctly.

## 2. Infrastructure & Environment

### 2.1 Critical Requirements
- **JDK 21**: Mandatory for targeting SDK 36+ (Android 16).
- **Render Mode**: Configured with `robolectric.pixelCopyRenderMode = hardware` for high-fidelity snapshots.
- **Private Previews**: Enabled to capture internal component variants without exposing them to the public API.

### 2.2 Test Isolation
To avoid production infrastructure leaks (like Firebase or Hilt production modules), all Robolectric tests MUST use the custom application class:
```kotlin
@Config(application = TestMeteoMartoApp::class)
```

### 2.3 Gradle Configuration Detail (`app/build.gradle.kts`)
```kotlin
// Roborazzi Plugin Configuration
roborazzi {
    outputDir.set(file("src/test/snapshots"))
    generateComposePreviewRobolectricTests {
        enable = true
        packages = listOf("${android.namespace}.ui.designsystem.components")
        includePrivatePreviews = true
    }
}

// Android Test Options for Fidelity
android {
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            all {
                it.systemProperties["robolectric.pixelCopyRenderMode"] = "hardware"
            }
        }
    }
}
```

## 3. Execution & Persistence

### 3.1 Visual Regression (Roborazzi)
We follow **ADR 09 (Pragmatic Snapshots)**: focusing on dimensions that impact UX (Theme, RTL, Font Scale).

- **Path**: Golden Images are stored in `app/src/test/snapshots/`.
- **Policy**: All snapshots **MUST** be committed to the repository as the baseline for CI/CD.
- **Commands**:
    - `verifyRoborazziDebug`: Compare against Golden Images.
    - `recordRoborazziDebug`: Update/Create Golden Images.

### 3.2 Unit Testing
- **Execution**: Run via `./gradlew :app:testDebugUnitTest`.
- **Pattern**: Given-When-Then. Use `runTest` for coroutines and `Turbine` for Flow verification.

## 4. Best Practices for Developers & Agents

1. **Dual Verification**: Every feature must have its logic verified in a ViewModel Unit Test and its visual states captured in a Screenshot Test.
2. **Stateless UI First**: Always target the `*Content` (stateless) composables for screenshot tests.
3. **Atomic Commits**: Feature changes and their corresponding tests (Unit + Snapshots) must be included in the same branch.
4. **Fakes over Mocks**: Prefer **Fakes** for complex business components (e.g., `AuthRepository`) to improve test stability.
