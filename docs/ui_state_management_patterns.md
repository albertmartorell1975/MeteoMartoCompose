# UI State Management Patterns: Data Class vs. Sealed Class

This document summarizes the two primary approaches for managing UI State in modern Android applications with Jetpack Compose, along with the recommendation of the hybrid model for production-grade apps.

---

## 1. Data Class Approach (State as a Stream)
The state is defined as a single immutable object with multiple properties representing every element on the screen.

### ✅ Pros
- **Partial Updates**: Thanks to the `.copy()` method, it is very easy to change a single property (e.g., `isLoading = true`) without affecting the rest of the state.
- **Data Continuity**: Existing data (like a list of cities) is preserved while the state transitions to "loading", preventing UI flickers.
- **Additive States**: Ideal for screens where multiple elements can coexist (e.g., background data + alert dialog + loading overlay).

### ❌ Cons
- **Impossible States**: It allows invalid combinations, such as having `error != null` and `isLoading = true` simultaneously.
- **Logic in UI**: The view becomes cluttered with checks like `if (state.property != null)` or `if (state.isLoading)`.

---

## 2. Sealed Class Approach (State Machine)
The state is defined as a set of mutually exclusive situations. You are either in one or the other.

### ✅ Pros
- **Type Safety**: Guarantees that only one main state is displayed at a time. No risk of impossible states.
- **Clean UI**: The view uses a single `when(state)` block that the compiler forces to be exhaustively handled.
- **Single Source of Truth (Logic)**: All decisions about the screen's phase reside entirely within the ViewModel.

### ❌ Cons
- **Verbosity**: You must rebuild the entire object for any change. There is no `.copy()` between different branches of a `sealed class`.
- **Loss of Context**: When moving from `Success` to `Loading`, the data in the `Success` branch is lost.
- **Rigidity**: Hard to manage when you want to show a temporary dialog without closing the background state.

---

## 3. The Hybrid Approach (Senior Recommendation)

This model combines the best of both worlds: it uses a **`data class`** for global flags and a **`sealed interface`** for the main content.

### Modeling Example
```kotlin
data class CityWeatherUiState(
    // Mutually exclusive main content (Loading, Success, Error)
    val content: CityContent = CityContent.Loading,
    
    // Additive flags (can happen anytime without closing the main content)
    val isLoggingOut: Boolean = false,
    val showFab: Boolean = false,
    val highTempAlert: Boolean = false
)

sealed interface CityContent {
    object Loading : CityContent
    data class Success(val city: CityWeatherDomain) : CityContent
    data class Error(val error: CustomError) : CityContent
}
```

### UI Implementation
```kotlin
@Composable
fun CityWeatherContent(state: CityWeatherUiState) {
    Box {
        // 1. Main Content Management (Clean and safe)
        when (val content = state.content) {
            is CityContent.Loading -> CircularProgress()
            is CityContent.Success -> WeatherLayout(content.city)
            is CityContent.Error -> ErrorLayout(content.error)
        }

        // 2. Additive Layers (Simple with booleans)
        if (state.isLoggingOut) LogoutDialog()
        if (state.highTempAlert) AlertIcon()
    }
}
```

---

## Decision Summary

| Situation | Suggested Approach |
| :--- | :--- |
| Very simple screens (single button or text) | `sealed class` |
| Screens with persistent data and background loading | `data class` |
| **Complex Production Applications** | **Hybrid Model** |

> [!TIP]
> For the **MeteoMartoCompose** project, the hybrid model is the best choice to clean up the climate screen UI without losing the ease of managing GPS and logout dialogs.
