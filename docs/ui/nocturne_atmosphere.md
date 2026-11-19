---
name: Nocturne Atmosphere
colors:
  surface: '#0e1419'
  surface-dim: '#0e1419'
  surface-bright: '#343a3f'
  surface-container-lowest: '#090f14'
  surface-container-low: '#161c21'
  surface-container: '#1a2025'
  surface-container-high: '#242b30'
  surface-container-highest: '#2f363b'
  on-surface: '#dde3ea'
  on-surface-variant: '#c4c6cc'
  inverse-surface: '#dde3ea'
  inverse-on-surface: '#2b3136'
  outline: '#8e9196'
  outline-variant: '#44474c'
  surface-tint: '#bac8dc'
  primary: '#bac8dc'
  on-primary: '#243141'
  primary-container: '#0d1b2a'
  on-primary-container: '#768497'
  inverse-primary: '#525f71'
  secondary: '#5bd5fc'
  on-secondary: '#003543'
  secondary-container: '#00a3c8'
  on-secondary-container: '#003341'
  tertiary: '#e0b6ff'
  on-tertiary: '#4b007d'
  tertiary-container: '#2c004b'
  on-tertiary-container: '#ae59f3'
  error: '#ffb4ab'
  on-error: '#690005'
  error-container: '#93000a'
  on-error-container: '#ffdad6'
  primary-fixed: '#d6e4f9'
  primary-fixed-dim: '#bac8dc'
  on-primary-fixed: '#0f1c2c'
  on-primary-fixed-variant: '#3a4859'
  secondary-fixed: '#b7eaff'
  secondary-fixed-dim: '#5bd5fc'
  on-secondary-fixed: '#001f28'
  on-secondary-fixed-variant: '#004e61'
  tertiary-fixed: '#f2daff'
  tertiary-fixed-dim: '#e0b6ff'
  on-tertiary-fixed: '#2e004e'
  on-tertiary-fixed-variant: '#6b00af'
  background: '#0e1419'
  on-background: '#dde3ea'
  surface-variant: '#2f363b'
typography:
  display-hero:
    fontFamily: Manrope
    fontSize: 72px
    fontWeight: '300'
    lineHeight: 80px
    letterSpacing: -0.03em
  display-hero-mobile:
    fontFamily: Manrope
    fontSize: 56px
    fontWeight: '300'
    lineHeight: 64px
    letterSpacing: -0.03em
  headline-lg:
    fontFamily: Manrope
    fontSize: 36px
    fontWeight: '600'
    lineHeight: 44px
    letterSpacing: -0.02em
  headline-lg-mobile:
    fontFamily: Manrope
    fontSize: 28px
    fontWeight: '600'
    lineHeight: 36px
    letterSpacing: -0.02em
  headline-md:
    fontFamily: Manrope
    fontSize: 22px
    fontWeight: '500'
    lineHeight: 28px
  body-lg:
    fontFamily: Geist
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  body-md:
    fontFamily: Geist
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 20px
  label-lg:
    fontFamily: Geist
    fontSize: 14px
    fontWeight: '500'
    lineHeight: 20px
    letterSpacing: 0.01em
  label-md:
    fontFamily: Geist
    fontSize: 12px
    fontWeight: '500'
    lineHeight: 16px
    letterSpacing: 0.04em
  metric-caption:
    fontFamily: Geist
    fontSize: 11px
    fontWeight: '600'
    lineHeight: 14px
    letterSpacing: 0.06em
rounded:
  sm: 0.5rem
  DEFAULT: 1rem
  md: 1.5rem
  lg: 2rem
  xl: 3rem
  full: 9999px
spacing:
  gutter: 1rem
  gutter-tablet: 1.5rem
  gutter-desktop: 2rem
  margin: 1.25rem
  margin-tablet: 2rem
  margin-desktop: 3rem
  space-xs: 0.25rem
  space-sm: 0.5rem
  space-md: 1rem
  space-lg: 1.5rem
  space-xl: 2rem
  space-2xl: 3rem
---

## Brand & Style

