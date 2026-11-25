# MeteoMartoCompose Design System (`Fnd*` & `Mm*`)

This document is the unified developer and AI reference for the **MeteoMarto Atmospheric KISS Design System** (`ui.designsystem.foundation` and `ui.designsystem.components`), aligned with `DESIGN.md`, `docs/ui/meteomarto_design_contract.txt`, `design-system-governance`, `compose-modifier-and-layout-style`, and `edge-to-edge`.

---

## 1. Architecture & Token Layering

All styling flows deterministically through four strict layers — **never** via inline hex `Color(0x...)` literals, raw `dp`/`sp` magic numbers, or Android 12+ OS wallpaper `dynamicColor` overrides:

```text
FndColorPrimitives (internal, 70 unique hex literals — single source of truth)
        │
        ▼
SimpleWeatherPalette (5 authoritative KISS 4-color palettes + resolveWeatherPalette)
        │
        ▼
FndColors (semantic surface, container, outline, error, thermal hot/cold tokens — WCAG AA >= 4.5:1)
        │
        ▼
MeteoMartoTheme -> FndColors.toMaterialColorScheme() + FndTypography + FndShapes
        │
        ▼
Stateless Mm* Components (11 reusable Material 3 wrappers under ui.designsystem.components)
```

Access active tokens inside any `@Composable` exclusively via `MeteoMartoTheme`:
- `MeteoMartoTheme.palette` (`SimpleWeatherPalette`)
- `MeteoMartoTheme.colors` (`FndColors`)
- `MeteoMartoTheme.typography` (`FndTypography`)
- `MeteoMartoTheme.shapes` (`FndShapes`)
- `MeteoMartoTheme.spacing` (`FndSpacing`)
- `MeteoMartoTheme.dimensions` (`FndDimensions`)

---

## 2. Foundation Tokens (`ui.designsystem.foundation`)

### 2.1 Five Authoritative KISS Palettes (`WeatherPalette.kt`)

Deterministic evaluation precedence in `resolveWeatherPalette(condition, isNight, isExtremeAlert)`:

```text
ExtremeAlert  >  Night  >  Sunny  >  Rain  >  DayBaseline
```

| Palette | Trigger Rule | `background` | `surface` | `onSurface` | `primary` | Mode |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **`DayBaselinePalette`** | Static neutral flows (Splash, Auth, Terms, Favorites, Error) & `BASELINE` daytime | `#F8F9FA` | `#FFFFFF` | `#1A1C1E` | `#4A627A` | Light |
| **`SunnyPalette`** | OpenWeather `800` (`SUNNY`) daytime | `#FFF8F0` | `#FFFFFF` | `#2D2314` | `#E68A00` | Light |
| **`RainPalette`** | OpenWeather `2xx`/`3xx`/`5xx`/`6xx` (`RAIN`) daytime | `#EBF2F7` | `#F4F8FB` | `#13202C` | `#2C5282` | Light |
| **`NightPalette`** | Nocturnal state (`isNight == true`) when not in `ExtremeAlert` | `#0E1419` | `#161C21` | `#E1E2E5` | `#9D8DF1` | Dark |
| **`ExtremeAlertPalette`** | **Current location only** when `temperature > RemoteConfigThreshold` | `#701A1A` | `#8B2323` | `#FFFFFF` | `#B5C4FF` | Dark |

### 2.2 Full Material 3 Container, Surface & Thermal Roles (`FndColors.kt` & `DesignSystem.kt`)

`FndColors.toMaterialColorScheme()` maps every Material 3 role (`primaryContainer`, `onPrimaryContainer`, `secondaryContainer`, `onSecondaryContainer`, `surfaceVariant`, `onSurfaceVariant`, `surfaceContainerLowest`..`surfaceContainerHighest`, `outline`, `outlineVariant`, `error`, `errorContainer`) from `FndColors` so zero Material 3 primitives (`FilledTonalButton`, `NavigationSuiteScaffold`, `OutlinedTextField`, `TopAppBar`, `FAB`) fall back to default M3 purple.

- **WCAG AA Guarantee (`>= 4.5:1`)**: Verified programmatically by `WeatherThemePaletteTest` across all 5 palettes (plus custom light/dark fallbacks) for text on `background`/`surface`, `onPrimary`/`primary`, container pairs, `onSurfaceVariant` on `surfaceContainerHigh`, inverted `MmSnackbarHost` pairs (`background` on `onSurface`, `primaryContainer` on `onSurface`), and thermal accents (`hot` / `cold`) rendered on `surface` inside `MmCard`.
- **Single-Source Color Primitives**: All `70` unique raw hex literals live strictly in `internal object FndColorPrimitives` (`0` duplicates, `0` unused, `0` inline `Color(0x...)` outside `FndColorPrimitives.kt`).

