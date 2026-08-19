# Feature: High Temperature Push Notification (MM-01)

## 1. Idea Diagnosis & Project Impact
- **Objective:** Monitor current temperature in background and send a push notification if it exceeds a threshold defined in Firebase Remote Config.
- **Impacted Modules:** `:domain`, `:data`, `:usecases`, `:app`.
- **Testing Requirement:** **High**. The threshold comparison logic and Remote Config mapping require Unit Tests to ensure reliability.

## 2. Technical Overview
This feature implements a background monitoring system using **WorkManager**. It follows the **SSOT** policy by considering Firebase Remote Config as the source for the threshold. The logic is encapsulated in a UseCase to maintain Clean Architecture and "Zero Leakage".

## 3. Implementation Workflow

### Stage 1: Domain Layer (:domain)
- [x] Define `RemoteConfigRepository` interface with `getTemperatureThreshold(): Flow<Double>`.
- [x] Define `NotificationService` interface for system notifications.

### Stage 2: Data Layer - Infrastructure (:app/framework)
- [x] Add Firebase Remote Config dependency to `libs.versions.toml`.
- [x] Add Google Services plugin to `app/build.gradle.kts` (if missing).
- [x] Execute **Gradle Sync**.
- [x] Create `res/xml/remote_config_defaults.xml` with `temperature_threshold = 30.0`.
- [x] Implement `FirebaseRemoteConfigDataSource` in `:app/framework/remoteconfig`.
- [x] Implement `NotificationServiceImpl` in `:app/framework/notification` (Priority: HIGH).
- [x] Implement `RemoteConfigRepositoryImpl` in `:app/data/remoteconfig` (Bridge between interface and DataSource).
- [x] **Unit Test**: `RemoteConfigMapper` for boundary values.

### Stage 3: Use Cases & Fullscreen Alert UI (:usecases & :app)
- [x] Create `CheckTemperatureThresholdUseCase`:
    - Fetch current temp from existing `CityWeatherRepository`.
    - Fetch threshold from `RemoteConfigRepository`.
    - Compare and return a result indicating if alert should be shown.
- [x] **Unit Test**: `CheckTemperatureThresholdUseCase` (Verify logic triggers correctly).
- [x] **UI Screen**: Implement `HighTemperatureAlertScreen` (Compose):
    - Fullscreen overlay.
    - Close icon (X) at top-right.
    - Title: `high_temp_notif_title`.
    - Body: `high_temp_notif_content` with dynamic temperature.
    - Footer button: `accept_warning`.
- [x] **Navigation Logic**:
    - Define `HighTemperatureAlert` route in `NavigationUtils.kt`.
    - Add to `HomeNavGraph.kt` as a dialog or fullscreen destination.
    - Trigger check in `CityWeatherViewModel` on launch.

### Stage 4: Application & Background (:app)
- [x] Create `TemperatureCheckWorker` using WorkManager.
- [x] Provide Hilt bindings for `RemoteConfigRepository` and `NotificationService` in specialized modules.
- [x] Schedule `PeriodicWorkRequest` in `Application` class with dynamic interval from Remote Config.
- [x] Refactor Worker to orchestrate atomic UseCases instead of a God UseCase.
- [x] Implement robust error handling with fallback values in Remote Config.

## 4. Verification Checklist
- [ ] Run `./gradlew :usecases:test` to verify threshold logic.
- [ ] Run `./gradlew :data:test` to verify mappers.
- [ ] Deploy app to emulator: `android run`.
- [ ] Trigger Worker manually via ADB: `adb shell am broadcast -a com.google.android.gms.gcm.ACTION_TASK_READY...`
- [ ] Verify System Tray Notification appears with correct temperature.

## 5. Git Strategy (MM-01)
- **Branch**: `feature/MM-01-high-temp-notification`
- **Commits**:
    1. `feat(domain): [MM-01] define notification and remote config interfaces`
    2. `feat(data): [MM-01] implement firebase remote config and notification service`
    3. `test(data): [MM-01] add unit tests for remote config mappers`
    4. `feat(usecases): [MM-01] implement temperature threshold check logic`
    5. `test(usecases): [MM-01] add unit tests for threshold comparison`
    6. `feat(app): [MM-01] setup workmanager worker and hilt dependencies`
