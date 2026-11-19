# Build Brief

## Problem

`MeteoMartoCompose` was originally built and partially migrated to a Design System (`feature/MM-02-design-system`) before the Android AI Workflow Foundation v2.0.0 existed. As a result, the codebase has accumulated architectural and visual drift:
- Conflicting visual references (legacy Material 3 violet tokens vs. new Stitch screen exports and the KISS dynamic weather palette contract).
- Legacy data/error patterns (`CustomError` in `:data`, missing OpenWeather condition ID and `isNight` persistence in Room, hardcoded OpenWeather API key in `flavors.gradle`).
- Screen-level Robolectric screenshot tests (`*ScreenshotTest.kt`) and MockK-heavy tests that diverge from the Foundation v2.0.0 pragmatic testing strategy (`testing-setup` v2.4.0).
- Inconsistencies across ViewModels, Compose state/navigation wiring (`CityWeatherScreen` vs. `FavoritesDetailScreen`), and outdated CI/tooling paths.

## Current Workaround / Existing System

The repository already delivers a functional Android application (`Layer-Based`: `:domain`, `:usecases`, `:data`, `:app`) with:
- Firebase Authentication (`Splash`, `Login`, `SignUp`, `Terms`, `Logout`).
- Current-location weather retrieval via OpenWeather API, Room caching, and background/foreground high-temperature alerts governed by Firebase Remote Config (`MM-01`).
- Favorites list & detail viewing.
- Initial `Fnd*` / `Mm*` Design System components, Roborazzi preview scanning, Dokka, and JaCoCo (`MM-02`).

## Target Users

- **Authenticated End Users (Android)**: Mobile and tablet/foldable users who sign in to monitor real-time weather at their current location, receive high-temperature alerts, and manage favorite cities.
- **Maintainers & AI Subagents**: Developers and Foundation v2.0.0 subagents (`planner`, `implementer`, `validator`, `documenter`) evolving the app cleanly under deterministic quality gates.

## Goals

1. **Unify the Visual Direction & Dynamic Design System**: Align `Fnd*` tokens and `Mm*` components with `DESIGN.md` (the 5 KISS weather palettes from `docs/ui/meteomarto_design_contract.txt`, Stitch layouts in `docs/ui/screens/`, `Roboto Flex` with `12.sp` minimum font size, and `.webp` assets from `docs/ui/resources/`).
2. **Modernize All Clean Architecture Layers**: Upgrade `:domain`, `:data`, `:usecases`, and `:app` to comply with Foundation v2.0.0 skills (`repository-pattern-governance`, `viewmodel-architecture-governance`, `compose-navigation-architecture`, `compose-state-hoisting`, `hilt`, `room-schema-governance`, `dependency-manager`).
3. **Eliminate Secret Leakage**: Migrate hardcoded OpenWeather API credentials out of `flavors.gradle` using `secrets-gradle-plugin` and `local.defaults.properties`.
4. **Adopt Pragmatic High-Value Testing (`testing-setup` v2.4.0)**: Standardize on pure JVM unit tests with manual Fakes, keep Roborazzi strictly for isolated Design System (`Mm*`) `@Preview` components, remove screen-level `*ScreenshotTest.kt` bloat, and rely on `@android-cli` for black-box E2E validation.
5. **Slice Incrementally Without Scope Creep**: Break the modernization into cohesive, independently verifiable features in `feature_list.json` without introducing new product features.

## Non-Goals

- **No new end-user features**: Hourly/7-day forecasts, manual city search, home-screen widgets, user profile editing, or Wear OS are explicitly out of scope.
- **No Navigation 3 migration**: Keep stable type-safe Jetpack Navigation Compose (`AppState` + Destinations pattern) to honor `dependency-manager` Stable Versions Policy.
- **No Feature-First re-modularization**: Preserve the existing `Layer-Based` module topology (`:domain`, `:usecases`, `:data`, `:app`).
- **No manual theme switcher**: Dynamic theming on `CityForecastScreen` is strictly driven by live weather telemetry and Remote Config alert state.
- **No Robolectric functional or screen-level screenshot tests** for application screens.

## Modernization Vertical Slices (Scope Outline)

To be finalized into `feature_list.json` during `@harness-starter`, the modernization decomposes into clean sequential slices:
1. **Baseline & Security Hygiene**: Reconcile branch alignment (`develop` vs. `feature/MM-02-design-system` JDK/JVM config), migrate API secrets to `secrets-gradle-plugin` + `local.defaults.properties`, and establish the CI quality gate (`.github/workflows/gate.yml`).
2. **Domain, Data & Room Modernization**: Introduce pure `:domain` `DomainError`, OpenWeather `WeatherCondition` mapping, `weatherConditionId` + `isNight` fields with an explicit Room schema migration, and SSOT repository/data source cleanup.
3. **Dynamic Design System & Asset Alignment**: Implement the 5 KISS `SimpleWeatherPalette` themes (`DayBaseline`, `Sunny`, `Rain`, `Night`, `ExtremeAlert`), enforce `12.sp` minimum typography on `Roboto Flex`, convert/install `docs/ui/resources/*.webp` into `app/src/main/res/drawable/`, and align `Mm*` components + Roborazzi Design System snapshots.
4. **Navigation, Auth & Core Screens Overhaul**: Refactor Navigation Compose (`AppState` + unified `CityForecastScreen` for current location and favorite detail), rebuild Auth (`Splash`, `Login`, `Register`, `Terms`), `CityForecastScreen`, `FavoritesScreen`, `HighTemperatureAlertScreen`, `ErrorForecastScreen`, and `LogoutDialog` against Stitch layouts and `viewmodel-architecture-governance`.
5. **Pragmatic Testing Alignment**: Migrate unit tests toward manual Fakes and strict test budgets (`testing-setup` v2.4.0), retire screen-level `*ScreenshotTest.kt` files, and update `docs/testing_strategy_guide.md`.

## Validation Plan

- **Deterministic Local & CI Gate**: `./gradlew testPreDebugUnitTest lintPreDebug --parallel --build-cache --configuration-cache` (JDK 21).
- **Design System Visual Regression**: `./gradlew :app:verifyRoborazziPreDebug` scanning `ui.designsystem.components` previews only.
- **Pure JVM Unit Tests**: Deterministic tests for domain rules (`WeatherCondition`, alert threshold), data mappers (`Throwable` -> `DomainError`, Room migration), use cases, and ViewModels (`Passive Initialization` + `Hybrid UI State`).
- **Black-Box E2E Runtime (Level 4)**: Executed on emulator via `@android-cli` against each feature's `Given / When / Then` acceptance scenarios when an emulator is active.

## Success Criteria

- Zero hardcoded API keys in Gradle build scripts; clean builds succeed on fresh clones and CI using `local.defaults.properties`.
- Every screen matches its assigned palette and layout contract in `DESIGN.md` with zero typography below `12.sp` and full RTL / 200% font-scaling compliance.
- `CityForecastScreen` dynamically reacts to `ExtremeAlert > Night > Sunny > Rain > DayBaseline` both online and offline from Room.
- All ViewModels, Repositories, Data Sources, and Navigation conform to Foundation v2.0.0 skills with zero Robolectric screen tests and passing CI gate.

## Notes

- Existing feature records under `docs/features/temperature_warning/` (`MM-01`) and `docs/features/design_system/` (`MM-02`) are preserved as historical context.
- Repository flavors (`pre` and `pro` in `flavors.gradle`) require `PreDebug` Gradle task variants in both local verification and CI.
