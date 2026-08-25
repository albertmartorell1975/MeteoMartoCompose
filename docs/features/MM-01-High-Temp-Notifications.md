# Definitive Technical Record - High Temperature Alerts (MM-01)

This document is the official and complete technical record of the **MM-01** feature development. It captures the full journey from initial domain modeling to high-level architectural hardening and governance evolution.

---

## 1. Core Technical Milestones

### Feature Lifecycle
*   **Domain Modeling**: Defined the `TemperatureAlertResult` entity and updated domain models to support stateful threshold logic.
*   **SSOT & Remote Config**: Established **Firebase Remote Config** as the Single Source of Truth for thresholds. Implemented a reactive real-time listener to trigger background checks instantly upon console updates.
*   **Persistent Alerts**: Updated Room DB and Repositories to track `isAlertNotified`, ensuring users are alerted once per "heat wave" but resetting automatically when temperatures normalize.

### Infrastructure & Background
*   **Worker Reliability**: Developed `TemperatureCheckWorker` with `Expedited` job status to bypass Android battery-saving restrictions on devices like the Pixel 7a.
*   **Warm Boot Support**: Configured `MainActivity` with **`launchMode="singleTop"`**. This ensures that clicking a notification delivers the intent to the existing activity instead of creating a redundant instance.
*   **DI Best Practices**: Refactored Hilt modules by strictly separating **Providing** and **Binding** logic, improving build performance and dependency clarity.

---

## 2. Navigation Architecture Overhaul

The most significant UX improvement was the complete refactoring of the application's entry flow to support reliable Deep Linking:

*   **Root State Pattern**: Removed the `SplashScreen` and its `SplashViewModel` from the Jetpack Navigation graph. The splash is no longer a "destination" but a **global loading state**.
*   **Navigation.kt Refactor**: The `NavHost` is now conditionally rendered. The app first determines the user's session status via `MainViewModel`. The `NavHost` only initializes once the destination (`Auth` or `Dashboard`) is certain.
*   **Backstack Integrity**: By removing the Splash from the graph, we eliminated the "navigation loops" where clicking a notification could trap the user between the Splash and the Alert UI.
*   **Intent-Aware Delay**: Updated `MainActivity` to detect if the app was started via a Deep Link. If so, the artificial splash delay is bypassed to provide an **instant-to-content** experience for notifications.

---

## 3. Architectural Hardening (The "MeteoMarto" Way)

### System Abstraction & Safety
*   **The Checker Pattern**: Fully isolated Android-specific logic (SDK version checks, Manifest permissions) inside `AndroidPermissionChecker`.
*   **Stateless UI Transformation**: Refactored `CityWeatherScreen` into a Stateful "Wiring" Composable and a pure Stateless "Content" Composable.
*   **Zero Leakage**: The UI now communicates via functional requirements (e.g., `getRequiredPermissions()`) rather than technical Android strings.

### Clean Code Standards
*   **Magic Literal Prohibition**: Eliminated all hardcoded strings. URI schemes (`package`), notification IDs, and Log Tags now live in a centralized `AppConstants.kt`.
*   **Import Governance**: Performed a global cleanup of **wildcard imports (`.*`)** to prevent namespace pollution and improve IDE performance.
*   **Canonical Order**: Standardized all Use Cases to follow the official Kotlin modifier order (`suspend operator`).

---

## 4. Governance & Intelligence Promotion

The feature drove significant updates to the **`android-ai-workflow-foundation`**, making these rules standard for all future projects:

| Skill / Doc | Evolution Summary |
| :--- | :--- |
| **`AGENTS.md`** | Defined **Core Architectural Mandates**: Permission Abstraction, Stateless UI First, and Constant Centralization. |
| **`kotlin-style`** | Added strict rules for **Magic Literal Prohibition**, **Wildcard Import Forbid**, and **Modifier Ordering**. |
| **`dependency-manager`** | Formalized rules for KSP synchronization and dependency grouping. |
| **`git-governance`** | Hardened conventions for atomic, layer-specific commit conventions. |

---

## 5. Final Verification Results

*   ✅ **Logic**: 100% pass rate in `:usecases` and `:data` unit tests.
*   ✅ **Clean UI**: Verified that pressing 'Back' from the Dashboard closes the app immediately without returning to a hidden Splash screen.
*   ✅ **Field Test**: Verified full reactive loop: Firebase Update -> Expedited Worker -> Push Notification -> Deep Link -> Proper Exit.

**MM-01 is now verified as the new architectural benchmark for the MeteoMartoCompose project.**
