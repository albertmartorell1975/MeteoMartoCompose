---
name: MeteoMarto Atmospheric KISS Design System
description: Deterministic Material 3 weather design system combining the 5 KISS climate palettes, Stitch screen layouts, Roboto Flex variable typography (>= 12.sp), and adaptive Jetpack Compose components.
designAssets:
  sourceOfTruth:
    - path: docs/ui/meteomarto_design_contract.txt
      role: Canonical 5-palette color matrix and static vs. dynamic screen behavior rules
      status: authoritative
    - path: docs/ui/screens/
      role: 9 Stitch screen mockups (screen.png + code.html) defining layout hierarchy and visual structure
      status: authoritative
    - path: docs/ui/resources/
      role: Optimized WebP graphic assets (ic_logo.webp, ic_warning.webp, ic_error_forecast.webp)
      status: authoritative
  secondaryReferences:
    - path: docs/ui/atmospheric_baseline.md
      role: Material 3 spacing, shape, elevation, and motion guidance (colors superseded by contract)
      status: secondary
    - path: docs/ui/nocturne_atmosphere.md
      role: Night atmospheric depth and glass card inspiration (typography and non-contract colors superseded)
      status: inspiration
palettes:
  dayBaseline:
    background: "#F8F9FA"
    surface: "#FFFFFF"
    onSurface: "#1A1C1E"
    primary: "#4A627A"
  sunny:
    background: "#FFF8F0"
    surface: "#FFFFFF"
    onSurface: "#2D2314"
    primary: "#E68A00"
  rain:
    background: "#EBF2F7"
    surface: "#F4F8FB"
    onSurface: "#13202C"
    primary: "#2C5282"
  night:
    background: "#0E1419"
    surface: "#161C21"
    onSurface: "#E1E2E5"
    primary: "#9D8DF1"
  extremeAlert:
    background: "#701A1A"
    surface: "#8B2323"
    onSurface: "#FFFFFF"
    primary: "#B5C4FF"
semanticAccents:
  thermalHigh: "#D32F2F"
  thermalLow: "#1976D2"
  error: "#BA1A1A"
typography:
  fontFamily: Roboto Flex
  fontAsset: app/src/main/res/font/roboto_flex.ttf
  minimumFontSize: 12sp
  scales:
    displayLarge: { size: 57sp, lineHeight: 64sp, weight: 400 }
    displayMedium: { size: 45sp, lineHeight: 52sp, weight: 400 }
    displaySmall: { size: 36sp, lineHeight: 44sp, weight: 400 }
    headlineLarge: { size: 32sp, lineHeight: 40sp, weight: 400 }
    headlineMedium: { size: 28sp, lineHeight: 36sp, weight: 400 }
    headlineSmall: { size: 24sp, lineHeight: 32sp, weight: 400 }
    titleLarge: { size: 22sp, lineHeight: 28sp, weight: 400 }
    titleMedium: { size: 16sp, lineHeight: 24sp, weight: 500 }
    titleSmall: { size: 14sp, lineHeight: 20sp, weight: 500 }
    bodyLarge: { size: 16sp, lineHeight: 24sp, weight: 400 }
    bodyMedium: { size: 14sp, lineHeight: 20sp, weight: 400 }
    bodySmall: { size: 12sp, lineHeight: 16sp, weight: 400 }
    labelLarge: { size: 14sp, lineHeight: 20sp, weight: 500 }
    labelMedium: { size: 12sp, lineHeight: 16sp, weight: 500 }
    labelSmall: { size: 12sp, lineHeight: 16sp, weight: 500 }
spacing:
  xs: 4dp
  sm: 8dp
  md: 16dp
  lg: 24dp
  xl: 32dp
  maxContentWidth: 600dp
shapes:
  input: 8dp
  card: 20dp
  dialog: 28dp
  pill: 9999dp
---

# Design Direction — MeteoMartoCompose

## Overview

