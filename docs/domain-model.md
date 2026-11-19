# Domain Model

## Core Concepts

### User (`UserDomain`)
Represents the authenticated Firebase user session (`id`, `email`, `isAnonymous`). Required to enter the authenticated shell (`CityForecastScreen`, `FavoritesScreen`).

### Current Location (`CurrentLocationDomain`)
Geographic coordinates (`latitude`, `longitude`) obtained from the device location provider. Used to fetch and persist the primary city weather and evaluate high-temperature alerts.

### City Weather (`CityWeatherDomain`)
Core weather aggregate persisted in Room (SSOT) and refreshed from OpenWeather API:
- Identity & location: `name`, `latitude`, `longitude`.
- Telemetry: `temperature`, `temperatureMin`, `temperatureMax`, `rain`, `pressure`, `weatherDescription`, `weatherIcon`.
- Dynamic theming attributes (to be added in modernization):
  - `weatherConditionId: Int` (raw OpenWeather `weather[0].id`).
  - `weatherCondition: WeatherCondition` (`BASELINE`, `SUNNY`, `RAIN` derived from `weatherConditionId`).
  - `isNight: Boolean` (derived from `weatherIcon?.endsWith("n")` with `dt < sunrise || dt > sunset` fallback).
- User & alert flags: `favorite`, `justAdded`, `isAlertNotified`.

### Weather Condition (`WeatherCondition`)
Pure domain enum mapped deterministically from OpenWeather `weather[0].id`:
- `SUNNY`: `id == 800` (Clear sky).
- `RAIN`: `id in 200..232` (Thunderstorm), `300..321` (Drizzle), `500..531` (Rain), `600..622` (Snow).
- `BASELINE`: All other codes (`700..781` Atmosphere/Mist/Fog, `801..804` Clouds, or unknown/missing).

### Temperature Alert (`TemperatureAlertResult`)
Result of evaluating the **Current Location** weather against Firebase Remote Config's temperature threshold:
- Trigger rule: `currentTemperature > threshold` (strictly greater than the Remote Config threshold, with a safe fallback default if Remote Config fetch fails).
- Scope rule: Applies **only to the user's current location**, never to remote favorite cities viewed in detail.
- State: `showAlert` (one-time foreground/notification trigger guarded by `isAlertNotified`) and `isPersistentAlertActive` (keeps `ExtremeAlert` active while the condition persists).

### Domain Error (`DomainError`)
Pure Kotlin sealed error hierarchy in `:domain` replacing `:data`'s `CustomError`:
- Authentication errors (invalid credentials, email already in use, weak password, user not found, session expired).
- Network & API errors (no connectivity, timeout, HTTP/server failure, serialization failure).
- Location & permission errors (location permission denied, GPS/location services disabled, location unavailable).
- Persistence & unknown errors (Room read/write failure, unexpected error).

## Relationships

- One authenticated `User` accesses the weather and favorites experience on the device.
- One `Current Location` resolves to the primary `City Weather` record displayed on `CityForecastScreen`.
- Zero or many `City Weather` records are marked `favorite = true` and displayed in `FavoritesScreen`.
- Selecting a favorite city opens the same `CityForecastScreen` bound to that `City Weather` record's telemetry (without evaluating current-location `ExtremeAlert` unless it is the current location).
- One Remote Config `threshold` governs `TemperatureAlertResult` evaluation for the current location.

## States and Lifecycles

### Authentication Lifecycle
`Unauthenticated` -> (`Login` or `SignUp` + `Terms` acceptance) -> `Authenticated` -> (`Logout` via `LogoutDialog`) -> `Unauthenticated`.
Revocation/expiry: signing out clears Firebase Auth session and immediately navigates back to the Auth graph, preventing access to weather or favorites screens.

### City Weather & Dynamic Theme Resolution Lifecycle
`Loading` -> (`Fetch OpenWeather` + `Persist in Room SSOT`) -> `Success(CityWeatherDomain)` or `Error(DomainError)`.
When `Success` is rendered on `CityForecastScreen`, theme palette resolves with strict precedence:
1. `isExtremeAlert == true` (Current Location `temperature > threshold`) -> `ExtremeAlertPalette`
2. `isNight == true` -> `NightPalette`
3. `weatherCondition == SUNNY` -> `SunnyPalette`
4. `weatherCondition == RAIN` -> `RainPalette`
5. Otherwise -> `DayBaselinePalette`

### Favorite Lifecycle
`Not Favorite` -> (Tap Favorite FAB on `CityForecastScreen`) -> `Favorite` (persisted in Room, visible in `FavoritesScreen`) -> (Tap delete + confirm or toggle FAB) -> `Not Favorite`.

### High-Temperature Alert Lifecycle
`Inactive` -> (Current location `temperature > threshold` and `!isAlertNotified`) -> `AlertTriggered` (notification + `HighTemperatureAlertScreen` + `ExtremeAlertPalette`, sets `isAlertNotified = true`) -> (User acknowledges) -> `PersistentActive` (remains in `ExtremeAlertPalette` while `temperature > threshold`) -> (Temperature drops `<= threshold`) -> `Inactive` (resets `isAlertNotified = false`).

## Important Scenarios

1. **Offline / Cached Viewing**: If the device loses network connectivity after a successful sync, Room SSOT still provides `CityWeatherDomain` including `weatherConditionId` and `isNight` so `CityForecastScreen` renders the last known dynamic palette without crashing.
2. **Mixed-Weather Favorites List**: A user has sunny, rainy, and nighttime cities in `FavoritesScreen`. The list always remains neutral (`DayBaselinePalette`); tapping any city opens `CityForecastScreen` with that city's dynamic palette.
3. **Permission Denied / GPS Disabled**: Missing location permission or disabled GPS produces a typed `DomainError` and routes actions through `SystemService` use cases (`OpenAppSettingsUseCase`, `OpenLocationSettingsUseCase`) without leaking Android APIs into Compose.

## Edge Cases

- **Missing or unknown OpenWeather ID**: Defaults safely to `WeatherCondition.BASELINE` (`DayBaselinePalette`).
- **Missing `weatherIcon` suffix (`d`/`n`)**: Falls back to comparing `dt` against `sys.sunrise` and `sys.sunset`; if timestamps are unavailable, defaults to `isNight = false`.
- **Room Upgrade on Existing Installs**: Adding `weatherConditionId` and `isNight` columns requires an explicit Room `Migration` with safe defaults (`0` / `false`) to avoid destructive database wipes.
- **Remote Config Unreachable**: Falls back to in-app default threshold so alert evaluation never blocks weather loading.
- **Unauthenticated Startup**: `Splash` checks active session passively and routes directly to `Login` (not weather) when no session exists.
