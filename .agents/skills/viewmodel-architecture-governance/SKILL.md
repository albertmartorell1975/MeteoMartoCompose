---
name: viewmodel-architecture-governance
description: Unified architectural rules for ViewModels, focusing on the Passive Initialization Mandate and the Hybrid UI State Pattern.
---

# ViewModel Architecture Governance

This skill centralizes all architectural mandates for ViewModels to ensure consistency, testability, and resource efficiency.

## 1. Passive Initialization Mandate

The `ViewModel` constructor and `init` block MUST remain **passive**. It is strictly forbidden to launch coroutines or start data collection during construction.

### Rationale
- **Testability**: Mocks and test rules must be configured *before* any work starts. If `init` launches work, the test cannot control the starting conditions.
- **Resource Efficiency**: Avoid wasting CPU/Battery if the ViewModel is instantiated (e.g., in a backstack) but the UI is not yet visible.
- **Predictability**: Ensures the UI is observing the state before the first emission occurs, avoiding missed events.

### Strategies to stay passive:

#### A. For Imperative Actions (One-shot loads, manual triggers)
Expose a dedicated function (e.g., `onStart()` or `loadData()`) and trigger it from the UI.

**❌ BAD (Active constructor)**
```kotlin
class CityViewModel(private val repository: WeatherRepository) : ViewModel() {
    init {
        // ❌ Starts immediately upon instantiation. Hard to mock 'repository' in tests.
        viewModelScope.launch { repository.loadCurrentWeather() }
    }
}
```

**✅ GOOD (UI-driven)**
```kotlin
class CityViewModel(private val repository: WeatherRepository) : ViewModel() {
    fun onStart() {
        viewModelScope.launch { 
            _state.update { it.copy(isLoading = true) }
            repository.loadCurrentWeather() 
            _state.update { it.copy(isLoading = false) }
        }
    }
}

// In the Screen Composable (Wiring):
LaunchedEffect(Unit) {
    viewModel.onStart() // ✅ Triggered explicitly when the UI is ready
}
```

#### B. For Reactive Data Streams (Database observers, Config flows)
Avoid manual collection in `init`. Use the **Declarative State Pattern** with the `stateIn` operator.

**❌ BAD (Manual collection in init)**
```kotlin
class StreamViewModel(private val repository: DataRepository) : ViewModel() {
    private val _data = MutableStateFlow(emptyList<Item>())
    val data = _data.asStateFlow()

    init {
        // ❌ Manual management, redundant boilerplate, starts immediately.
        viewModelScope.launch {
            repository.observeData().collect { _data.value = it }
        }
    }
}
```

**✅ GOOD (Declarative stateIn)**
```kotlin
class StreamViewModel(repository: DataRepository) : ViewModel() {
    // ✅ Flow is converted to StateFlow lazily. 
    // It only starts when the UI subscribes.
    val data: StateFlow<List<Item>> = repository.observeData()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // ✅ Rotation-safe
            initialValue = emptyList()
        )
}
```

### Benefits for Unit Testing
With a passive constructor, your tests become deterministic:
```kotlin
@Test
fun `when screen starts, data is loaded`() = runTest {
    // 1. Setup mocks (Possible because init is passive)
    coEvery { repository.loadData() } returns successResult
    
    // 2. Instantiate ViewModel
    val viewModel = MyViewModel(repository)
    
    // 3. Trigger work manually
    viewModel.onStart()
    
    // 4. Verify results
    assertEquals(expectedState, viewModel.state.value)
}
```

---

## 2. UI State Modeling (The Hybrid Pattern)

To ensure a robust interface, use the **Hybrid Model**: a `data class` for global coordination and a `sealed interface` for mutually exclusive content.

### Mandate: Separate "Base Content" from "Additive UI Layers"

1. **Mutually Exclusive States** (Loading, Success, Error): Use a `sealed interface`.
2. **Additive States** (Dialogs, Overlays, FAB visibility): Use `Boolean` flags in the parent `data class`.

### Detailed Implementation Example

#### A. Modeling at the ViewModel
```kotlin
data class ScreenUiState(
    // 1. The primary phase of the screen (Exclusive)
    val content: MainContent = MainContent.Loading,
    
    // 2. Transitory or overlapping UI elements (Additive)
    val isLoggingOut: Boolean = false,
    val showFab: Boolean = false
)

sealed interface MainContent {
    object Loading : MainContent
    data class Success(val city: CityWeatherDomain) : MainContent
    data class Error(val type: CustomError) : MainContent
}
```

#### B. Implementation at the UI (Stateless Content)
```kotlin
@Composable
fun CityWeatherContent(state: ScreenUiState) {
    Box(modifier = Modifier.fillMaxSize()) {
        // 1. Handle primary content with a clean 'when'
        when (val content = state.content) {
            is MainContent.Loading -> CircularProgressIndicator()
            is MainContent.Success -> WeatherDetails(content.city)
            is MainContent.Error -> ErrorView(content.type)
        }

        // 2. Overlap additive elements based on independent flags
        if (state.isLoggingOut) {
            LogoutDialog(onConfirm = { /* ... */ })
        }
    }
}
```