MeteoMartoCompose uses a calm, legible Material 3 weather design system built around deterministic **KISS dynamic theming** (`Fnd*` tokens and `Mm*` stateless components under `com.martorell.albert.meteomartocompose.ui.designsystem`). Static utility and list screens stay neutral (`DayBaselinePalette`), while the single-city forecast view dynamically adapts to live weather conditions and alerts.

## Existing Design Assets & Authority Hierarchy

When exported design documents conflict, agents MUST follow this exact precedence:

1. **Colors & Dynamic Behavior (Authoritative)**: `docs/ui/meteomarto_design_contract.txt`
   - Defines the 5 `SimpleWeatherPalette` palettes and which screens are static vs. dynamic.
   - Note: Implementation lives in `com.martorell.albert.meteomartocompose.ui.designsystem.foundation` (not `com.meteomarto.ui.theme`), and weather codes map to OpenWeather IDs (`800` = `SUNNY`; `200..232`, `300..321`, `500..531`, `600..622` = `RAIN`; all others = `BASELINE`).
2. **Screen Layouts & Visual Hierarchy (Authoritative)**: `docs/ui/screens/<screen-name>/{screen.png,code.html}`
   - Authoritative for layout composition, element placement, visual density, and screen structure.
   - Hardcoded Tailwind hex colors or alternate font stacks (`Inter`, `Manrope`, `Plus Jakarta Sans`) inside `code.html` are overridden by the tokens in this `DESIGN.md`.
3. **Graphic Resources (Authoritative)**: `docs/ui/resources/` (`ic_logo.webp`, `ic_warning.webp`, `ic_error_forecast.webp`).
4. **Spacing, Elevation & Atmospheric Craft (Secondary Reference)**: `docs/ui/atmospheric_baseline.md` and `docs/ui/nocturne_atmosphere.md`
   - Used for spacing rhythm (`4dp`/`8dp`), pill shapes, tonal layering, and subtle atmospheric depth. Their conflicting violet/nocturne palettes and `11px` font sizes are superseded by this document.

## Android Resource Conversion Policy

1. **Target Folder:** Only graphic assets located in `docs/ui/resources/` or `docs/features/<feature-id>/ui/resources/` (raw PNG/JPEG or pre-optimized WebP) MUST be placed as optimized `.webp` drawables in `app/src/main/res/drawable/ic_<name>.webp`.
2. **Exclusion:** Full-screen mockups (`docs/ui/screens/<screen>/screen.png` or `docs/features/<feature-id>/ui/screens/<screen>/screen.png`) and `code.html` exports are visual reference layout guides ONLY and MUST NOT be converted into Android drawable resources.

## Colors & Dynamic Theme Precedence

### Palette Matrix (`SimpleWeatherPalette`)

| Palette | Condition | Background | Surface | OnSurface | Primary |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **DayBaseline** | Clouds, mist, neutral/static screens | `#F8F9FA` | `#FFFFFF` | `#1A1C1E` | `#4A627A` |
| **Sunny** | Clear sky (OpenWeather `800`) | `#FFF8F0` | `#FFFFFF` | `#2D2314` | `#E68A00` |
| **Rain** | Storm, drizzle, rain, snow (`2xx`, `3xx`, `5xx`, `6xx`) | `#EBF2F7` | `#F4F8FB` | `#13202C` | `#2C5282` |
| **Night** | Nighttime (`isNight == true`) | `#0E1419` | `#161C21` | `#E1E2E5` | `#9D8DF1` |
| **ExtremeAlert** | Current location `temperature > remoteConfigThreshold` | `#701A1A` | `#8B2323` | `#FFFFFF` | `#B5C4FF` |

### Evaluation Order (Dynamic Screens Only)
1. `isExtremeAlert == true` -> `ExtremeAlertPalette`
2. `isNight == true` -> `NightPalette`
3. `condition == WeatherCondition.SUNNY` -> `SunnyPalette`
4. `condition == WeatherCondition.RAIN` -> `RainPalette`
5. Fallback -> `DayBaselinePalette`