This design system reimagines mobile meteorological interfaces through an atmospheric, modern luxury lens. Drawing inspiration from contemporary Android Jetpack Compose paradigms, it transforms flat weather statistics into a visceral environmental experience. The mood shifts seamlessly across midnight backdrops, luminous frost surfaces, dynamic sky gradients, and dramatic warning states.

The aesthetic fuses **Glassmorphism** with **Atmospheric Minimalism**:
- **Deep Immersion:** A midnight atmospheric canvas (`#0D1B2A`) anchors the experience, reducing eye fatigue and giving meteorological states room to radiate.
- **Translucent Depth:** Generous frosted-glass surfaces and specular highlights provide layered elevation without heavy, distracting drop shadows.
- **Dynamic Adaptability:** Fluid lighting and contextual accents respond to real-time sky conditions (clear night, cloud density, rainfall precipitation, and severe thermal alerts).
- **Tactile Softness:** Fluid 28dp pill and capsule containers lend a modern, approachable feel to scientific weather metrics.

## Colors

The color palette centers around an oceanic midnight foundation, heightened by translucent glass tokens and vibrant environmental accents:

- **Primary (`#0D1B2A`):** The core midnight canvas. Sets an immersive, darkened horizon for data visualization and navigation bars.
- **Secondary (`#4CC9F0`):** Electric azure accent representing atmospheric moisture, brisk winds, and active navigation indicators.
- **Tertiary (`#7209B7`):** Deep twilight ultraviolet used for energetic secondary states, active pills, and high-altitude transitions.
- **Neutral (`#E0E6ED`):** Mist white used for high-contrast primary typography and prominent metric glyphs.
- **Severe Alert (`#8A1414`):** Rich, deep crimson retained from emergency thermal alerts, softened with dark vignette gradients to maintain elegance while signaling danger.
- **Precipitation Blue (`#3A86FF`):** Specific hue dedicated to rainfall indices, barometric drops, and moisture meters.

Surfaces employ a translucent glass hierarchy (`#1B2A4A80` with a 1px inner edge of `#FFFFFF1F`) over radial backdrop glows to communicate atmospheric depth without visual clutter.

## Typography

The typographic hierarchy combines the balanced, sculptural geometry of **Manrope** for primary locations, condition titles, and hero readings with the hyper-refined, monoline precision of **Geist** for dense meteorological telemetry and system labels.

- **Hero & Large Numbers:** Manrope at Light weight (300) ensures substantial, oversized temperatures (e.g., 28.3°) remain airy and modern rather than blocky or oppressive.
- **City & Structural Headers:** Manrope SemiBold (600) conveys grounded authority for location names and modal headers.
- **Telemetry & Labels:** Geist (Regular 400 and Medium 500) provides monospaced-like visual balance for parameters like pressure, humidity, min/max ranges, and bottom bar destinations.
- **Numerical Consistency:** Tabular figures are enforced for metric readings, ensuring real-time forecast counter updates do not trigger horizontal reflow.

## Layout & Spacing

This design system uses a responsive fluid grid optimized for handheld mobile ergonomics with smooth expansion onto large surfaces:

- **Mobile (Base):** 4-column layout with `1.25rem` outer canvas margin and `1rem` column gutters. Primary actions (such as "Update forecast" and Favorite toggles) are positioned in comfortable thumb zones within the lower 40% of the viewport.
- **Tablet & Foldables (600dp+):** 8-column layout. The Weather City View morphs from a single continuous vertical column into a dual-pane card grid: hero weather card on the left pane, multi-metric telemetry breakdown on the right pane.
- **Desktop (1024dp+):** 12-column layout with centered maximum content width of `1200px` and `3rem` margins.

Vertical rhythm relies strictly on the 4px base increment (`space-xs` through `space-2xl`). Metric tiles maintain generous internal breathing room (`space-lg`) to preserve the feeling of open sky.

## Elevation & Depth

Visual hierarchy does not rely on harsh drop shadows. Instead, it is achieved through layered **Glassmorphism**, surface opacity modulation, and diffuse ambient back-glows:

