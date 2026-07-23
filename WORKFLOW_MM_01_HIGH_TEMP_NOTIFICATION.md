# Feature: High Temperature Push Notification (MM-01)

## 1. Idea Diagnosis & Project Impact
- **Objective:** Monitor current temperature in background and send a push notification if it exceeds a threshold defined in Firebase Remote Config.
- **Impacted Modules:** `:domain`, `:data`, `:usecases`, `:app`.
- **Testing Requirement:** **High**. The threshold comparison logic and Remote Config mapping require Unit Tests to ensure reliability.

## 2. Technical Overview
This feature implements a background monitoring system using **WorkManager**. It follows the **SSOT** policy by considering Firebase Remote Config as the source for the threshold. The logic is encapsulated in a UseCase to maintain Clean Architecture and "Zero Leakage".

## 3. Implementation Workflow

### Stage 1: Domain Layer (:domain)
- [] Define `RemoteConfigRepository` interface with `getTemperatureThreshold(): Flow<Double>`.
- [] Define `NotificationService` interface for system notifications.
- [ ] Define `ThresholdExceededException` (optional) or Result types.

### Stage 2: Data Layer - Infrastructure (:data)
- [ ] Implement `FirebaseRemoteConfigDataSource` using Firebase SDK.
- [ ] Implement `NotificationServiceImpl` using Android `NotificationManager`.
- [ ] Implement `RemoteConfigRepositoryImpl` (Mapping Firebase values to Domain).
- [ ] **Unit Test**: Validate `RemoteConfigMapper` handles nulls or invalid strings.

### Stage 3: Use Cases & Logic (:usecases)
- [ ] Create `CheckTemperatureThresholdUseCase`:
    - Fetch current temp from existing `CityWeatherRepository`.
    - Fetch threshold from `RemoteConfigRepository`.
    - Compare and trigger `NotificationService`.
- [ ] **Unit Test**: `CheckTemperatureThresholdUseCase` (Verify notification triggers only when `current > threshold`).

### Stage 4: Application & Background (:app)
- [ ] Create `TemperatureCheckWorker` using WorkManager.
- [ ] Provide Hilt bindings for `RemoteConfigRepository` and `NotificationService` in `AppModule`.
- [ ] Schedule `PeriodicWorkRequest` in `Application` class (1-hour interval).

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
