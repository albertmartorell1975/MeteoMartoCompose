# Validation Report: navigation-shell-unification

> 🟢 **Validator**: `[VALIDATOR · workflow-validator]`
> **Verdict**: `accept`
> **Spec Contract**: `docs/features/navigation-shell-unification/spec.md`
> **Iteration**: 2

---

## 1. 4-Level Validation Matrix

| Validation Level | Status (`PASS` / `FAIL` / `SKIPPED`) | Summary & Evidence |
| :--- | :--- | :--- |
| **1. Spec Contract & Acceptance Scenarios** | `PASS` | Audited repair delta `b4051ac..d5697fc` (`-4` lines in `AppState.kt`, zero unintended code changes) and cumulative diff `f98d403..d5697fc` (16 files: 5 created, 5 deleted, 4 modified, plus `PROGRESS.md` & `feature_list.json`). All 4 `Given / When / Then` Acceptance Scenarios are implemented with real logic and verified end-to-end: unified `DashboardScreens.CityForecast(cityName: String? = null)` preserving `"cityName"`, single `composable<DashboardScreens.Favorites>` registration in `HomeNavGraph.kt`, `collectAsStateWithLifecycle()` in `Navigation.kt`, stateless decoupled `TopAppBarCustom`, `AppState`/`AppShellState`/`TopLevelRoute` in `ui.navigation`, preserved `FabState` delegation, `LaunchedEffect(currentBackStackEntry?.id)` scroll reset, and `0` KDoc comments on the 5 KDoc-deferred files. |
| **2. Security & Scope Discipline** | `PASS` | Strict `WIP=1` atomic scope verified: `0` edits to `AndroidManifest.xml`, `MainActivity.kt`, `ui/FabState.kt`, `ui/navigation/shared/FabCustom.kt`, `ui/screens/**`, ViewModels, `ui/designsystem/**`, `:domain`, `:data`, `:usecases`, Gradle scripts, durable docs, or `app/src/test/snapshots/**` (`60/60` untouched). Deep-link `meteomarto://alert/high-temperature/{temperature}` preserved cleanly with typed `Double` argument and `DialogProperties`; `0` secrets, `0` Intent redirection risks, `0` `try-catch` control flow. |
| **3. Compiler Evidence, Lint & Architecture** | `PASS` | Finding 1 (Trigger #15 dead `@Composable fun isTopLevelRouteSelected(route: Any)` overload) removed cleanly in `d5697fc` — all **18 Mandatory REVISE Triggers** (incl. Trigger #15 & Trigger #18) now pass. Independent JDK `21.0.11` verification passed all 4 gates: `assemblePreDebug --no-daemon` (`1m 46s`, `0` compiler warnings); `testPreDebugUnitTest lintPreDebug --parallel --build-cache --configuration-cache` (`:app:testPreDebugUnitTest` `95/95` = `35` pure JVM + `60` Roborazzi, `0` failed/skipped; `lint-results-preDebug.xml` `0` warnings on all 9 touched `.kt` files); `:domain:test :usecases:test :data:test --rerun-tasks --no-daemon` (`33s`, `1/1`, `3/3`, `3/3`); `:app:verifyRoborazziPreDebug --rerun-tasks --no-daemon` (`2m 15s`, `results-summary.json`: `60/60 unchanged`). `AppShellStateTest` (`2/2` `@Test`) audited as `KEEP` (`0` MockK, `0` Robolectric). |
| **4. Runtime, Activity Recreation & Process Death (`@android-cli`)** | `PASS` | `emulator-5554` recovered (`Service package: found`; physical device `adb-2C241JEHN09429...` strictly untouched). Hydrated `app-pre-debug.apk` installed cleanly (`Performing Streamed Install Success`). Executed live black-box journeys + **Probe 4A** (`always_finish_activities 1 -> 0`) + **Probe 4B** (`KEYCODE_HOME` -> `am kill` -> Recents restore with `PID_AFTER != PID_BEFORE`) across all 6 destinations (`CityForecast(null)`, `Favorites`, `CityForecast("Mountain View")`, `HighTemperatureAlert(38.5)`, `AuthScreens.Terms`, `AuthScreens.SignUp`) plus decoupled `TopAppBarCustom` Logout confirmation -> `AuthScreens.Login`. `0` crashes, `0` Binder errors, `always_finish_activities` verified reset to `0` (see §3.3). |

---

## 2. Findings

### 2.1 Blocking Findings (Critical / High / Medium)
No blocking findings identified. *(Iteration 1 Finding 1 — unused `@Composable fun isTopLevelRouteSelected(route: Any): Boolean` in `AppState.kt` under Trigger #15 — was verified resolved in commit `d5697fc`.)*

### 2.2 Non-Blocking Code Review Observations (Low / Spec-Smells / Cross-OS Risks)

* **Observation 1 (`Navigation.kt#L42-L43` & `AppState.kt#L59-L63`)**: `Navigation.kt` reads `val currentBackStackEntry = appState.currentBackStackEntry` on line 42 and then `val shellChrome = appState.shellChrome` on line 43, where `AppState.shellChrome` invokes `currentBackStackEntry` (`navController.currentBackStackEntryAsState().value`) a second time. Because `currentBackStackEntryFlow` is a `StateFlow`, both reads stay synchronized, though exposing `fun resolveChrome(entry: NavBackStackEntry?): AppShellChrome` (mirroring `isTopLevelRouteSelected(entry, route)`) in a future cleanup would avoid a duplicate `collectAsState()` subscription in `Navigation`.
* **Observation 2 (`AppState.kt#L54`)**: `AppState.windowSizeClass` is exposed as a public property per `compose-navigation-architecture` §1 and `spec.md`, and currently has no external reader because `MmNavigation` delegates compact/expanded switching internally to `NavigationSuiteScaffold`. Downstream adaptive screen overhauls (`auth-*`, `city-forecast-*`, `favorites-list-overhaul`) can consume `appState.windowSizeClass` directly where responsive pane/padding layouts are introduced.
* **Observation 3 (`Navigation.kt#L74-L139` & untouched `MmNavigation.kt#L70-L166`)**: When `showBottomNavigation` toggles between `true` and `false` (e.g., `Favorites` -> `CityForecast("Mountain View")` or `CityForecast(null)` -> `HighTemperatureAlert`), `MmNavigation` switches between its `if (items.isEmpty())` `Scaffold` branch and its `else` `NavigationSuiteScaffold` branch. Live Level 4 probes confirmed that because `appState` (`navController`) is remembered *above* `MmNavigation` (`Navigation.kt#L41`), backstack state and recreation/process-death restoration work without regression; a future Design System refinement could unify `MmNavigation` via `NavigationSuiteType.None` to keep `NavHost` in a single composition slot.
* **Observation 4 (`FavoritesDetailViewModel.kt#L26`, untouched)**: Legacy KDoc still contains the phrase `(refer to favoriteSubGraph for more details)`. Leaving `FavoritesDetailViewModel.kt` untouched is correct per `spec.md` Non-Goals; it will be retired in `city-forecast-state-and-palette`.

---

## 3. Security Assessment Summary
* **Status**: `No issues found`
* **Details**:
  * **Android Component & Intent Security (`android-intent-security`)**: `AndroidManifest.xml` and `MainActivity.kt` remain untouched; `MainActivity`'s `meteomarto://alert` deep link maps strictly to `dialog<DashboardScreens.HighTemperatureAlert>(deepLinks = listOf(navDeepLink<DashboardScreens.HighTemperatureAlert>(basePath = AppConstants.DEEP_LINK_BASE)))` with a primitive `Double` argument (`temperature`), verified live on `emulator-5554` with `meteomarto://alert/high-temperature/38.5`, zero nested `Intent` extraction, and zero Intent redirection risk.
  * **Secrets & Configuration**: Zero secrets, API keys, tokens, or Gradle/manifest changes in the diff.
  * **Boundary Validation**: `HomeNavGraph.kt#L59` guards `city?.name?.takeIf { it.isNotBlank() }` before calling `appState::navigateToCityForecast`, and `DashboardScreens.CityForecast.fromCityName(...)` normalizes `null`/empty/whitespace inputs to `null` (Current Location mode) and trims valid city names. Zero `try-catch` control flow; proactive `hasRoute<T>()` checks used in both `AppShellState.classifyDestination` and `AppState.dashboardBackStackEntryOrNull`.

---

## 3.1 Per-Test Value & Falsifiability Audit

| `@Test` | Subject & Real Complexity | Production Line That Breaks If Inverted | Verdict (`KEEP` / `CONSOLIDATE` / `DELETE`) |
| :--- | :--- | :--- | :--- |
| `AppShellStateTest.resolveChrome and tab selection map current forecast favorite detail favorites list and other routes` | Exhaustive 4-way `ShellDestination` chrome mapping (`when`) + compound top-level selection predicate (`route is DashboardScreens.CityForecast && !route.isFavoriteDetail`) preventing `FavoriteCityDetail` from highlighting the `City` tab or leaking bottom bar / logout / FAB. | `AppShellState.kt#L35-L64` (any chrome flag flip) and `AppShellState.kt#L71-L76` (removing `!route.isFavoriteDetail` or mis-mapping `FavoriteCityDetail`). | `KEEP` |
| `AppShellStateTest.cityForecast route contract normalizes cityName and preserves SavedStateHandle key` | Boundary input normalization (`null`/empty/whitespace -> `null`, trimmed non-blank -> favorite detail), `NAV_OPTIONS` uniqueness/order, and Kotlinx Serialization element-name guard (`"cityName"`) protecting `SavedStateHandle` cold-restoration compatibility with `FavoritesDetailViewModel.CITY_NAME_ARG`. | `Destinations.kt#L31` (renaming `cityName`), `Destinations.kt#L35` (inverting `isFavoriteDetail`), `Destinations.kt#L39` (removing `.trim()` or `.takeIf { it.isNotEmpty() }`), or `AppShellState.kt#L97-L108` (duplicating/reordering `NAV_OPTIONS`). | `KEEP` |

*Test Budget Check*: `2` new pure-JVM `@Test` methods in `1` new class (`<= 3`/class, `<= 6`/feature); total pure JVM unit tests across workspace = `42` (`35` `:app` + `1` `:domain` + `3` `:usecases` + `3` `:data`) + `60` Design System Roborazzi previews = `95/95` in `:app`; `0` MockK, `0` `RobolectricTestRunner`.

---

## 3.2 Adversarial Attempts Log

1. **Attempt — Verify Iteration 1 Finding 1 (Trigger #15 dead code) resolution and zero leftover unused symbols**: Inspected `git diff b4051ac..d5697fc` and ran `git grep -n "isTopLevelRouteSelected" -- app/src/` across the entire `:app` module. -> **Result**: `PASS` (Exact `-4` line deletion in `AppState.kt`; only the actively used 2-arg `AppState.isTopLevelRouteSelected(entry, route)` called from `Navigation.kt#L77` remains; `0` unused imports; `0` KDoc added; `0` other dead public/internal symbols).
2. **Attempt — Verify cumulative scope discipline, KDoc deferral, and zero legacy/suppression/lifecycle regressions**: Inspected `git diff --stat f98d403..d5697fc` and ran targeted `git grep` sweeps across `app/src/`, `data/`, `usecases/`, `domain/`. -> **Result**: `PASS` (`0` edits to `AndroidManifest.xml`, `MainActivity.kt`, `ui/FabState.kt`, `ui/navigation/shared/FabCustom.kt`, `ui/screens/**`, ViewModels, `ui/designsystem/**`, or `app/src/test/snapshots/**`; `0` KDoc `/**` on the 5 deferred files; `0` `@Suppress`/`@SuppressLint`; `0` wildcard imports; `0` `collectAsState(` in `Navigation.kt`; `0` `hiltViewModel`/`AppState`/`NavDestination` in `TopAppBarCustom.kt`; exact `1` `composable<DashboardScreens.Favorites>` registration; `0` legacy code references; `0` `io.mockk`/`RobolectricTestRunner`).
3. **Attempt — Independent uncached JDK `21.0.11` compiler, unit test, Lint XML, and Roborazzi parity verification**: Executed `assemblePreDebug --no-daemon`, `testPreDebugUnitTest lintPreDebug --parallel --build-cache --configuration-cache`, `:domain:test :usecases:test :data:test --rerun-tasks --no-daemon`, and `:app:verifyRoborazziPreDebug --rerun-tasks --no-daemon` inside `.worktrees/val-navigation-shell-unification-i2`, then programmatically inspected `lint-results-preDebug.xml`, all `TEST-*.xml`, Gradle compiler output, and `results-summary.json`. -> **Result**: `PASS` (`0` compiler warnings; `0` Lint warnings on all 9 touched `.kt` files out of 67 pre-existing project-wide Lint items; `:app` `95/95`, `:domain` `1/1`, `:usecases` `3/3`, `:data` `3/3`; Roborazzi `{"total":60,"recorded":0,"added":0,"changed":0,"unchanged":60}`; `git status` clean).
4. **Attempt — Break unified `CityForecast(cityName = "Mountain View")` detail routing, chrome isolation, and LMK process-death restoration live on `emulator-5554`**: Favorited `"Mountain View"` via the `FabState`-backed FAB, switched to `Favorites`, tapped `"Mountain View"`, verified Detail chrome (`"City weather"`, Back arrow, zero Bottom Nav / Logout / FAB), executed Probe 4A (`always_finish_activities 1 -> 0`) and Probe 4B (`PID_BEFORE=10115` -> `am kill` -> `''` -> Recents restore `PID_AFTER=10400`), then tapped TopAppBar Back arrow. -> **Result**: `PASS` (Restored directly onto `CityForecast("Mountain View")` with `"Mountain View"` weather and Detail chrome intact; TopAppBar Back popped cleanly to `Favorites`; `0` `AndroidRuntime:E` / `JavaBinder:E` logcat errors).
5. **Attempt — Break top-level tab switching, decoupled `TopAppBarCustom` Logout teardown, and cold `meteomarto://alert/high-temperature/38.5` deep link live on `emulator-5554`**: Cold-launched `meteomarto://alert/high-temperature/38.5`, ran Probes 4A & 4B (`PID 9240 -> 9441`), dismissed via `"Understood"` into `CityForecast(null)`, switched `City` <-> `Favorites` (running Probes 4A & 4B on both `Favorites` `PID 9441 -> 9732` and `CityForecast(null)` `PID 9732 -> 10115`), tapped TopAppBar `"Log out"` -> confirmed `"Continue"` -> verified `AuthScreens.Login` with zero dashboard chrome. -> **Result**: `PASS` (All transitions, chrome switches, and LMK restorations succeeded with `0` crashes or Binder errors).
6. **Attempt — Break non-root `SubGraphs.Auth` routes (`AuthScreens.Terms` and `AuthScreens.SignUp`) under Activity recreation & LMK process death on `emulator-5554`**: Navigated `Login` -> `Terms` and `Login` -> `SignUp`, executed Probes 4A & 4B on both (`Terms`: `PID 7697 -> 7925`; `SignUp`: `PID 8903 -> 9114`), and verified Back popped cleanly to `Login`. -> **Result**: `PASS` (Both non-root auth routes survived Activity recreation and true process death with zero dashboard chrome bleed-through and `0` logcat errors; `always_finish_activities` verified reset to `0`).

---

## 3.3 Level 4 Process Death & Recreation Matrix

*(Executed exclusively on `-s emulator-5554` with `com.martorell.albert.meteomartocompose` preDebug APK; physical device `adb-2C241JEHN09429...` strictly untouched; `always_finish_activities` confirmed reset to `0` after every Probe 4A pass and at final cleanup.)*

| Screen / Destination & Active State | Probe 4A (Activity Recreation) | Probe 4B (LMK `am kill` + Recents) | Logcat & Restored UI Evidence |
| :--- | :---: | :---: | :--- |
| `DashboardScreens.CityForecast(cityName = null)` — Current location `"Mountain View"` loaded (`28.5°C`, clear sky), TopAppBar `"Meteo Marto"` + Logout, Favorite FAB, Bottom Nav | `PASS` | `PASS` | `PID 9732 -> '' -> 10115`. Restored directly onto `CityForecast(null)` with `"Mountain View"`, `"Meteo Marto"`, `"Log out"`, `"Favorite"` FAB, and Bottom Nav (`City`, `Favorites`). `0` `AndroidRuntime:E` / `JavaBinder:E` errors. |
| `DashboardScreens.Favorites` — Secondary top-level tab, TopAppBar `"List of Favorites"` (no Back/Logout/FAB), Bottom Nav | `PASS` | `PASS` | `PID 9441 -> '' -> 9732`. Restored directly onto `Favorites` with `"List of Favorites"`, zero Logout/Back/FAB, and Bottom Nav intact. `0` logcat errors. |
| `DashboardScreens.CityForecast(cityName = "Mountain View")` — Unified non-root Favorite Detail via `SavedStateHandle["cityName"]`, Detail chrome (`"City weather"` + Back arrow, no Bottom Nav/Logout/FAB) | `PASS` | `PASS` | `PID 10115 -> '' -> 10400`. Restored directly onto `CityForecast("Mountain View")` reloading `"Mountain View"` (`28.5°C`) with `"City weather"` + `"Back"` arrow and zero Bottom Nav/FAB/Logout; tapping TopAppBar `"Back"` popped cleanly to `Favorites`. `0` logcat errors. |
| `DashboardScreens.HighTemperatureAlert(temperature = 38.5)` — Cold `meteomarto://alert/high-temperature/38.5` dialog destination | `PASS` | `PASS` | `PID 9240 -> '' -> 9441`. Restored directly onto `"High Temperature Alert!"` (`38.5°C`) with zero TopAppBar/Bottom Nav bleed-through; tapping `"Understood"` dismissed cleanly via `appState.navigateUp()` to `CityForecast(null)`. `0` logcat errors. |
| `AuthScreens.Terms` — Non-root Auth destination (zero dashboard chrome) | `PASS` | `PASS` | `PID 7697 -> '' -> 7925`. Restored directly onto `"Terms and conditions"` with zero dashboard chrome; `KEYCODE_BACK` popped cleanly to `AuthScreens.Login`. `0` logcat errors. |
| `AuthScreens.SignUp` — Non-root Auth destination (zero dashboard chrome) | `PASS` | `PASS` | `PID 8903 -> '' -> 9114`. Restored directly onto `"Create a new user"` with zero dashboard chrome; `KEYCODE_BACK` popped cleanly to `AuthScreens.Login`. `0` logcat errors. |

---

## 4. Implementation Repair Brief (Required when Verdict is `revise` or `block`)

N/A — Feature accepted and ready for `workflow-documenter`.
