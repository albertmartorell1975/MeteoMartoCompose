# Post-PR Review Feedback: navigation-shell-unification

## PR Review Round #1

- **PR**: `#15` (`https://github.com/albertmartorell1975/MeteoMartoCompose/pull/15`)
- **Commit Reviewed**: `4055c7d99b5b02528593a521043d659a2daeab5a`
- **Review Submitted**: `2026-11-27T10:27:58Z` (`albertmartorell1975` — `COMMENTED`, 5 unresolved inline threads + user prompt documentation request)

### Finding 1 (Code Fix — `workflow-implementer`)
- **File / Line**: `app/src/main/java/com/martorell/albert/meteomartocompose/ui/navigation/shared/TopAppBarCustom.kt#L26` (previews at `#L77-L98`)
- **Reviewer Comment**: *"Vec poca diferència visual entre dark mode i light mode. Comprovat a la preview"*
- **Root Cause**: `TopAppBarCustom` intentionally uses `containerColor = Color.Transparent` so it blends over the active atmospheric screen background. However, `TopAppBarCustomTopLevelPreview()` and `TopAppBarCustomDetailPreview()` invoke `TopAppBarCustom(...)` directly inside `MeteoMartoTheme { ... }` without wrapping it in `Surface(color = MeteoMartoTheme.colors.background)`. As a result, Android Studio's `@MmPreview` / `@MmDevicePreview` renders the transparent bar over the default preview host background instead of painting `DayBaselinePalette` (`#F8FAFC`) in Light Mode and `NightPalette` (`#0B1120`) in Dark Mode.
- **Required Fix**: Wrap `TopAppBarCustom(...)` inside `Surface(color = MeteoMartoTheme.colors.background) { ... }` in both `TopAppBarCustomTopLevelPreview()` and `TopAppBarCustomDetailPreview()` (matching `MmDynamicPalettesShowcase` and Design System preview conventions). Keep production `TopAppBarCustom` signature and `containerColor = Color.Transparent` untouched. Zero new `@Test` methods; zero edits to `spec.md` or Roborazzi snapshots (`60/60`).

### Finding 2 (Durable Documentation — Stage 5 Consolidation by `workflow-orchestrator`)
- **Source**: User prompt (*"Potser ja existeix, però hauriem de crear o actualitzar la documentació explicant els components que fan possible la shell de navegació."*) + 4 architectural review questions on PR #15:
  1. `TopAppBarCustom.kt:27` — *"Per què hem eliminat AppState?"* (Stateless decoupling from `AppState`/`NavDestination`/`hiltViewModel`, unidirectional data flow, previewability, and decoupling from `CityWeatherViewModel`).
  2. `AppShellState.kt:13` — *"Pots explicar la diferència entre AppShellState i AppState?"* (Pure JVM-testable chrome/selection mapper vs. `@Stable` Compose navigation orchestrator).
  3. `Navigation.kt:56` — *"quin és l'objectiu?"* (`LaunchedEffect(currentBackStackEntry?.id)` resetting shared `TopAppBarState` `heightOffset`/`contentOffset` so collapsed scroll state does not bleed across destinations).
  4. `Navigation.kt:35` — *"Diferpencia amb collectAsState?"* (`collectAsStateWithLifecycle()` pauses upstream Flow collection in `STOPPED`/background state vs. `collectAsState()` which collects continuously).
- **Required Action (Orchestrator Stage 5 only — NOT `workflow-implementer` or `workflow-documenter`)**: Create `docs/navigation-shell.md` documenting every component of the navigation shell (`Navigation.kt`, `AppState`, `AppShellState` / `AppShellChrome`, `Destinations.kt`, `TopLevelRoute`, `HomeNavGraph.kt`, `authSubGraph.kt`, `TopAppBarCustom.kt`, `MmNavigation.kt`), architecture diagram, `AppState` vs `AppShellState` responsibilities, `CityForecast` route contract, chrome matrix, `TopAppBarCustom` decoupling, scroll reset, and `collectAsStateWithLifecycle()`, and link `docs/navigation-shell.md` from `README.md`.
