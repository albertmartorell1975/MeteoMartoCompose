---
name: Atmospheric Baseline
colors:
  surface: '#fdf7ff'
  surface-dim: '#ded8e0'
  surface-bright: '#fdf7ff'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f8f2fa'
  surface-container: '#f2ecf4'
  surface-container-high: '#ece6ee'
  surface-container-highest: '#e6e1e9'
  on-surface: '#1d1b20'
  on-surface-variant: '#494551'
  inverse-surface: '#322f35'
  inverse-on-surface: '#f5eff7'
  outline: '#7a7582'
  outline-variant: '#cac4d2'
  surface-tint: '#6650a4'
  primary: '#4e378a'
  on-primary: '#ffffff'
  primary-container: '#6650a4'
  on-primary-container: '#dfd2ff'
  inverse-primary: '#cfbdff'
  secondary: '#625b71'
  on-secondary: '#ffffff'
  secondary-container: '#e8def9'
  on-secondary-container: '#686177'
  tertiary: '#594300'
  on-tertiary: '#ffffff'
  tertiary-container: '#765a00'
  on-tertiary-container: '#fad475'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#e8ddff'
  primary-fixed-dim: '#cfbdff'
  on-primary-fixed: '#21005d'
  on-primary-fixed-variant: '#4e388a'
  secondary-fixed: '#e8def9'
  secondary-fixed-dim: '#ccc2dc'
  on-secondary-fixed: '#1e192b'
  on-secondary-fixed-variant: '#4a4358'
  tertiary-fixed: '#ffdf96'
  tertiary-fixed-dim: '#e8c266'
  on-tertiary-fixed: '#251a00'
  on-tertiary-fixed-variant: '#5a4400'
  background: '#fdf7ff'
  on-background: '#1d1b20'
  surface-variant: '#e6e1e9'
typography:
  display-lg:
    fontFamily: Roboto Flex
    fontSize: 57px
    fontWeight: '400'
    lineHeight: 64px
    letterSpacing: -0.25px
  display-md:
    fontFamily: Roboto Flex
    fontSize: 45px
    fontWeight: '400'
    lineHeight: 52px
    letterSpacing: 0px
  display-sm:
    fontFamily: Roboto Flex
    fontSize: 36px
    fontWeight: '400'
    lineHeight: 44px
    letterSpacing: 0px
  headline-lg:
    fontFamily: Roboto Flex
    fontSize: 32px
    fontWeight: '400'
    lineHeight: 40px
    letterSpacing: 0px
  headline-md:
    fontFamily: Roboto Flex
    fontSize: 28px
    fontWeight: '400'
    lineHeight: 36px
    letterSpacing: 0px
  headline-sm:
    fontFamily: Roboto Flex
    fontSize: 24px
    fontWeight: '400'
    lineHeight: 32px
    letterSpacing: 0px
  title-lg:
    fontFamily: Roboto Flex
    fontSize: 22px
    fontWeight: '400'
    lineHeight: 28px
    letterSpacing: 0px
  title-md:
    fontFamily: Roboto Flex
    fontSize: 16px
    fontWeight: '500'
    lineHeight: 24px
    letterSpacing: 0.15px
  title-sm:
    fontFamily: Roboto Flex
    fontSize: 14px
    fontWeight: '500'
    lineHeight: 20px
    letterSpacing: 0.1px
  body-lg:
    fontFamily: Roboto Flex
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
    letterSpacing: 0.5px
  body-md:
    fontFamily: Roboto Flex
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 20px
    letterSpacing: 0.25px
  body-sm:
    fontFamily: Roboto Flex
    fontSize: 12px
    fontWeight: '400'
    lineHeight: 16px
    letterSpacing: 0.4px
  label-lg:
    fontFamily: Roboto Flex
    fontSize: 14px
    fontWeight: '500'
    lineHeight: 20px
    letterSpacing: 0.1px
  label-md:
    fontFamily: Roboto Flex
    fontSize: 12px
    fontWeight: '500'
    lineHeight: 16px
    letterSpacing: 0.5px
  label-sm:
    fontFamily: Roboto Flex
    fontSize: 11px
    fontWeight: '500'
    lineHeight: 16px
    letterSpacing: 0.5px
