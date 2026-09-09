# MeteoMartoCompose Testing Strategy Guide

This document is the **Single Source of Truth (SSOT)** for the project's testing strategy, infrastructure, and execution standards.

## 1. Pragmatic Testing Stack

The project uses a tiered testing approach designed for speed, reliability, and measurable quality.

| Type | Tools | Runner | Purpose |
| :--- | :--- | :--- | :--- |
| **Unit Testing** | JUnit 4, MockK, Turbine | JUnit | **The Brain**: Business logic, ViewModels, UseCases, and Repositories. |
| **Visual Regression** | Roborazzi | Robolectric | **The Body**: Visual stability, design, and UI states (screenshots). |
| **Code Coverage** | Jacoco | Gradle Plugin | **The Metric**: Measures the % of logic actually verified by tests. |

> [!NOTE]
> **Simplification Mandate**: To avoid redundancy and fragility, we prioritize ViewModel Unit Tests over UI Behavior tests. If the logic is verified in the ViewModel, we rely on Screenshot tests to ensure the UI correctly renders that state.

## 2. Infrastructure & Environment

### 2.1 Critical Requirements
- **JDK 21**: Mandatory for targeting SDK 36+ (Android 16).
- **Render Mode**: Configured with `hardware` for high-fidelity snapshots.
- **Private Previews**: Enabled to capture internal component variants without leaking them to the public API.

### 2.2 Test Isolation
To avoid production infrastructure leaks (like Firebase or production Hilt modules), all Robolectric tests **MUST** use the custom application class:
```kotlin
@Config(application = TestMeteoMartoApp::class)
```

### 2.3 Gradle Configuration Detail (`app/build.gradle.kts`)
The following configuration ensures that Roborazzi and Jacoco work seamlessly with the design system and flavor-based environments:

```kotlin
// Roborazzi Plugin Configuration
roborazzi {
    outputDir.set(file("src/test/snapshots"))
    generateComposePreviewRobolectricTests {
        enable = true
        // Scans exclusively the design system package for automated snapshots
        packages = listOf("${android.namespace}.ui.designsystem.components")
        // Also captures private @Preview functions
        includePrivatePreviews = true
    }
}

// Android Test Options for fidelity and Jacoco integration
android {
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            all {
                // Required for Roborazzi 4.12.2+ to avoid broken images
                it.systemProperties["robolectric.pixelCopyRenderMode"] = "hardware"
            }
        }
    }
}
```

### 2.4 Core Testing Dependencies (`libs.versions.toml`)
The project uses the following dependencies for testing (declared in the version catalog):

| Library | Version Catalog Alias | Description |
| :--- | :--- | :--- |
| **JUnit 4** | `libs.junit` | Core unit testing framework. |
| **MockK** | `libs.mockk` | Mocking framework for Kotlin. |
| **Turbine** | `libs.turbine` | Small testing library for Kotlin Flows. |
| **Coroutines Test**| `libs.kotlinx.coroutines.test` | Utilities for testing asynchronous code. |
| **Robolectric** | `libs.robolectric` | Android simulation on the JVM. |
| **Roborazzi** | `libs.roborazzi.core` | Visual regression tool (screenshots). |
| **Compose UI Test**| `libs.androidx.ui.test.junit4` | Compose testing integration for JUnit. |

## 3. Execution & Commands

### 3.1 Unit Testing & Coverage
To run unit tests and generate the Jacoco coverage report:
```bash
# General coverage report (runs all unit tests)
./gradlew :app:jacocoTestReport

# Run unit tests only (no coverage report)
./gradlew :app:testPreDebugUnitTest
```

### 3.2 Visual Regression (Roborazzi)
We follow **ADR 09 (Pragmatic Snapshots)**: focusing on dimensions that impact UX (Theme, RTL, Font Scale).

#### Manual Screenshot Tests
To execute specific screenshot tests manually coded in `src/test`:
```bash
# Verify against Golden Images
./gradlew verifyRoborazziPreDebug

# Record/Update Golden Images
./gradlew recordRoborazziPreDebug
```

#### Automated Preview Scanner
To run the automated scanner that captures all `@Preview` functions in the configured packages:
```bash
# Verify all previews
./gradlew verifyRoborazziDebug

# Record all previews
./gradlew recordRoborazziDebug
```

## 4. Visualizing & Interpreting Coverage (Jacoco)

### 4.1 Report Location
The interactive HTML report is generated at:
`app/build/reports/jacoco/jacocoTestReport/html/index.html`

### 4.2 How to Read the Report
- **Green**: Code is fully covered.
- **Red**: Code was never executed during tests.
- **Yellow (Diamond)**: Partial branch coverage (e.g., only one path of an `if`/`when` was tested).

**Goal**: Maintain instruction coverage >80% and branch coverage >50% for core business logic.

## 5. Best Practices for Developers & Agents

1. **Dual Verification**: Every new feature must include its corresponding Unit Test and Screenshot Test.
2. **Stateless UI First**: Always target the `*Content` (stateless) composables for snapshots.
3. **Atomic Commits**: Always include tests in the same branch as the code change.
4. **Fakes over Mocks**: Prioritize the use of **Fakes** for complex business components to improve stability.
