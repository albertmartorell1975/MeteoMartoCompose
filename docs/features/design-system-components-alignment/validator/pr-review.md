# PR Review Log — `design-system-components-alignment` (PR #13)

## PR Review Round #1

- **PR**: [#13](https://github.com/albertmartorell1975/MeteoMartoCompose/pull/13) (`feature/design-system-components-alignment` -> `feature/MM-02-design-system`)
- **Reviewed Commit**: `263a24fb4fd586811fc6e46e503220e76e02ac18`
- **Date**: `2026-11-26`
- **Reviewer**: `@albertmartorell1975`

### Actionable Code Finding (1)

#### Finding 1 — Decouple `MmNavigation` from `NavDestination` (Option B confirmed by developer)
- **File**: `app/src/main/java/com/martorell/albert/meteomartocompose/ui/designsystem/components/MmNavigation.kt`
- **Line**: `69`
- **Diff Hunk**:
  ```kotlin
  fun MmNavigation(
      items: List<MmNavigationItem>,
      currentRoute: NavDestination?,
      onItemClick: (Any) -> Unit,
      modifier: Modifier = Modifier,
      isItemSelected: (MmNavigationItem) -> Boolean = { item ->
          currentRoute?.hierarchy?.any { it.hasRoute(item.route::class) } == true
      },
  ```
- **Reviewer Instruction**: *"Per què tenim aquesta lògica a la firma de la classe?"* -> Developer explicitly selected **Option B**: completely decouple `MmNavigation` from AndroidX `NavDestination` by removing `currentRoute: NavDestination?` (and all `androidx.navigation.*` imports) from `MmNavigation.kt`, making `isItemSelected: (MmNavigationItem) -> Boolean` a required parameter before `modifier: Modifier = Modifier` (strict `design-system-governance` §1 order: `items`, `isItemSelected`, `onItemClick`, `modifier`, optional slots, trailing `content`), and moving the `currentDestination?.hierarchy?.any { it.hasRoute(item.route::class) } == true` predicate to the sole caller in `app/src/main/java/com/martorell/albert/meteomartocompose/ui/navigation/Navigation.kt`.
- **Additional Notes**:
  - Update `MmNavigationPreview()` in `MmNavigation.kt` to drop `currentRoute = null` while preserving `isItemSelected = { it.route == "city" }` (`0` visual change; all `60/60` Roborazzi PNGs remain `unchanged`).
  - Remove the now-stale `@param currentRoute` KDoc line from `MmNavigation.kt` so Dokka passes with `0` warnings during implementation/validation (`workflow-documenter` will review and refine `MmNavigation` KDoc in Stage 4).
  - Do **NOT** touch `docs/features/design-system-components-alignment/spec.md` (No Planner Rule) or `docs/design-system.md` (updated by `workflow-orchestrator` during Stage 5 Consolidation).
  - Zero new `@Test` methods needed.

### Clarifications Answered (No Code Changes Requested)
1. `MmButton.kt:53` — Confirmed `modifier.heightIn(min = buttonHeight).minimumInteractiveComponentSize()` complies with `compose-modifier-and-layout-style` §2–§3 and `design-system-governance` §2 (`>= 48dp` + 200% font scale growth).
2. `MmCard.kt:112` — Confirmed `Surface(color = MeteoMartoTheme.colors.background)` is preview-only so `MmCardPreview` renders on the active theme background (`#F8F9FA` / `#0E1419`) instead of Android's default white preview canvas.
3. `MmDialog.kt:101` — Confirmed `FlowRow` in `MmDialogPreviewLayout` wraps action buttons cleanly at 200% font scaling without horizontal clipping (matching Material 3 `AlertDialogFlowRow`).
4. `MmPreview.kt:45` — Confirmed `name` and `group` organize Android Studio Preview tabs and produce deterministic Roborazzi snapshot filenames.
5. `LoginScreen.kt:271` — Confirmed explicit `MmLoadingOverlay(modifier = Modifier.fillMaxSize())` at caller sites follows `compose-modifier-and-layout-style` §3 after removing hardcoded root `.fillMaxSize()` from `MmLoadingOverlay`.
