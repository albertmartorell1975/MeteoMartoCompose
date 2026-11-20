# Validation Report: secrets-build-hygiene

> 🟢 **Validator**: `[VALIDATOR · workflow-validator]`
> **Verdict**: `accept`
> **Spec Contract**: `docs/features/secrets-build-hygiene/spec.md`
> **Iteration**: 1

---

## 1. 4-Level Validation Matrix

| Validation Level | Status (`PASS` / `FAIL` / `SKIPPED`) | Summary & Evidence |
| :--- | :--- | :--- |
| **1. Spec Contract & Acceptance Scenarios** | `PASS` | Audited `git diff 1635151..HEAD` across all 12 changed files against `docs/features/secrets-build-hygiene/spec.md`. `flavors.gradle` removes `API_KEY_VALUE` from both `pre` and `pro` (`0` matches in `git grep -n "API_KEY_VALUE" flavors.gradle`) while preserving `APP_ID_PARAM`, `API_WB_BASE`, `OPEN_WEATHER_ICON_URL`, and `app_name`. `local.defaults.properties` defines `API_KEY_VALUE=DEFAULT_OPENWEATHER_API_KEY`. `secrets-gradle-plugin` `2.0.1` is declared in `gradle/libs.versions.toml`, registered in root `build.gradle.kts`, and configured declaratively in `app/build.gradle.kts` (`propertiesFileName = "local.properties"`, `defaultPropertiesFileName = "local.defaults.properties"`, `ignoreList.add("sdk.*")`) with zero imperative `Properties`/`inputStream` I/O. `:domain`, `:usecases`, and `:data` align on `JavaVersion.VERSION_21`, `jvmToolchain(21)`, and `JvmTarget.JVM_21`. `.github/workflows/design-system-ci.yml` (`push` and `pull_request` paths) and `app/build.gradle.kts` Dokka `isDesignSystemMode` regex target `ui/designsystem` (`com.martorell.albert.meteomartocompose.ui.designsystem.*`). Approved scope amendment in `app/build.gradle.kts` restores `androidTestImplementation(platform(libs.androidx.compose.bom))` / `testImplementation(platform(libs.androidx.compose.bom))` and removes hardcoded `roborazzi.test.*` flags from `testOptions.unitTests.all`. `UI Involved: no` (zero UI/asset edits). |
| **2. Security & Scope Discipline** | `PASS` | `WIP=1` strictly honored (zero out-of-scope `.kt`, UI, asset, or `:data` module-type edits). `local.properties` is ignored (`.gitignore:15`) and untracked; `local.defaults.properties` contains only non-secret placeholder `API_KEY_VALUE=DEFAULT_OPENWEATHER_API_KEY`; `app/google-services.json` is not in the diff. Generated `BuildConfig.java` (`preDebug` and `proDebug`) exposes `API_KEY_VALUE = "DEFAULT_OPENWEATHER_API_KEY"` and does not leak `sdk.dir`. `AndroidManifest.xml` untouched and clean (`MainActivity` `exported="true"` with `MAIN`/`LAUNCHER` and `VIEW` deep link; `InitializationProvider` `exported="false"`). All `dependency-manager` rules (1, 2, 7, 10, 12) satisfied. |
| **3. Compiler Evidence & Test Quality** | `PASS` | Independently executed on JDK `21.0.11` (`C:\Users\JoanGN\.jdks\jbr-21.0.11`) in `.worktrees/val-secrets-build-hygiene`: `./gradlew assemblePreDebug` (`BUILD SUCCESSFUL in 1m 6s`, 48 tasks), `./gradlew testPreDebugUnitTest lintPreDebug --parallel --build-cache --configuration-cache` (`BUILD SUCCESSFUL in 15s`, configuration cache stored; second run `BUILD SUCCESSFUL in 1s`, `Configuration cache entry reused`; `lint-results-preDebug.html` shows 0 errors), `./gradlew :app:testPreDebugUnitTest --rerun-tasks --no-daemon` (`BUILD SUCCESSFUL in 2m`, `120/120` tests passed, `0` failed, `0` skipped, clean `git status`), `./gradlew :domain:test :usecases:test :data:test --rerun-tasks --no-daemon` (`BUILD SUCCESSFUL in 23s`, `:usecases:test` `6/6` passed), `./gradlew :app:dokkaGenerateHtml -PgenerateDocs -PdesignSystemDocs --no-daemon` (`BUILD SUCCESSFUL in 1m 18s`, generated `ui.designsystem.components` and `ui.designsystem.foundation` only), and `./gradlew :app:generateProDebugBuildConfig --no-daemon` (`BUILD SUCCESSFUL in 13s`). `0` new `@Test` methods added per spec (`testing-setup` compliant). |
| **4. Runtime / Emulator Flow (`@android-cli`)** | `PASS` | Verified pre-installed `com.martorell.albert.meteomartocompose` on active emulator `emulator-5554` (`adb -s emulator-5554 shell am start -W -n com.martorell.albert.meteomartocompose/.MainActivity`): cold launch succeeded (`Status: ok`, `TotalTime: 2911ms`, running PID `24239`). `uiautomator dump` confirmed Compose UI hierarchy rendered cleanly (`City`, `Favorites` navigation and graceful error state `"Failed to find out the city forecast"` under placeholder `DEFAULT_OPENWEATHER_API_KEY`). `logcat -d -s AndroidRuntime:E FATAL:E` showed zero fatal exceptions or `BuildConfig` crashes. |

