# PR Review Feedback Log — `pragmatic-testing-cleanup` (PR #5)

> 🟣 **Maintained by**: `[ORCHESTRATOR · workflow-orchestrator]`
> **Pull Request**: `#5` (`https://github.com/albertmartorell1975/MeteoMartoCompose/pull/5`)
> **Base Branch**: `feature/MM-02-design-system`
> **Head Branch**: `feature/pragmatic-testing-cleanup`

---

## PR Review Round #1

- **Submitted at**: `2026-11-21`
- **Reviewer**: `@albertmartorell1975` (PR author self-review via `COMMENTED` + chat confirmation)
- **Contract Rule**: No changes to `docs/features/pragmatic-testing-cleanup/spec.md`; `workflow-planner` is NOT re-run. All other PR review comments (`TemperatureCheckCoordinator.kt` creation & `CancellationException` rethrow, `TemperatureCheckWorker.kt`, `MmPreview.kt`, `FavoritesDetailViewModel.kt` `CITY_NAME_ARG`) were clarified and explicitly approved by the developer to remain as implemented.

### Actionable Findings to Fix in Round #1

#### Finding 1 — Restore `"TempAlert"` Logging via `AppLogger` Abstraction
- **Path**: `app/src/main/java/com/martorell/albert/meteomartocompose/ui/screens/city/CityWeatherViewModel.kt`
- **Line**: `190` (`LEFT`, comment `#4652072658`)
- **Diff Hunk**:
  ```kotlin
  -                    Log.d(
  -                        "TempAlert",
  -                        "Current Temperature: ${result.currentTemperature}°C  High Alert Threshold: ${result.threshold}°C",
  -                    )
  ```
- **Reviewer Instruction**:
  - *"No entenc per què hem eliminat aquest Log. Ens va bé per saber la temperatura actual i quina és la que dispara l'avís de temperatura extrema."*
  - Confirmed in chat: *"Tenia al cap crear una interfície AppLogger i una implementació per tal d'abstraure la seva lògica de les classes que l'utilitzin. La resta em sembla bé. Podem incloure aquesta implementació a la feature?"*
- **Required Implementation**:
  1. Create interface `AppLogger` (`fun d(tag: String, message: String)` and `fun e(tag: String, message: String, throwable: Throwable? = null)`) and Android implementation `AndroidAppLogger @Inject constructor() : AppLogger` (delegating to `android.util.Log.d` / `android.util.Log.e`) following the existing `AppLifecycleObserver` / `AppLifecycleObserverImpl` pattern under `app/src/main/java/com/martorell/albert/meteomartocompose/utils/` (no KDoc comments — `workflow-documenter` will add English KDoc in Stage 4).
  2. Bind `AndroidAppLogger` to `AppLogger` with `@Binds @Singleton` in `app/src/main/java/com/martorell/albert/meteomartocompose/di/AppBindingModule.kt`.
  3. Inject `private val appLogger: AppLogger` into `CityWeatherViewModel` and restore the `"TempAlert"` log inside `startMonitoring()`:
     ```kotlin
     appLogger.d(
         "TempAlert",
         "Current Temperature: ${result.currentTemperature}°C  High Alert Threshold: ${result.threshold}°C",
     )
     ```
  4. Add `FakeAppLogger` (recording debug/error entries in memory) in `app/src/test/java/com/martorell/albert/meteomartocompose/testutil/fakes/FakeCityWeatherDependencies.kt` and pass it into `CityWeatherViewModelTest` (optionally assert the `"TempAlert"` log entry inside the existing `startMonitoring` `@Test` without exceeding the strict `3` `@Test` cap and keeping `0` MockK / `0` Robolectric).

#### Finding 2 — Cross-OS Roborazzi Anti-Aliasing Tolerance for `Design System CI` (`ubuntu-latest` vs Windows)
- **Path**: `app/build.gradle.kts` & `app/src/test/` (Design System CI run `#44825870499`)
- **Context**: `Quality Gate` and `Dokka` passed on PR #5, while `Visual Regression (Roborazzi)` on `ubuntu-26.04` failed on `29/36` Windows-recorded snapshots solely due to `< 0.1%` (~20 single-pixel) font sub-pixel anti-aliasing differences between Windows Skia and Linux FreeType/Skia (verified visually via `roborazzi-comparison-results` artifact).
- **Required Implementation**:
  1. Configure a lightweight custom `AndroidComposePreviewTester` subclass in `app/src/test/java/com/martorell/albert/meteomartocompose/ui/designsystem/RoborazziPreviewTester.kt` (wired via `testerQualifiedClassName` in `app/build.gradle.kts` `roborazzi.generateComposePreviewRobolectricTests {}`) that overrides `options()` to apply a tight cross-OS anti-aliasing tolerance (`RoborazziOptions.CompareOptions(changeThreshold = 0.01f)`, 1% max pixel change) while preserving exact `36` Design System snapshots and catching any real layout, color, text, or component regressions.
  2. Verify locally with `./gradlew :app:verifyRoborazziPreDebug --no-daemon` (`36/36` pass, `0` snapshot mutations) and full canonical gate `./gradlew testPreDebugUnitTest lintPreDebug --parallel --build-cache --configuration-cache` + `./gradlew assemblePreDebug --no-daemon`.
