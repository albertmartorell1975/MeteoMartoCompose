# Validation Report: domain-error-contract

> 🟢 **Validator**: `[VALIDATOR · workflow-validator]`
> **Verdict**: `accept`
> **Spec Contract**: `docs/features/domain-error-contract/spec.md`
> **Iteration**: 1

---

## 1. 4-Level Validation Matrix

| Validation Level | Status (`PASS` / `FAIL` / `SKIPPED`) | Summary & Evidence |
| :--- | :--- | :--- |
| **1. Spec Contract & Acceptance Scenarios** | `PASS` | Audited `git diff 1eea1a7..HEAD` (`11a8094`) against all 4 `Given/When/Then` scenarios in `spec.md`. Pure `:domain` `DomainError` + `Result<out T, out E : DomainError>` have `0` imports and zero raw `Throwable`/exception string leakage; `:data` `ErrorMapper`/`ExceptionMapper` (`toDomainError`, `safeCall`, `safeSyncCall`) preserve `CancellationException`, guard cause-chain cycles via `IdentityHashMap`, and map `401`/`403` vs other HTTP codes; `:app` `AndroidErrorMapper` (`@Binds @Singleton`), `AuthErrorMapper` (`FirebaseAuthWeakPasswordException` matched before `FirebaseAuthInvalidCredentialsException`), `AccountServiceImpl`, `LocationServerDataSourceImpl`, `CityRoomDataSource` (`Local.NotFound` on `null`), `FirebaseRemoteConfigDataSourceImpl`, `UiText`, exhaustive `DomainError.toUiText()`, `strings.xml`, 5 ViewModels, and 5 Screens all satisfy the spec contract. |
| **2. Security & Scope Discipline** | `PASS` | `0` matches for `CustomError`, `CustomErrorFlow`, `ResultResponse`, `customTryCatch`, `customFlowTryCatch`, `arrow.`, `io.mockk` across `app/`, `data/`, `domain/`, `usecases/`; `0` `android.*`/`androidx.*`/`retrofit`/`room`/`firebase` imports in `:domain`; `0` `:data` error imports or `R.string` in ViewModels; `0` `runCatching` across all modules; `0` `RobolectricTestRunner` in `app/src/test/`; `0` changes to `AndroidManifest.xml`, `framework/db/`, `ui/designsystem/`, or the `36` PNGs in `app/src/test/snapshots/`; `0` KDoc comments on the 8 KDoc-deferred production files; all 13 Architecture & SOLID triggers pass; `isReturnDefaultValues = true` in `app/build.gradle.kts` audited with zero assertion masking risk. |
| **3. Compiler Evidence & Test Quality** | `PASS` | Independently re-executed all 4 Gradle gates on JDK `21.0.11` in `.worktrees/val-domain-error-contract`: `assemblePreDebug --no-daemon` (`BUILD SUCCESSFUL in 1m 29s`, `0` compiler warnings on changed/new files); `testPreDebugUnitTest lintPreDebug --parallel --build-cache --configuration-cache` (`BUILD SUCCESSFUL in 1m 9s`, configuration cache stored, `0` lint warnings on changed/new `.kt` files); `:domain:test :usecases:test :data:test --rerun-tasks --no-daemon` (`BUILD SUCCESSFUL in 27s`, `:data:test` `3/3`, `:usecases:test` `3/3`, `:domain:test` `NO-SOURCE`); `:app:verifyRoborazziPreDebug --no-daemon` (`BUILD SUCCESSFUL in 49s`, freshly executed `:app:testPreDebugUnitTest` `61/61` = `25` unit tests across `10` classes `<= 3`/class + `36` Roborazzi previews, `results-summary.json`: `total=36, recorded=0, added=0, changed=0, unchanged=36`). All `6` new `@Test` methods pass Per-Test Value & Falsifiability Audit as `KEEP`. `git status --short` clean. |
| **4. Runtime / Emulator Flow (`@android-cli` / `adb`)** | `PASS` | Deployed freshly built `val-domain-error-contract` `app-pre-debug.apk` onto `emulator-5554`. Verified: (1) Online cold launch (`Status: ok`, `TotalTime: 1435ms`) loaded live `"Mountain View"` forecast cleanly; (2) Offline cold launch under airplane mode (`Status: ok`, `TotalTime: 1369ms`) mapped `IOException` -> `DomainError.Network.NoInternet` -> localized `R.string.error_connectivity` (`"Connectivity error"`) with zero raw exception text; (3) Disabling airplane mode and tapping `"Update forecast"` transitioned cleanly `Error ("Connectivity error") -> Retry -> Success ("Mountain View")`; (4) `0` fatal exceptions in `adb logcat -d -s AndroidRuntime:E FATAL:E`. |

