# Risks and Open Questions

## Blocking Next Phase

None. Discovery is complete enough to proceed to `@harness-starter` and derive `CONSTRAINTS.md`, `PROGRESS.md`, `feature_list.json`, and `.github/workflows/gate.yml`.

## Implementation-Time Questions

1. **Branch Reconciliation (`develop` vs. `feature/MM-02-design-system`)**: `develop` has 5 recent commits updating `:domain`/`:data`/`:usecases` to Java/Kotlin JVM `17` and `:app` to `17`, whereas `feature/MM-02-design-system` uses `jvmToolchain(21)` in `:app` (required for Roborazzi/Robolectric + SDK 36+) and `11` in pure JVM modules. Decide exact rebase/merge timing and whether pure JVM modules align on `17` or `21`.
2. **GitHub CLI Authentication**: `gh` (`2.102.1`) is installed locally, but the active token for `albertmartorell1975` is currently invalid (`gh auth status`). Run `gh auth login -h github.com` before invoking `@workflow-orchestrator` in `publish` or `review-fix` mode.
3. **`isNight` Tie-Breaker**: Confirm whether `weatherIcon?.endsWith("n")` is always primary (with `dt < sunrise || dt > sunset` only as fallback when icon is absent) when mapping `CityWeatherResponse` to `CityWeatherDomain`.
4. **Favorite Empty State & Delete Confirmation Visuals**: Stitch exports cover the 9 primary screens; `FavoriteEmptyState` and the delete-favorite confirmation dialog will reuse `DayBaselinePalette` with `MmErrorState` / `MmDialog` unless dedicated mockups are added later.

## Later / Not MVP

- Additional weather features (hourly forecast, 7-day forecast, manual city search, home-screen widgets).
- Migration from Jetpack Navigation Compose to Jetpack Navigation 3 (deferred until stable and aligned with project roadmap).
- Feature-First multi-module refactoring.
- Manual user override for light/dark/climate themes.

## Assumptions

- OpenWeather API remains the sole weather data provider, and Firebase Auth + Firebase Remote Config remain the authentication and feature-threshold providers.
- Product flavors `pre` and `pro` (`flavors.gradle`) remain active, so Gradle verification gates use `testPreDebugUnitTest` and `lintPreDebug` with JDK 21 (`jbr-21` locally, Temurin JDK 21 on GitHub Actions).
- Existing `docs/features/temperature_warning/` (`MM-01`) remains untouched as a historical record while legacy `docs/features/design_system/` (`MM-02`) was retired once superseded by `DESIGN.md` and unified `docs/features/<feature-id>/` folders.

## Risks

1. **Room Schema Migration Risk**: Adding `weatherConditionId` and `isNight` columns to the weather entity without an explicit `Migration` (and schema export verification per `room-schema-governance`) could crash existing installations or wipe saved favorites.
2. **Hardcoded API Key Exposure**: `flavors.gradle` currently commits `API_KEY_VALUE` in plain text. Migrating to `secrets-gradle-plugin` + `local.defaults.properties` must be verified in CI so `BuildConfig` compiles cleanly without a local `local.properties` file.
3. **Outdated `design-system-ci.yml` Trigger Paths**: `.github/workflows/design-system-ci.yml` currently watches legacy paths (`ui/components/designsystem/**` and `ui/theme/**`) instead of `ui/designsystem/**`; visual regression CI will silently skip triggers until updated.
4. **Contrast on Dark Dynamic Palettes**: Semantic thermal accents (`#D32F2F`, `#1976D2`) must be verified for WCAG contrast against `NightPalette` (`#0E1419` / `#161C21`) and `ExtremeAlertPalette` (`#701A1A` / `#8B2323`).
5. **JDK 25 vs. Gradle 8.14.3 Local Mismatch**: The system default Java / Android Studio embedded JBR on Windows is `25.0.4`, while Gradle `8.14.3` supports up to Java 24. Local builds and Android Studio MUST continue using `jbr-21`.

## Research Tasks

- Audit existing `AppDatabase` version and `CityWeatherEntity` columns before planning the Room migration slice.
- Audit all usages of `CustomError` across `:data`, `:usecases`, and `:app` to plan a clean `DomainError` migration with zero layer leakage.
- Audit all existing `*Test.kt` and `*ScreenshotTest.kt` files against `testing-setup` v2.4.0 to separate Design System Roborazzi coverage from Pure JVM unit tests.
