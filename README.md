# 🌤️ MeteoMartoCompose

MeteoMartoCompose is a modern Android weather application built with Kotlin and Jetpack Compose.

The project was originally created as a playground to experiment with modern Android development practices, architecture and libraries.

The app retrieves weather information for a selected location and allows users to save favorite cities to check their forecasts.

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

---

## 🔐 Demo Access

To try MeteoMartoCompose, you can create a new account using Firebase Authentication.

If you prefer not to register, a default demo account is available:

**Email:** `meteomarto@gmail.com`  
**Password:** `123456`

This account is intended for demonstration purposes and allows you to explore the application without creating a new account.

---

## 🤖 AI-assisted Development Experiment

MeteoMartoCompose is also being used as a real Android project to experiment with an **AI-assisted development workflow**.

The workflow is based on the [Android AI Workflow Foundation](https://github.com/albertmartorell1975/android-ai-workflow-foundation), an evolving foundation for structuring and supervising AI-assisted development within an existing IDE.

The application itself is independent of this experiment. The AI workflow is being evaluated through the development of new features on top of the existing codebase.

### Current experiment

The first experiment is the development of a **high-temperature push notification** feature.

This work is currently being developed in:

**[`feature/MM-01-high-temp-notification`](https://github.com/albertmartorell1975/MeteoMartoCompose/tree/feature/MM-01-high-temp-notification)**

> ⚠️ **This feature is currently under development and has not been merged into `develop`.**

The feature is intended to notify users when the temperature exceeds a configurable threshold.

This branch is being used to evaluate the AI-assisted workflow through a complete development cycle, including planning, implementation, validation, testing and review.

The workflow itself is still evolving as the experiment progresses.

---

## 📍 Repository Status

- `develop` → current development version of the Android application.
- `feature/MM-01-high-temp-notification` → experimental feature and current AI-assisted development workflow evaluation.

The application and the AI workflow are intentionally kept separate:

**MeteoMartoCompose is the Android project; the Android AI Workflow Foundation is the development process being experimented with.**