### 2.3 Typography, Shapes, Spacing, Dimensions & Motion

- **Typography (`FndTypography.kt`)**: Powered exclusively by variable **Roboto Flex** (`R.font.roboto_flex`) across weights `Normal (400)`, `Medium (500)`, `SemiBold (600)`, and `Bold (700)`. All 15 Material 3 scales enforce a strict **`>= 12.sp`** floor (`labelSmall = 12.sp / 16.sp`), bridged cleanly via `toMaterialTypography()`.
- **Shapes (`FndShapes.kt`)**:
  - `input = RoundedCornerShape(8.dp)` (`MmTextField`, `MmSnackbarHost`)
  - `card = RoundedCornerShape(20.dp)` (`MmCard`, `MmWeatherInfo` telemetry card)
  - `dialog = RoundedCornerShape(28.dp)` (`MmDialog`)
  - `pill = CircleShape` (`MmPrimaryButton`, `MmSecondaryButton`, `MmTertiaryButton`, navigation active indicators)
- **Spacing (`FndSpacing.kt`)**: 4dp baseline grid (`extraSmall = 4.dp`, `small = 8.dp`, `medium = 16.dp`, `large = 24.dp`, `extraLarge = 32.dp`).
- **Dimensions (`FndDimensions.kt`)**: `buttonHeight = 48.dp`, `minTouchTarget = 48.dp`, `maxContentWidth = 600.dp`, `authFormWidth = 300.dp`, `logoSize = 180.dp`, `weatherIconSize = 90.dp`, `errorIconSize = 120.dp`, `borderStrokeWidth = 1.dp`.
- **Motion (`FndMotion.kt`) & Density (`FndDensityProvider.kt`)**: Spring physics (`SpringExpressive` / `SpringStandard`) for expressive button weight feedback and error shake animations (`ErrorShake`); `FndDensityProvider` isolates user font scaling (`fontScale`) without distorting structural density.
- **Bundled Graphic Assets (`app/src/main/res/drawable/`)**: `ic_logo.webp`, `ic_warning.webp`, `ic_error_forecast.webp` (sourced exclusively from `docs/ui/resources/`; screen mockups under `docs/ui/screens/` are never converted into drawables).

---

## 3. Stateless `Mm*` Component Catalog (`ui.designsystem.components`)

Every `Mm*` component is stateless, accepts `modifier: Modifier = Modifier` as its first optional parameter (applied first to the root with **zero** hardcoded root `.fillMaxSize()` / `.fillMaxWidth()`), uses `start`/`end` RTL alignment, and supports 200% font scaling without clipping.