rounded:
  sm: 0.5rem
  DEFAULT: 1rem
  md: 1.5rem
  lg: 2rem
  xl: 3rem
  full: 9999px
spacing:
  gutter: 1rem
  margin: 1rem
  margin-tablet: 1.5rem
  space-xs: 0.25rem
  space-sm: 0.5rem
  space-md: 1rem
  space-lg: 1.5rem
  space-xl: 2rem
---

## Brand & Style

The design system establishes a calm, highly legible, and disciplined Material 3 foundation engineered for real-time atmospheric observation and weather telemetry. Rooted in Google's Material You principles, it balances systematic precision with understated, organic clarity. The visual language evokes feelings of calm reassurance, environmental awareness, and effortless utility.

The aesthetic follows modern Corporate/Material 3 conventions: warm cloud canvases, soft tonal layers, crisp variable typography, and semantic thermal accents that make critical data instantly recognizable. Visual clutter, ornamental skeuomorphism, and heavy dropshadows are rejected in favor of generous whitespace, tonal surface elevation, pill-shaped interactions, and fluid typographic hierarchies.

## Colors

The core color strategy leverages Material 3 baseline tokens paired with explicit meteorological functional values:

- **Primary (`#6650a4`)**: Deep Violet drives focused interactive states, primary action pills, and active navigation indicators. In dark environments, it shifts to soft lavender (`#d0bcff`).
- **Secondary (`#625b71`)**: Muted Slate-Violet provides tonal contrast for auxiliary controls, segmented buttons, and secondary action surfaces. In dark environments, it shifts to pale mist (`#ccc2dc`).
- **Canvas & Surface (`#fffbfe`)**: Warm Cloud White serves as the default daylight backdrop, minimizing glare while preserving clean optical contrast. In dark mode, it transitions to Dusk Charcoal (`#1c1b1f`).
- **On-Surface Ink (`#1c1b1f`)**: Deep Charcoal ink provides accessible reading contrast for primary typography and symbols.
- **Thermal & Telemetry Roles**: Dedicated semantic slots handle meteorological feedback. Solar Heat Red (`#d32f2f`) indicates high-temperature readings and thermal warning states, while Atmospheric Sky Blue (`#1976d2`) identifies minimum temperature readouts, barometric low indicators, and precipitation levels.

## Typography

The design system exclusively adopts Roboto Flex across all hierarchy levels. Using variable parametric axes (`wght` 400 through 700, `wdth` 100, `opsz` 14), the type scale transitions from large display temperatures to compact telemetry labels without optical distortion.

- **Display Scales (`display-lg` to `display-sm`)**: Used for hero condition temperatures and large ambient city titles. Kept at Regular weight (400) to maintain crisp edges without heavy pixel density.
- **Headline Scales (`headline-lg` to `headline-sm`)**: Primary screen titles, weather conditions, alert headings, and full-screen state titles.
- **Title Scales (`title-lg` to `title-sm`)**: Medium-emphasis section headings, card headers, and list item city labels (often weighted up to Medium 500 or SemiBold 600 in active list states).
- **Body Scales (`body-lg` to `body-sm`)**: Telemetry metrics, sensor rows, dialog explanatory copy, and general text.
- **Label Scales (`label-lg` to `label-sm`)**: Navigation item labels, input field labels, button copy, and status chips.

All text containers must permit 200% font accessibility scaling without clipping by avoiding fixed container heights and using flexible, intrinsic height boundaries.

## Layout & Spacing

The layout model is built on an incremental 4dp/8dp mathematical rhythm:

- **Spacing Progression**: Micro-gaps between related badges and icons scale at `space-xs` (4px) and `space-sm` (8px). Structural padding inside cards, forms, and item rows uses `space-md` (16px). Inter-card spacing and section breaks use `space-lg` (24px) and `space-xl` (32px).
- **Canvas Margins & Gutters**: Compact screens utilize a base `margin` of 16px (`1rem`) and a 16px column gutter. Expanded layouts (tablets and foldables) step up to a 24px (`1.5rem`) margin.
- **Adaptive Reflow**:
  - **Compact (<600dp)**: Single-column centered telemetry hierarchy with anchored bottom navigation bar.
  - **Medium / Expanded (≥600dp)**: Content width is capped at 600dp (`max-content-width`) for centered readouts or auth containers, while top-level navigation adapts into a leading vertical Navigation Rail.
