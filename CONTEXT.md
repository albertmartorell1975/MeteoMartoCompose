# Context

## Glossary

### City Weather
The current meteorological snapshot for a specific geographic location, fetched from OpenWeather API and persisted locally in Room as the Single Source of Truth (SSOT). Includes temperature (current, min, max), weather condition code, description, icon, rain volume, atmospheric pressure, coordinates, day/night state (`isNight`), favorite flag, and alert notification state.

### Current Location
The device's active GPS/network coordinates resolved via Google Play Services Location. Determines the primary city displayed on `CityForecastScreen` and serves as the sole trigger for `ExtremeAlert`.

### Favorite City
A `City Weather` record explicitly bookmarked by the authenticated user and persisted in Room. Listed neutrally on `FavoritesScreen` and opened in full dynamic mode via `CityForecastScreen`.

### Weather Condition
Finite domain classification derived from OpenWeather condition IDs (`weather[0].id`) used to drive dynamic theming on `CityForecastScreen`:
- `SUNNY`: Clear sky (OpenWeather code `800`).
- `RAIN`: Thunderstorm (`200..232`), drizzle (`300..321`), rain (`500..531`), or snow (`600..622`).
- `BASELINE`: Clouds (`801..804`), atmosphere/mist/fog (`700..781`), or fallback/unknown states.

### Nocturnal State (`isNight`)
Boolean domain indicator representing whether the observed city is currently experiencing nighttime, derived from OpenWeather telemetry (`weatherIcon` ending in `"n"`, with `dt < sunrise || dt > sunset` as structural reference) and persisted in Room for offline consistency.

### Extreme Alert (`ExtremeAlert`)
High-temperature warning state triggered exclusively when the **Current Location** temperature strictly exceeds (`>`) the threshold configured in Firebase Remote Config (`temperature > threshold`). Drives both the persistent high-temperature alert experience (`HighTemperatureAlertScreen` / notification) and the highest-priority `ExtremeAlertPalette`.

### Theme Palette (`SimpleWeatherPalette`)
Minimal deterministic 4-token color palette (`background`, `surface`, `onSurface`, `primary`) applied across screens according to the KISS dynamic theming contract (`DayBaseline`, `Sunny`, `Rain`, `Night`, `ExtremeAlert`).

### Domain Error (`DomainError`)
Typed, layer-pure Kotlin sealed hierarchy in `:domain` representing business, network, authentication, location, permission, and persistence failures without leaking Android, Retrofit, Room, or Firebase exceptions across layer boundaries.

### System Abstraction (`Checker` / `SystemService`)
Interfaces defined in `:data` and exposed via `:usecases` that encapsulate Android runtime permissions and OS settings intents so UI composables and ViewModels never import `android.Manifest`, `Build.VERSION`, or `Context`.

### Design System (`Fnd*` & `Mm*`)
Project UI component library under `app/.../ui/designsystem/` composed of foundation tokens (`Fnd*`: colors, typography, spacing, dimensions, motion) and stateless Material 3 components (`Mm*`).

## Rejected / Ambiguous Terms

### `CustomError`
Use `DomainError` instead. Reason: `CustomError` lives in `:data` and mixes infrastructure concerns; Foundation v2.0.0 (`repository-pattern-governance`) mandates a pure typed `DomainError` model owned by `:domain`.

### `FavoritesDetailScreen` (as a separate visual layout)
Use `CityForecastScreen` (in Favorite Detail mode) instead. Reason: Per `docs/ui/meteomarto_design_contract.txt`, viewing the current city or tapping a favorite city shares the same dynamic forecast screen contract.

### WMO Weather Codes
Use OpenWeather Condition IDs instead. Reason: The application consumes OpenWeather API (`CityWeatherResponse`), where clear sky is `800` and snow is `600..622` rather than WMO codes `0..1` / `50..82`.

### Fixed `>35°C` Alert Threshold
Use Remote Config Temperature Threshold instead. Reason: The high-temperature threshold is dynamically governed by Firebase Remote Config (with a safe fallback default), never hardcoded in UI or domain logic.