Thermal telemetry values retain high-contrast semantic accents (`#D32F2F` for max/high heat, `#1976D2` for min/precipitation) adjusted for legibility on dark (`Night` / `ExtremeAlert`) surfaces.

## Typography

- **Single Font Family**: **Roboto Flex** (`app/src/main/res/font/roboto_flex.ttf`) across all screens and components.
- **Strict Minimum Size**: **`12.sp`** across all labels, captions, badges, and pills (`labelSmall` raised from `11px` to `12.sp` per `design-system-governance`).
- **Accessibility Scaling**: Every text container MUST support 200% font scaling (`fontScale = 2.0f`) via `wrapContentHeight()` or `minHeight` with zero clipped text.

## Core Screens & Reference Mapping

| Screen | Theme Mode | Palette | Stitch Reference (`docs/ui/screens/`) |
| :--- | :--- | :--- | :--- |
| `SplashScreen` | Static | `DayBaseline` | `meteomarto_splash_welcome/` |
| `LoginScreen` | Static | `DayBaseline` | `meteomarto_login_atmospheric/` |
| `SignUpScreen` (`Register`) | Static | `DayBaseline` | `meteomarto_registre_create_user/` |
| `TermsScreen` | Static | `DayBaseline` | `meteomarto_termes_i_condicions/` |
| `FavoritesScreen` (List & Empty State) | Static | `DayBaseline` | `meteomarto_llista_de_preferits_atmospheric/` |
| `CityForecastScreen` (Current City & Favorite Detail) | Dynamic | Resolved by precedence | `meteomarto_previsi_ciutat_atmospheric/` |
| `ErrorForecastScreen` | Static | `DayBaseline` | `meteomarto_error_previsi_ciutat/` |
| `HighTemperatureAlertScreen` | Dynamic (Alert) | `ExtremeAlert` | `meteomarto_alerta_meteorol_gica_atmospheric/` |
| `LogoutDialog` / Delete Favorite Dialog | Inherited | Active screen palette | `meteomarto_di_leg_log_out/` |

## Components & Layout Principles

- **Prefix Convention**: Foundation tokens use `Fnd*`; stateless reusable UI components use `Mm*` (`MmScaffold`, `MmButton`, `MmText`, `MmTextField`, `MmDialog`, `MmErrorState`, `MmLoadingOverlay`, `MmNavigation`, `MmSnackbar`, `MmWeatherInfo`).
- **Shapes**: Full pill (`9999dp`) for primary/secondary buttons, chips, and navigation indicators; `20dp`–`24dp` for weather and favorite cards; `28dp` for modals/dialogs; `8dp` for outlined text fields.
- **Motion**: Use Spring Physics (`FndMotion`) for interactive press/state transitions rather than linear tweens.
- **Responsive Baseline**: Compact (`<600dp`) uses bottom navigation bar and `16dp` margins; Medium/Expanded (`>=600dp`) switches to a leading Navigation Rail via `NavigationSuiteScaffold`, `24dp` margins, and `600dp` max centered content width.
- **Accessibility & RTL**: Universal `start`/`end` alignment (zero `left`/`right`), minimum `48dp x 48dp` interactive touch targets (`Modifier.minimumInteractiveComponentSize()`), and dual `@Preview` coverage (`@MmThemePreview` + `@MmDevicePreview`).

## Do's and Don'ts

- **DO** resolve all colors, spacing, shapes, and typography exclusively through `Fnd*` tokens and `SimpleWeatherPalette`.
- **DO** reuse `CityForecastScreen` for both current location and favorite city detail.
- **DO** keep Roborazzi snapshot verification focused exclusively on `ui.designsystem.components` `@Preview` composables.
- **DON'T** hardcode hex colors, `sp`/`dp` magic literals, or `11.sp` text sizes in screens or components.
- **DON'T** tint `FavoritesScreen` or Auth screens with a weather palette.
- **DON'T** convert `docs/ui/screens/*/screen.png` mockups into drawables.
