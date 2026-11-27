# MeteoMartoCompose Testing Strategy Guide

This document is the **Single Source of Truth (SSOT)** for the project's testing strategy, infrastructure, and execution standards, aligned with `.agents/skills/testing-setup/SKILL.md` (`v2.4.0`) and `.agents/skills/design-system-governance/SKILL.md` (§4).

## 1. Pragmatic Testing Stack

MeteoMartoCompose uses a lean, fast, three-tier verification strategy optimized for pure JVM execution, minimal maintenance overhead, and high signal:

| Tier | Scope | Tools | Runner | Purpose |
| :--- | :--- | :--- | :--- | :--- |
| **Pure JVM Unit Tests** | ViewModels, UseCases with orchestration/branching, Repositories, Mappers, Coordinators | JUnit 4, `kotlinx-coroutines-test`, Turbine, Manual Fakes, `MainDispatcherRule`, `SavedStateHandle(mapOf(...))` | Pure JVM (JUnit) | Verify state transitions, cold-restoration (`SavedStateHandle`), error mapping, business rules, and orchestration without Android or bytecode-mocking overhead. |
| **Design System Visual Regression** | `ui.designsystem.components` `@Preview` composables only | Roborazzi (`generateComposePreviewRobolectricTests` + `RoborazziPreviewTester` `0.01f`), Robolectric (internal renderer only) | JVM (Roborazzi) | Guard Design System tokens, all 5 KISS palettes (`DayBaseline`, `Night`, `Sunny`, `Rain`, `ExtremeAlert`), RTL (`ar`), 200% font scaling, and Phone/Tablet adaptive layouts (`60` golden PNGs). |
| **Black-Box E2E & Process Death Probes** | Complete user journeys, real `NavHost`, Hilt graph, Room, Activity recreation & LMK process death | `@android-cli` (`android run`, `android layout --diff`, `android screen capture`) + `adb` Probes 4A/4B | Live Emulator / Device | Validate Acceptance Scenarios plus Activity recreation (`always_finish_activities 1 -> 0`) and LMK process death (`am kill` + Recents restore with `PID_AFTER != PID_BEFORE`) without in-repo instrumented tests. |

### Explicitly Forbidden Patterns
- **Zero `MockK` / `Mockito`**: Use hand-written, deterministic in-memory `Fake*` implementations for all domain/data interfaces (`app/src/test/java/.../testutil/fakes/` and `usecases/src/test/java/.../usecases/fakes/`).
- **Zero Robolectric Functional / Unit Tests**: Never annotate `*ViewModelTest`, `*WorkerTest`, `*ScreenTest`, or `NavigationTest` with `@RunWith(RobolectricTestRunner::class)`. Robolectric is reserved exclusively as Roborazzi's internal renderer for Design System previews.
- **Zero Feature Screen Screenshot Tests**: Regular feature screens verify visual layout via stateless `@Preview` functions plus clean `./gradlew assemblePreDebug lintPreDebug`, not committed screen golden PNGs.
- **Zero In-Repo Instrumented Tests (`src/androidTest/`)**: Espresso, `createAndroidComposeRule`, UI Automator, and `androidTestImplementation` dependencies are retired in favor of `@android-cli` black-box E2E validation.
- **Zero Trivial Tests**: Never write tests for Kotlin `data class`es, `*PreviewData`, 1:1 pass-through UseCases, 1:1 mappers without branching, or simple collection wrappers.

## 2. Strict Test Budgets & Single-Layer Assertion Rule

- **Per Test Class Cap**: **1 to 3 cohesive `@Test` methods maximum** per test class. Group related branch assertions of a single behavior into 1–2 cohesive `@Test` methods.
- **Per Feature Slice Cap**: **0 to 6 `@Test` methods total** across all modules for a standard feature slice (write `0` tests when a layer has no conditional branching, calculation, error mapping, or state transitions).
- **Single-Layer Assertion Rule**: Test each business rule or edge case **once at the lowest layer that owns it**:
  - `:domain` — Pure mathematical calculations, domain rules, and boundary conditions.
  - `:data` — Serialization/deserialization, caching policies, and `Throwable`-to-`DomainError` mapping.
  - `:usecases` — Multi-repository orchestration and fallback logic.
  - `:app` — ViewModel passive initialization, state transitions (`Loading`, `Error`, `Success`), `SavedStateHandle(mapOf(...))` cold-restoration, complex UI mappers, and pure Kotlin worker coordinators.

## 3. Infrastructure & Environment

