# Navigation Architecture and Scaffolds Simplification

The goal is to eliminate redundancy in visual containers (nested `Scaffold`s) and centralize adaptive navigation logic in a single point, following the **"Stateless Screen Mandate"** principle.

## User Review Required

> [!IMPORTANT]
> This change will move the visual responsibility of the app shell (top bars, bottom bars, and FAB) from individual screens to the global orchestrator (`Navigation.kt`). Screens will become "naked".

> [!WARNING]
> We will remove the `ProvideAppBarTitle` pattern based on ViewModels to replace it with a pattern based on typed routes (Kotlin Serialization), which is cleaner and more predictable in Jetpack Compose.

## Proposed Changes

---

### Global Adaptive Shell

#### [MODIFY] [Navigation.kt](file:///Users/AlbertMartorell/Development/Android/MeteoMartoCompose/app/src/main/java/com/martorell/albert/meteomartocompose/ui/navigation/Navigation.kt)
- Replace manual `Scaffold` with `MmNavigation` as the root component.
- Wrap the single `NavHost` with this `MmNavigation`.
- Configure `topBar` and `floatingActionButton` from here, delegating to `AppState`.

#### [MODIFY] [TopAppBarCustom.kt](file:///Users/AlbertMartorell/Development/Android/MeteoMartoCompose/app/src/main/java/com/martorell/albert/meteomartocompose/ui/navigation/shared/TopAppBarCustom.kt)
- Remove `TopAppBarViewModel` and calls to `SideEffect`.
- Refactor to obtain the title and actions directly from the current route (`navController.currentBackStackEntry`).

#### [MODIFY] [AppState.kt](file:///Users/AlbertMartorell/Development/Android/MeteoMartoCompose/app/src/main/java/com/martorell/albert/meteomartocompose/ui/AppState.kt)
- Add logic to map routes to titles (Resources) and navigation icons centrally.

---

### Screens Migration (Phase 5)

#### [MODIFY] [LoginScreen.kt](file:///Users/AlbertMartorell/Development/Android/MeteoMartoCompose/app/src/main/java/com/martorell/albert/meteomartocompose/ui/screens/auth/LoginScreen.kt)
- [DELETE] Internal call to `MmNavigation`.
- Return screen content directly applying global `PaddingValues`.

#### [MODIFY] [SignUpScreen.kt](file:///Users/AlbertMartorell/Development/Android/MeteoMartoCompose/app/src/main/java/com/martorell/albert/meteomartocompose/ui/screens/auth/SignUpScreen.kt)
- [DELETE] Internal call to `MmNavigation`.

#### [MODIFY] [TermsScreen.kt](file:///Users/AlbertMartorell/Development/Android/MeteoMartoCompose/app/src/main/java/com/martorell/albert/meteomartocompose/ui/screens/auth/TermsScreen.kt)
- [DELETE] Internal call to `MmNavigation`.

#### [MODIFY] [CityWeatherScreen.kt](file:///Users/AlbertMartorell/Development/Android/MeteoMartoCompose/app/src/main/java/com/martorell/albert/meteomartocompose/ui/screens/city/CityWeatherScreen.kt)
- [DELETE] Use of `ProvideAppBarTitle` and `ProvideAppBarAction`.

---

### Documentation and Workflow

#### [MODIFY] [WORKFLOW_MM_02_DESIGN_SYSTEM.md](file:///Users/AlbertMartorell/Development/Android/MeteoMartoCompose/docs/features/design_system/WORKFLOW_MM_02_DESIGN_SYSTEM.md)
- Update Phase 5 to reflect the new naked screen architecture.

## Verification Plan

### Automated Tests
- Run `gradle assembleDebug` to verify no signature breaks.
- Review existing navigation tests.

### Manual Verification
- Check that margins (paddings) are correct on mobile (Bottom Bar) and Tablet (Nav Rail).
- Verify that `TopAppBar` title changes correctly when navigating between CityWeather and Favorites.
- Ensure Login screen does not show unnecessary navigation bars or titles.
