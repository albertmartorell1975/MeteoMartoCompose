# Feature: High Temperature Push Notification (MM-01)

## 1. Idea Diagnosis & Project Impact
- **Objective:** Monitor current temperature in background and send a push notification if it exceeds a threshold defined in Firebase Remote Config.
- **Impacted Modules:** `:domain`, `:data`, `:usecases`, `:app`.
- **Testing Requirement:** **High**. The threshold comparison logic and Remote Config mapping require Unit Tests to ensure reliability.

## 2. Technical Overview
This feature implements a background monitoring system using **WorkManager**. It follows the **SSOT** policy by considering Firebase Remote Config as the source for the threshold. The logic is encapsulated in a UseCase to maintain Clean Architecture and "Zero Leakage". 

**Key Architectural Decision:** The Splash Screen has been decoupled from the Navigation Graph and moved to a Root State Pattern in `MainActivity` to ensure reliable Deep Link handling and prevent navigation loops when exiting the app.

## 3. Implementation Workflow

### Stage 1: Domain Layer (:domain)
- [x] Define `RemoteConfigRepository` interface with `getTemperatureThreshold(): Flow<Double>`.
- [x] Define `NotificationService` interface for system notifications.
- [x] Define `PermissionRepository` with `checkNotificationPermission(): Boolean` support.

### Stage 2: Data Layer - Infrastructure (:app/framework)
- [x] Add Firebase Remote Config dependency to `libs.versions.toml`.
- [x] Add Google Services plugin to `app/build.gradle.kts`.
- [x] Implement `FirebaseRemoteConfigDataSource` with `ConfigUpdateListener` for real-time updates.
- [x] Implement `NotificationServiceImpl` using `NotificationChannel` (Importance: HIGH).
- [x] Implement `PermissionRepositoryImpl` to handle `POST_NOTIFICATIONS` check (Android 13+).
- [x] **Refinement**: Configure `RemoteConfigRepositoryImpl` as a `@Singleton` with `shareIn(Eagerly)` to maintain a stable connection to Firebase.

### Stage 3: Use Cases & UI Refactoring (:usecases & :app)
- [x] Create `CheckTemperatureThresholdUseCase`.
- [x] Create `CheckNotificationPermissionUseCase`.
- [x] **Refinement: Splash Screen Overhaul**:
    - [x] Create `MainViewModel` to manage the global "Root State" (Loading, LoggedIn).
    - [x] Create `SplashUI.kt` as a stateless Composable.
    - [x] Remove Splash from `NavHost` and `authSubGraph.kt` to fix backstack loops.
- [x] **UI Screen**: Implement `HighTemperatureAlertScreen` with Deep Link support (`meteomarto://alert/high-temperature`).
- [x] **Navigation**: Update `Navigation.kt` to skip splash delay if an intent data (Deep Link) is present.

### Stage 4: Application & Background Orchestration (:app)
- [x] Create `TemperatureCheckWorker` using WorkManager.
    - [x] Implement `Expedited` work support to bypass background restrictions.
    - [x] Ensure `appLifecycleObserver` prevents push notifications when the app is in foreground.
- [x] Implement `WorkScheduler`:
    - [x] Reactive observation of Firebase changes to trigger immediate checks.
    - [x] Periodic checking every 15 minutes (System limit).
- [x] **Logging**: Unify all debug traces under the `MeteoMartoDebug` tag.
- [x] **Refinement: Centralized Constants**:
    - [x] Create `AppConstants.kt` to eliminate hardcoded strings (Magic Literals) like `"package"`, deep link URIs, and notification IDs.
    - [x] Update `kotlin-style` skill to strictly forbid wildcard imports and magic literals.

## 4. Verification Checklist
- [ ] Run `./gradlew :usecases:test` to verify threshold logic.
- [ ] Run `./gradlew :data:test` to verify mappers.
- [x] Deploy app to emulator/device: `android run`.
- [x] Verify Permission Rationale adaptively (Location vs Notifications).
- [x] Verify Real-time trigger: Change threshold in Firebase console and receive push in background.
- [x] Verify Deep Link: Clicking notification opens Alert UI without Splash loops.
- [x] Verify Exit logic: Pressing 'Back' from Dashboard closes the app correctly.

## 5. Git Strategy (MM-01)
- **Branch**: `feature/MM-01-high-temp-notification`
- **Latest Commit**: `refactor(ui): [MM-01] fix notification permissions and uncouple splash screen from navigation graph`