| Component | File | Purpose & Key Guarantees |
| :--- | :--- | :--- |
| **`MmPrimaryButton` / `MmSecondaryButton` / `MmTertiaryButton`** | `MmButton.kt` | Pill-shaped (`FndShapes.pill`) filled, tonal, and text buttons with `heightIn(min = 48.dp).minimumInteractiveComponentSize()`, spring-animated Roboto Flex weight on press, and explicit `MeteoMartoTheme.colors.*`. |
| **`MmCard`** | `MmCard.kt` | Non-clickable and clickable (`onClick`) surface container using `FndShapes.card` (`20.dp`), `surface`/`onSurface`, `1.dp` `outlineVariant` border, and `minimumInteractiveComponentSize()` on the clickable overload. |
| **`MmDialog`** | `MmDialog.kt` | Modal dialog (`FndShapes.dialog = 28.dp`, optional `icon` and `properties`) backed by `surface`/`onSurface`; paired with `internal MmDialogPreviewLayout` (`FlowRow` action bar for 200% font scaling) for window-free Roborazzi capture. |
| **`MmErrorState`** | `MmErrorState.kt` | Centered error state bounded by `maxContentWidth` (`600.dp`), defaulting to `R.drawable.ic_error_forecast` (`errorIconSize = 120.dp`), optional `description`, and localized `R.string.retry_action` CTA (`MmPrimaryButton`). |
| **`MmLoadingOverlay`** | `MmLoadingOverlay.kt` | Full-surface modal scrim (`SCRIM_ALPHA = 0.5f`) consuming pointer input, displaying `CircularProgressIndicator`, and exposing localized TalkBack `R.string.loading_content_description` semantics. |
| **`MmNavigation`** | `MmNavigation.kt` | Adaptive navigation shell (`NavigationSuiteScaffold`: bottom bar `< 600dp`, leading rail `>= 600dp`) with `@Immutable MmNavigationItem`, optional `isItemSelected`, explicit palette colors, `maxLines = 1`/`Ellipsis` labels, and `.consumeWindowInsets(innerPadding)`. |
| **`MmScaffold`** | `MmScaffold.kt` | Root themed `Surface` defaulting to `MeteoMartoTheme.colors.background` / `onSurface` with caller-owned sizing. |
| **`MmSnackbar` / `MmSnackbarHost` / `showMmSnackbar`** | `MmSnackbar.kt` | `rememberUpdatedState`-safe snackbar trigger helper plus themed `MmSnackbarHost` (`FndShapes.input`, inverted `onSurface`/`background` + `primaryContainer` action accent). |
| **`MmText`** | `MmText.kt` | Exclusive typographic boundary exposing all **15/15** Material 3 scales (`DisplayLarge`..`LabelSmall`) backed by `FndTypography` (`Roboto Flex >= 12.sp`). |
| **`MmTextField`** | `MmTextField.kt` | Single-root `OutlinedTextField` (`FndShapes.input = 8.dp`, explicit `OutlinedTextFieldDefaults.colors(...)`, optional `singleLine`) with spring horizontal error-shake guarded against `LocalInspectionMode.current`. |
| **`MmWeatherInfo`** | `MmWeatherInfo.kt` | Weather telemetry header (`weatherIconSize = 90.dp`, Coil `.crossfade(enable = !LocalInspectionMode.current)`) + `MmCard`-wrapped telemetry rows guaranteeing `>= 4.60:1` WCAG AA contrast for `hot`/`cold` across all 5 palettes. |

---

## 4. Accessibility, Adaptive & Edge-to-Edge Standards

- **Universal RTL**: Exclusively `start`/`end` padding and alignment (`@MmPreview` verifies `locale = "ar"` on every component).
- **Font Scaling (200%)**: Minimum `12.sp` typography floor; containers use `heightIn(min = ...)`, `wrapContentHeight()`, `FlowRow`, and `TextOverflow.Ellipsis` (`@MmPreview` verifies `fontScale = 2.0f`).
- **Touch Targets**: Interactive buttons and clickable cards enforce `>= 48dp x 48dp` via `Modifier.minimumInteractiveComponentSize()`.
- **Edge-to-Edge**: `MmNavigation` chains `.padding(innerPadding).consumeWindowInsets(innerPadding)` to prevent double inset consumption in nested `Scaffold` layouts.
- **Responsive Layout**: Compact (`< 600dp`) uses bottom bar and `16.dp` margins; Medium/Expanded (`>= 600dp`) switches to a leading Navigation Rail with `600.dp` `maxContentWidth` (`@MmDevicePreview` verifies `Phone` + `Tablet`).

---

## 5. Verification Workflow (Roborazzi & Dokka)

Design System visual regression uses **Roborazzi** (`RoborazziPreviewTester` with `CompareOptions(changeThreshold = 0.01f)` for cross-OS font anti-aliasing stability) capturing **`60` deterministic golden PNGs** under `app/src/test/snapshots/`:

- `@MmPreview` (`4` PNGs): `Light Mode` (`DayBaseline`), `Dark Mode` (`Night`), `RTL` (`ar`), `Large Font` (`2.0f`) — all with `showBackground = true`.
- `@MmDevicePreview` (`2` PNGs): `Phone` (`411x891dp`), `Tablet` (`1280x800dp@240dpi`).
- `@MmDynamicPalettesPreview` + `MmDynamicPalettesShowcase` (`1` vertical PNG per themed component; `heightDp = 1600` on `MmWeatherInfoPalettesPreview`): renders `SunnyPalette`, `RainPalette`, and `ExtremeAlertPalette` together without permutation explosion.

```bash
# Verify 60/60 Design System Roborazzi snapshots (JDK 21)
./gradlew :app:verifyRoborazziPreDebug --no-daemon

# Regenerate golden PNGs after intentional token or Mm* component changes
./gradlew :app:recordRoborazziPreDebug --no-daemon

# Generate Design System Dokka HTML reference (output: app/build/dokka/design-system)
./gradlew :app:dokkaGenerateHtml -PgenerateDocs -PdesignSystemDocs --no-daemon

# Canonical quality gate (pure JVM unit tests + Roborazzi + Android Lint)
./gradlew testPreDebugUnitTest lintPreDebug --parallel --build-cache --configuration-cache
```