---

## 2. Findings

No blocking (`Critical`, `High`, or `Medium`) findings identified. Three non-blocking `Low` notes recorded for traceability:

### Finding 1 (Low — Non-Blocking): Newly Unused String Resource `R.string.city_forecast_error`
* **Severity**: `Low`
* **File / Evidence**: `app/src/main/res/values/strings.xml#L41` (`lint-results-preDebug.html`: `The resource R.string.city_forecast_error appears to be unused`)
* **Suggested follow-up**: Remove `city_forecast_error` when rebuilding the city forecast error state in `city-forecast-dynamic-screen`.

### Finding 2 (Low — Non-Blocking): Audited `isReturnDefaultValues = true` Addition in `app/build.gradle.kts`
* **Severity**: `Low`
* **File / Evidence**: `app/build.gradle.kts#L35` (`testOptions.unitTests.isReturnDefaultValues = true`)
* **Why it matters**: Required on the pure JVM to instantiate `FirebaseAuth*Exception` (`Preconditions.checkNotEmpty` -> `android.text.TextUtils.isEmpty`) and `android.database.SQLException` without introducing MockK or Robolectric; audited with zero assertion masking across all `:app` unit tests.

### Finding 3 (Low — Pre-Existing, Out of Scope for `WIP=1`): `CityWeatherViewModel` `loadMutex` Held During Active Room Flow Collection
* **Severity**: `Low`
* **File / Evidence**: `app/src/main/java/com/martorell/albert/meteomartocompose/ui/screens/city/CityWeatherViewModel.kt#L91-L103`, `#L318-L337` (verified identical in base commit `1eea1a7`)
* **Suggested follow-up**: Address in `city-forecast-dynamic-screen`.

---

## 3. Security Assessment Summary
* **Status**: `No issues found`
* **Details**: `AndroidManifest.xml` untouched; zero hardcoded secrets; `DomainError` carries zero `Throwable` references and zero raw exception message strings; all user-facing error text resolves deterministically via `UiText.StringResource`; unused Arrow removed and `kotlinx-coroutines-core:1.11.0` declared explicitly.

---

## 3.1 Per-Test Value & Falsifiability Audit

All `6` newly created `@Test` methods across the `3` new test files (`ExceptionMapperTest` [3], `AndroidAndAuthErrorMapperTest` [2], `ErrorUiMappersTest` [1]) passed Per-Test Value & Falsifiability Audit as `KEEP`. Existing `25` unit tests across `9` classes were mechanically migrated with exact `@Test` counts preserved.

---

## 3.2 Adversarial Attempts Log

1. **Subtype-Ordering Traps**: Verified narrower subtypes (`SocketTimeoutException`, `FirebaseAuthWeakPasswordException`, `FirebaseRemoteConfigServerException`, `ResolvableApiException`) are matched strictly before supertypes, with `IdentityHashMap` cycle guards in all 3 mappers.
2. **`isReturnDefaultValues = true` Masking Audit**: Confirmed zero assertion masking across all `:app` unit tests (`FirebaseAuthException.errorCode` and `FirebaseRemoteConfigServerException.httpStatusCode` return real bytecode values).
3. **Structured Concurrency Cancellation Audit**: Verified `0` `runCatching` calls remain and every `catch (e: Exception)` / cause walker rethrows `CancellationException` first.
4. **Repository-Wide Hygiene Sweep**: Confirmed `0` legacy error/Arrow/MockK/Robolectric-unit-test matches, `36` untouched Roborazzi PNGs, and `0` KDoc blocks on all 8 deferred production files.
5. **Lint Warning Sweep**: Confirmed `0` warnings on any modified/new `.kt` files (`R.string.city_forecast_error` unused recorded as Low note).
6. **Independent 4-Gate Gradle Execution**: Re-ran all 4 Gradle commands on JDK `21.0.11` (`61/61` `:app`, `3/3` `:data`, `3/3` `:usecases`, `36/36` Roborazzi `unchanged`).
7. **Live Emulator Error & Recovery Flow (`emulator-5554`)**: Verified online launch (`"Mountain View"`), offline airplane-mode cold launch (`"Connectivity error"` via `DomainError.Network.NoInternet` -> `UiText`), and reconnect retry (`Error -> Success`) with `0` fatal logcat exceptions.

---

## 4. Implementation Repair Brief

N/A — Feature accepted and ready for `workflow-documenter`.