- **Base Ambient Canvas (Level 0):** Solid `#0D1B2A` with dynamic SVG/Canvas atmospheric radial gradients shifting softly based on weather context.
- **Frosted Container (Level 1):** Acrylic glass tile with `backdrop-filter: blur(24px)`, surface color `#1B2A4A4D` (30% opacity), and a delicate perimeter stroke of `#FFFFFF1F` (12% opacity white).
- **Raised Interactive Cards & Modals (Level 2):** Frosted glass surface `#22335C66` (40% opacity), `backdrop-filter: blur(32px)`, bordered with `#FFFFFF33`, cast against an ultra-diffuse 32px blur shadow with an electric cyan or rain-blue tint at 15% opacity (`0 16px 32px -8px #4CC9F026`).
- **Floating Controls & Quick Actions (Level 3):** Navigational floating action buttons (FABs) and active alert banners carry an assertive glow bloom: `0 8px 24px 0 #4CC9F040`.
- **Severe Weather Alert Overlay:** Full-bleed gradient veil transitioning from `#8A1414E6` at the apex down to `#2B0707F2`, frosted across a 40px glass blur with high-contrast mist iconography.

## Shapes

In harmony with contemporary Android Jetpack Compose guidelines, the design system utilizes generous pill and capsule forms (`roundedness: 3`).

- **Base Metric Cells & Cards:** Styled with `24px` to `28px` corner radiuses (`rounded-xl` equivalent), establishing a soft, organic feel akin to smoothed river stones.
- **Buttons & Contextual Chips:** Pure full-pill radiuses (9999px / `rounded-full`), reinforcing effortless tapability and comfort.
- **Text Inputs & Surface Sheets:** `24px` rounded containers for input fields; bottom sheets and alert dialogs carry `28px` top corner contours.
- **Segmented & Navigation Tabs:** Rounded capsule pills (`32px`) encapsulating icons and label combinations without hard visual seams.

## Components

### Buttons & Quick Actions
- **Primary CTA ("Update forecast"):** Full-pill container with a horizontal gradient from `#4CC9F0` to `#3A86FF`, high-contrast dark text (`#0D1B2A`), Manrope SemiBold typography, and a subtle glowing aura on touch.
- **Secondary / Ghost Buttons ("Terms and conditions", "Create a new user"):** Transparent glass pill with a 1px border (`#FFFFFF26`), neutral mist text, and a slight glass wash on hover/press.
- **Floating Action Button (Favorite Toggle):** 56dp capsule or circular tile in the bottom-right viewport with frosted acrylic material, housing an animated heart icon that transitions between mist stroke and glowing turquoise fill.

### Metric Chips & Data Displays
- **Atmospheric Metric Tiles:** Translucent glass cards grouping related telemetry (Temperature, High/Low, Pressure, Rain). Each reading features a subtle icon, Geist metric caption above, and a bold Manrope data value.
- **Condition Badges:** Compact pill chips containing a micro weather glyph and descriptive text (e.g., "broken clouds") with 12px horizontal padding and 6px vertical padding.

### Lists & City Cards (Favorites)
- **Favorite City Cards:** Wide horizontal glass slats with 24dp rounded corners. Features the city name in Manrope Medium, real-time temperature hero on the right, condition descriptor chip below, and a swipable or explicit trash icon button bathed in subtle crimson-accented hover glow.
- **Divider:** Never use opaque solid divider lines; use empty spacing or 1px hairline gradients (`transparent -> #FFFFFF1A -> transparent`).

### Form Inputs (Authentication Flow)
- **Glass Text Fields:** 56dp tall containers with 24dp rounded corners, filled with `#1B2A4A40`, bordered by `#FFFFFF26`. Placeholders in muted Geist text; active focused states transition the border to glowing `#4CC9F0` with a 2px outer ambient blur. Password visibility eye toggles sit flush on the trailing edge.

### Weather Alert Banners & Dialogs
- **Alert Container:** Urgent yet polished card incorporating a deep crimson backdrop blur (`#8A1414D9`), warning triangle badge, crisp white headline, and an acknowledge button ("Understood") rendered as a tinted mist pill.
- **Dismiss Control:** Top-right circular close button (36dp) with frosted white glass and crisp centered icon.

### Navigation Bar
- **Bottom Navigation Dock:** Floating translucent glass dock lifted 16dp above the screen bottom, housing pill-shaped destination indicators with smooth animated sliding selection pills (`#4CC9F026` indicator fill).