# UI State Management Patterns: Data Class vs. Sealed Class

This document summarizes the two primary approaches for managing UI State in modern Android applications with Jetpack Compose, along with the recommendation of the **Hybrid Model** for production-grade apps.

---

## 1. Data Class Approach (State as a Stream)
The state is defined as a single immutable object with multiple properties representing every element on the screen.

### ✅ Pros
- **Partial Updates**: Easy to change a single property without affecting the rest.
- **Additive States**: Ideal for overlapping UI (data + dialog + loader).

### ❌ Cons
- **Impossible States**: Allows invalid combinations (e.g., `error != null` AND `isLoading = true`).

---

## 2. Sealed Class Approach (State Machine)
The state is defined as a set of mutually exclusive situations.

### ✅ Pros
- **Type Safety**: Guarantees only one main state is displayed. No impossible states.
- **Clean UI**: Uses a single `when(state)` block.

---

## 3. The Hybrid Approach (MeteoMarto Standard)

This model combines the best of both worlds: it uses a **`data class`** for global coordination and a **`sealed interface`** for the mutually exclusive content phases.

### Mandate: Data Continuity & Process Death Resilience
Following `viewmodel-architecture-governance`:
- **`isLoading` must be an additive boolean flag**, not a branch of the sealed interface, so existing content stays visible behind a loader overlay during refresh.
- **Process Death survival**: persist route arguments (ID-only), active form drafts, filters, and critical dialog targets via `SavedStateHandle` or `rememberSaveable` (and Room SSOT), never in volatile RAM singletons or `onCleared()`. Verify `SavedStateHandle` ViewModels with pure JVM `SavedStateHandle(mapOf(...))` restoration tests.

### Modeling Example
```kotlin
data class CityWeatherUiState(
    // 1. Mutually exclusive primary phases (Sealed Interface)
    val content: CityContent = CityContent.Initial,
    
    // 2. Additive layers and global coordination (Data Class properties)
    val isLoading: Boolean = false,
    val isLoggingOut: Boolean = false,
    val highTempAlert: Boolean = false
)

sealed interface CityContent {
    data object Initial : CityContent
    data object NeedsLocation : CityContent
    data class Success(val city: CityWeatherDomain) : CityContent
    data class Error(val error: DomainError) : CityContent
}
```

### UI Implementation
```kotlin
@Composable
fun CityWeatherContent(state: CityWeatherUiState) {
    MmScaffold {
        // 1. Main Content Management (Clean and safe)
        when (val content = state.content) {
            is CityContent.Initial -> { /* Splash */ }
            is CityContent.NeedsLocation -> LocationWarning()
            is CityContent.Success -> WeatherLayout(content.city)
            is CityContent.Error -> ErrorLayout(content.error)
        }

        // 2. Additive Overlays (Always on top)
        if (state.isLoading) MmLoadingOverlay()
        if (state.isLoggingOut) LogoutDialog()
    }
}
```

---

## Decision Summary

| Situation | Suggested Approach |
| :--- | :--- |
| Very simple screens | `sealed class` |
| Screens with data continuity needs | **Hybrid Model** |

> [!TIP]
> Use the **Hybrid Model** with `isLoading: Boolean` to ensure a professional UX where data doesn't "flicker" out of existence during refresh cycles.