- **Directional Geometry**: Layout constraints must strictly use start and end alignment anchors to ensure seamless, native Right-to-Left (RTL) mirroring.

## Elevation & Depth

Visual hierarchy and spatial depth are achieved primarily through tonal layering and surface color gradation rather than heavy, artificial drop shadows:

- **Tonal Elevation**: Containers sit on the base surface (`#fffbfe`) using layered surface tiers (Surface Container Low, Surface Container, Surface Container High). Depth is signaled by subtle shifts in background tinting.
- **Ambient Lighting**: When floating action buttons, elevated cards, or modal dialogs require z-axis detachment, they use an ambient, low-opacity shadow tinted with the primary hue (`#6650a4` at 4% to 8% opacity) with extended blur radii (8dp to 24dp) and zero sharp directional offset.
- **Low-Contrast Outlines**: Non-elevated containers (such as weather metric cards or outline inputs) use crisp, low-contrast ghost borders (`onSurfaceVariant` at 12% opacity or tokenized border outlines) to delineate boundaries without visual weight.
- **Modal Scrims**: Dialogs and loading overlays utilize a 50% opacity neutral wash (`surface.copy(alpha = 0.5f)`), centering the user's attention while preserving spatial context behind the overlay.

## Shapes

The design system adopts a Pill-shaped (`3`) geometry baseline, reflecting the friendly, fluid curves of canonical Material 3:

- **Buttons & Interactive Pills**: Primary buttons, chips, and search bars employ full pill radius (`9999px` / `Full`), providing smooth, ergonomic tap affordances.
- **Dialogs & Major Sheets**: Modals and bottom sheets use extra-large radii (28px to 32px), creating soft, organic contours that frame complex data.
- **Cards & Data Modules**: Weather metric tiles, forecast list items, and container panels use rounded geometry (16px to 24px), preserving visual harmony with circular weather gauges and icons.
- **Input Fields**: Outlined text inputs feature soft 4px to 8px rounded corners to maintain structural alignment with horizontal form baselines.

## Components

### Buttons
- **Primary Button**: Solid fill using `primary` (`#6650a4`), content in `onPrimary` (`#ffffff`), full pill radius, minimum height of 48dp for touch-target standards, and minimum width of 200dp to avoid layout shift. On active press, typography dynamically animates from Regular (400) to SemiBold (600) via expressive spring motion.
- **Tonal / Secondary Button**: Filled tonal container using `secondaryContainer` with `onSecondaryContainer` text, matching the 48dp pill form factor.
- **Text Button**: Borderless interactive trigger with `primary` label and generous horizontal padding (`space-md`).

### Weather Telemetry Cards & Metric Badges
- **Weather Overview**: Centered hero layout featuring a 90dp vector condition icon, Display Large temperature readout, Headline Medium condition descriptor, and inline thermal status chips.
- **Telemetry Rows**: Clean key-value horizontal stacks separated by 16dp spacing. Values are semantically color-coded: Solar Heat Red (`#d32f2f`) for maximums and thermal alerts, Atmospheric Sky Blue (`#1976d2`) for minimum temperatures and precipitation levels.
- **Status Badges**: Pill-shaped micro-containers (24dp height) with low-opacity tonal background fills and high-contrast labels (`label-sm`).

### Text Inputs
- **Outlined Text Field**: Enforces full-width layout inside a bounded 300dp form width container. Outlined with low-contrast borders in resting state, shifting to 2dp `primary` border on focus.
- **Error Behavior**: Triggers semantic red tinting (`#b3261e`) and a rapid horizontal spring-shake animation upon invalid submission.

### Navigation
- **Bottom Navigation Bar**: Compact mobile navigation bar anchoring main views with pill-shaped indicator highlights around active icons and `label-sm` labels.
- **Adaptive Rail**: Automatically replaces the bottom bar on wide screens (≥600dp), docked to the leading start edge with vertically stacked items.

### Dialogs & Modals
- **Alert Dialog**: 28dp corner radius, padded internally with 24dp padding (`space-lg`), housing a Headline Medium title, Body Medium supporting text, and right-aligned pill action buttons.
- **Full-Screen Loading Overlay**: Translucent 50% surface scrim with centered `primary` CircularProgressIndicator, blocking touch events while background synchronization occurs.