### 3.1 Critical Requirements
- **JDK 21**: Mandatory across `:domain`, `:usecases`, `:data`, and `:app` (`jvmToolchain(21)`) for Gradle 8.14.3 and Roborazzi/Robolectric on `compileSdk = 36`.
- **`MainDispatcherRule`**: Every presentation test using `viewModelScope` or `stateIn` MUST apply `@get:Rule val mainDispatcherRule = MainDispatcherRule()` (`app/src/test/java/com/martorell/albert/meteomartocompose/testutil/MainDispatcherRule.kt`), calling `Dispatchers.setMain(UnconfinedTestDispatcher())` in `starting()` and `Dispatchers.resetMain()` in `finished()`.
- **Roborazzi Test Isolation**: `app/src/test/resources/robolectric.properties` wires `application = com.martorell.albert.meteomartocompose.TestMeteoMartoApp` globally so Design System preview rendering never triggers Hilt or Firebase initialization.
- **Deterministic Coil Previews**: In Design System composables using `AsyncImage` (e.g., `MmWeatherInfo.kt`), disable crossfade animation in inspection mode (`.crossfade(enable = !LocalInspectionMode.current)`) and provide `LocalAsyncImagePreviewHandler provides previewAsyncImageCoil` inside `@Preview` functions so snapshots render identically on Windows and `ubuntu-latest` CI.
- **Pragmatic Multipreview Baseline (`MmPreview.kt` & `RoborazziPreviewTester.kt`)**:
  - `@MmPreview` covers `Light Mode` (`DayBaseline`), `Dark Mode` (`Night`), `RTL` (`ar`), and `Large Font` (`2.0f`) — all with `showBackground = true`.
  - `@MmDevicePreview` covers `Phone` (`411x891dp`) and `Tablet` (`1280x800dp@240dpi`).
  - `@MmDynamicPalettesPreview` (`MmDynamicPalettesShowcase`) captures `SunnyPalette`, `RainPalette`, and `ExtremeAlertPalette` in a single vertical PNG per themed component (`60` golden PNGs total).
  - `RoborazziPreviewTester` applies `CompareOptions(changeThreshold = 0.01f)` to prevent false cross-OS font anti-aliasing failures between Windows and `ubuntu-latest` CI.

### 3.2 Gradle Configuration (`app/build.gradle.kts`)
```kotlin
roborazzi {
    outputDir.set(file("src/test/snapshots"))
    generateComposePreviewRobolectricTests {
        enable = true
        packages = listOf("${android.namespace}.ui.designsystem.components")
        includePrivatePreviews = true
    }
}

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
> [!IMPORTANT]
> Never hardcode `roborazzi.test.record`, `roborazzi.test.compare`, or `roborazzi.test.verify` inside `testOptions.unitTests.all`. Always let the Roborazzi Gradle tasks (`recordRoborazziPreDebug` / `verifyRoborazziPreDebug`) control those flags.

### 3.3 Core Testing Dependencies (`gradle/libs.versions.toml`)
| Library | Version Catalog Alias | Scope | Purpose |
| :--- | :--- | :--- | :--- |
| **JUnit 4** | `libs.junit` | `testImplementation` | Pure JVM test framework. |
| **Coroutines Test** | `libs.kotlinx.coroutines.test` | `testImplementation` | `runTest`, `UnconfinedTestDispatcher`, `advanceUntilIdle`. |
| **Turbine** | `libs.turbine` | `testImplementation` | Deterministic Kotlin `Flow` / `Channel` assertions. |
| **Robolectric** | `libs.robolectric` | `testImplementation` | Internal renderer for Roborazzi Design System `@Preview` scanning only. |
| **Roborazzi + Scanner** | `libs.roborazzi.*` | `testImplementation` | Automated Design System Compose `@Preview` visual regression. |
| **Compose BOM + UI Test** | `platform(libs.androidx.compose.bom)`, `libs.androidx.ui.test.junit4` | `testImplementation` | Required by Roborazzi's Compose Preview test runner. |
| **Compose Test Manifest** | `libs.androidx.ui.test.manifest` | `debugImplementation` | Required by Roborazzi to host preview compositions. |

## 4. Verification Commands (`pre` Product Flavor)

Because `flavors.gradle` defines `pre` and `pro` product flavors, always target the `preDebug` variant:

```bash
# Canonical CI & local quality gate (unit tests + Android Lint)
./gradlew testPreDebugUnitTest lintPreDebug --parallel --build-cache --configuration-cache

# Pure JVM layer module tests
./gradlew :domain:test :usecases:test :data:test --no-daemon

# Full APK & Hilt DI graph verification
./gradlew assemblePreDebug --no-daemon

# Design System visual regression verification (60 golden PNGs)
./gradlew :app:verifyRoborazziPreDebug --no-daemon

# Regenerate Design System golden PNGs after intentional Design Token or Mm* changes
./gradlew :app:recordRoborazziPreDebug --no-daemon

# Optional Jacoco coverage report (output: app/build/reports/jacoco/jacocoTestReport/html/index.html)
./gradlew :app:jacocoTestReport --no-daemon
```

### Snapshot Regeneration Triggers (`design-system-governance` §4)
Run `./gradlew :app:recordRoborazziPreDebug --no-daemon` followed immediately by `./gradlew :app:verifyRoborazziPreDebug --no-daemon` whenever:
1. A Design Token (Color, Spacing, Typography, Dimension) changes.
2. A Design System component (`Mm*`) layout or preview structure changes.
3. `.github/workflows/design-system-ci.yml` reports an intentional visual regression diff.
