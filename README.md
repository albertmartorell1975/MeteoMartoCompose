# 🌤️ MeteoMartoCompose

MeteoMartoCompose is a modern Android weather application built with Kotlin and Jetpack Compose.

The project was originally created as a playground to experiment with modern Android development practices, architecture and libraries.

The app retrieves weather information for a selected location and allows users to save favorite cities to check their forecasts.

Currently, the repository is used as a real-world playground to evaluate how a structured AI workflow can support feature development, architecture and engineering practices.

---

## 🏗️ Architecture & Tech Stack

The project follows Clean Architecture and SOLID principles, with a focus on separation of concerns, maintainability and testability.

### Android
- Kotlin
- Jetpack Compose
- Android Jetpack
- Coroutines & Flow

### Architecture
- Clean Architecture
- MVVM
- SOLID
- Repository pattern
- Dependency Injection

### Libraries & Services
- Hilt
- Room
- Retrofit
- Firebase
- OpenWeather API

### Local Configuration (`local.properties`)
OpenWeather credentials are injected declaratively via `secrets-gradle-plugin` with a committed placeholder fallback in `local.defaults.properties` (`API_KEY_VALUE=DEFAULT_OPENWEATHER_API_KEY`) so CI and fresh clones compile cleanly. To fetch live weather data locally, add your personal key to git-ignored `local.properties`:
```properties
API_KEY_VALUE=your_openweather_api_key
```

---

## 🔐 Demo Access

To try MeteoMartoCompose, you can create a new account using Firebase Authentication.

If you prefer not to register, a default demo account is available:

**Email:** `meteomarto@gmail.com`  
**Password:** `123456`

This account is intended for demonstration purposes and allows you to explore the application without creating a new account.

---

## 🤖 AI-Assisted Development Experiments

MeteoMartoCompose is also used to evaluate the Android AI Workflow Foundation, a structured workflow for integrating AI-assisted development into a real Android project.
The workflow is based on the [Android AI Workflow Foundation](https://github.com/albertmartorell1975/android-ai-workflow-foundation), an evolving foundation for structuring and supervising AI-assisted development within an existing IDE.

Each experiment is developed in an isolated Git branch so that the application remains the primary subject while the AI workflow can be evaluated independently.

### Current experiment

**MM-02: Design system architecture and governance**

**Branch**: `feature/MM-02-design-system`

The goal of MM-02 is not simply to create a reusable UI component library. It is to evaluate how an AI-assisted engineering workflow can help define and enforce Design System architecture, governance, consistency and automated visual validation in a modern Android project. **To maintain these high architectural standards, all AI contributions are strictly guided and evaluated by a custom `design-system-governance` skill.** See the unified Design System reference at [docs/design-system.md](docs/design-system.md).

> ⚠️ **This feature is currently under development and has not been merged into `develop`.**

#### Stateless Design System components
Material 3 components are wrapped behind explicit application-level `Mm*` APIs (`MmButton`, `MmCard`, `MmDialog`, `MmErrorState`, `MmLoadingOverlay`, `MmNavigation`, `MmScaffold`, `MmSnackbar`/`MmSnackbarHost`, `MmText`, `MmTextField`, `MmWeatherInfo`), establishing controlled component boundaries and avoiding UI-specific business state inside the Design System.

#### Controlled typography
Typography is centralized through `MmText` (all 15 Material 3 scales, `>= 12.sp`), with Roboto Flex variable fonts used to provide consistent and scalable typographic behavior.

#### Design tokens and foundations
Five deterministic KISS weather palettes (`DayBaseline`, `Sunny`, `Rain`, `Night`, `ExtremeAlert`), full Material 3 container/surface roles, spacing, shapes, dimensions, motion, and typography are defined through dedicated `Fnd*` tokens, reducing direct styling decisions across individual application screens.

#### Automated visual regression testing
Roborazzi is used for JVM-based screenshot testing and visual regression detection across a pragmatic `60`-PNG multipreview baseline (`@MmPreview`, `@MmDevicePreview`, and `@MmDynamicPalettesPreview`) covering all 5 KISS palettes, RTL, 200% font scaling, and Phone/Tablet form factors without requiring every permutation to be executed on a physical device.

#### Responsive UI and adaptive navigation
Material 3 Adaptive and NavigationSuiteScaffold are integrated to evaluate responsive layouts and navigation patterns across different screen sizes and form factors.

### Past Experiments

**MM-01: High-Temperature Push Notification**

**Branch**: `feature/MM-01-high-temp-notification`

The first AI-assisted experiment focused on implementing a high-temperature push notification feature. The feature was successfully implemented using the Android AI Workflow Foundation and subsequently merged into `develop`.

This experiment established the initial workflow for applying structured AI assistance to a real application feature and provided the foundation for the broader architectural experiment now being conducted with MM-02.

---

## 📌 Repository Status

The repository currently contains the following relevant branches:

- **`develop`** — Current development branch containing the completed MM-01 High-Temperature Push Notification experiment.
- **`feature/MM-02-design-system`** — Active work-in-progress branch containing the current MM-02 Design System experiment. It has not yet been merged into `develop`.

*MeteoMartoCompose is the Android project; the Android AI Workflow Foundation is the development process being experimented with.*