---

## 2. Findings

No blocking or non-blocking findings identified.

*(Non-blocking note on `dependency-manager` Rule 6: `androidTestImplementation(platform(libs.androidx.compose.bom))` and `testImplementation(platform(libs.androidx.compose.bom))` in `app/build.gradle.kts` are explicitly required by the approved `spec.md` amendment because `libs.androidx.ui.test.junit4` has no version in `libs.versions.toml` and `:app:generatePreDebugAndroidTestLintModel` failed without the BOM platform on `preDebugAndroidTestRuntimeClasspath`.)*

---

## 3. Security Assessment Summary
* **Status**: `No issues found`
* **Details**: Hardcoded OpenWeather API keys were removed from `flavors.gradle` (`pre` and `pro` flavors), replaced by `secrets-gradle-plugin:2.0.1` with committed placeholder `local.defaults.properties` (`API_KEY_VALUE=DEFAULT_OPENWEATHER_API_KEY`). Local `local.properties` (containing only `sdk.dir`) remains git-ignored, `ignoreList.add("sdk.*")` prevents `sdk.dir` from leaking into generated `BuildConfig.java`, no `AndroidManifest.xml` or Intent handling changes were introduced, and `APIKeyInterceptor` consumes `BuildConfig.API_KEY_VALUE` safely at runtime without host-env/file reads.

---

## 3.1 Per-Test Value & Falsifiability Audit

N/A — 0 unit tests added/modified (pure build/CI infrastructure slice per `docs/features/secrets-build-hygiene/spec.md` and `testing-setup`).

---

## 3.5 Adversarial Attempts Log

1. **JDK 25 vs JDK 21 Toolchain Break Check**: Executed `./gradlew --no-daemon -q javaToolchains` with default `JAVA_HOME` (JDK `25.0.4`) → failed immediately (`25.0.4`), confirming `CONSTRAINTS.md` JDK 21 mandate. Re-ran full verification suite with JDK `21.0.11` (`C:\Users\JoanGN\.jdks\jbr-21.0.11`) → all tasks succeeded cleanly.
2. **`BuildConfig.java` `sdk.dir` Leakage & Flavor Parity Check (`preDebug` + `proDebug`)**: Inspected generated `app/build/generated/source/buildConfig/pre/debug/com/martorell/albert/meteomartocompose/BuildConfig.java` and ran `./gradlew :app:generateProDebugBuildConfig --no-daemon` to inspect `pro/debug/.../BuildConfig.java`. Both flavors generated `public static final String API_KEY_VALUE = "DEFAULT_OPENWEATHER_API_KEY";` alongside `API_WB_BASE`, `APP_ID_PARAM`, and `OPEN_WEATHER_ICON_URL`, with zero `sdk.dir` fields emitted.
3. **Imperative `Properties` I/O & Leftover Secret Literal Check**: Ran `git grep -n "Properties\|inputStream" app/build.gradle.kts build.gradle.kts flavors.gradle` and `git grep -n "API_KEY_VALUE" flavors.gradle` → zero imperative `java.util.Properties` / `inputStream` loading and zero `API_KEY_VALUE` matches in `flavors.gradle`.
4. **Configuration-Cache Cold Store + Warm Reuse Check**: Executed `./gradlew testPreDebugUnitTest lintPreDebug --parallel --build-cache --configuration-cache` twice consecutively → first run printed `Configuration cache entry stored.`, second run printed `Configuration cache entry reused.` (`BUILD SUCCESSFUL in 1s`).
5. **Uncached Unit Test & Snapshot Mutation Check**: Ran `./gradlew :app:testPreDebugUnitTest --rerun-tasks --no-daemon` followed by `git status --short` → all `120/120` unit tests passed (`0` failures, `0` skipped) and `git status --short` remained completely empty (zero Roborazzi snapshot PNGs created or mutated).
6. **Dokka Package Filter Verification**: Ran `./gradlew :app:dokkaGenerateHtml -PgenerateDocs -PdesignSystemDocs --no-daemon` and inspected `app/build/dokka/design-system/-meteo-marto-compose -design -system -reference` → only `com.martorell.albert.meteomartocompose.ui.designsystem.components` and `com.martorell.albert.meteomartocompose.ui.designsystem.foundation` were emitted (all non-design-system packages suppressed).
7. **Live Emulator Cold-Boot & Placeholder API Key Crash Check**: Force-stopped and cold-launched `com.martorell.albert.meteomartocompose/.MainActivity` on `emulator-5554` using `DEFAULT_OPENWEATHER_API_KEY` → app remained alive (PID `24239`), rendered Compose `City`/`Favorites` shell and error fallback cleanly, with `0` `AndroidRuntime:E` or `FATAL:E` logcat entries.

---

## 4. Implementation Repair Brief

N/A — Feature accepted and ready for `workflow-documenter` / Stage 5 consolidation.
