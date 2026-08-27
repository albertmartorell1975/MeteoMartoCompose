# Implementation Plan — MM-02: Android Design System (Comprehensive Edition)

Evolve the MeteoMartoCompose UI into a modular, **reusable, testable, and highly composable** Material 3 Design System. This plan adheres to **Senior/Principal Android Engineer standards**, strictly following **Google's Accessibility (A11y), Material 3 Expressive (Android 16 style), RTL, and Adaptive UI best practices**.

## User Review Required

> [!IMPORTANT]
> **Phased Execution**: Approval of this plan **ONLY triggers the execution of Phase 1 (Infrastructure & Governance)**. We will stop and wait for explicit approval before proceeding to any other phase.
> **Dependency Integrity**: Any modification to `libs.versions.toml` MUST strictly follow the **`dependency-manager`** skill.
> **Zero M3 Leaks**: After Phase 5, feature screens will have NO direct imports from `androidx.compose.material3`.
> **Documentation Root**: All feature documentation will reside in `docs/features/design_system/`.

---

## 1. Pillars of the Design System

### A. Architectural "Super-Hoisting"
- **Stateless by Contract**: Every `MM*` component MUST be stateless. No internal `mutableStateOf`. All state is hoisted to the caller.
- **Slot API Pattern**: Components MUST use "Slots" (Composable lambdas) for content. They don't know about the data, only the layout.
- **Modifier Requirement**: Every component signature MUST start with `modifier: Modifier = Modifier` to allow external layout control (margins, weight) without breaking encapsulation.

### B. Adaptive Navigation & RTL
- **Adaptive Evolution**: Migration to **`NavigationSuiteScaffold`**. Centralized logic that automatically switches between Bottom Bar (Portrait/Compact) and Navigation Rail (Landscape/Expanded).
- **RTL Native**: Absolute prohibition of `left/right`. Use `start/end` exclusively. Directional icons (arrows) MUST be mirrored.

### C. Visual & Accessibility Excellence
- **Scaling**: Mandatory **`sp`** for all fonts. Support for **200% scaling** with native support for Android 14+ **non-linear scaling** and vertical reflow.
- **Expressive Interactions**: Use of **Variable Fonts (Roboto Flex)** for weight-axis feedback and **Spring Physics** for natural motion.
- **Modern Support**: Full **Dynamic Color** (Material You) on Android 12+ and **Edge-to-Edge** (WindowInsets) compliance on Android 15+.

---

## 2. Expected Visual Behavior (Testing Matrix)

To avoid "hallucinations" and ensure consistency, the following behaviors are defined:

| Feature | Android 12+ (API 31) | Android 11 & Lower |
| :--- | :--- | :--- |
| **Color Palette** | **Dynamic**: Material You (Wallpaper-based). | **Static**: `MMColors` Reference Tokens. |
| **Font Scaling** | **Non-linear (A14+)**: Adaptive scaling via `sp`. | **Linear**: Uniform scaling. |
| **Edge-to-Edge** | **Native (A15+)**: Mandatory system bar transparency. | **Custom**: `WindowInsets` logic. |
| **Navigation** | **Adaptive**: Automatic Bar vs. Rail transition. | **Fixed**: Logic-based transition. |
| **Motion (Spring)**| **Standard**: Available on all versions via Compose library. Aligned with A16 style. | **Standard**: Same behavior. Honors "Remove animations" setting. |

---

## 3. Proposed Changes

### [Phase 1: Infrastructure & Governance]
**Goal**: Set up the environment following the `dependency-manager` skill.
- **`libs.versions.toml`**: Add Roborazzi (Screenshot), Dokka (Docs), DataStore (A11y Persistence), and Material3 Adaptive.
- **Skill Evolution**:
    - [MODIFY] [workflow-feature/SKILL.md]: Update instructions to mandate the "Consult AGENTS.md" step in all generated workflows.
    - [MODIFY] [workflow-initializer/SKILL.md]: Update templates to reflect this governance mandate.
- **Skill Creation**: Create `.agents/skills/design-system-governance.md`.
- **Feature Roadmap**: Create `docs/features/design_system/WORKFLOW_MM_02_DESIGN_SYSTEM.md`.
- **Technical Record**: Create `docs/features/design_system/MM-02-Design-System.md`.

### [Phase 2: Foundation & Tokens (The "Truth")]
**Goal**: Implement the Design Tokens DNA.
- **Tokens**: `MMSpacing.kt` (Reference scale), `MMColors.kt` (Semantic roles), `MMTypography.kt` (**Roboto Flex Variable Font**).
- **Pinch-to-zoom**: `MMDensityProvider.kt` with **Jetpack DataStore** persistence for user-driven scaling.

### [Phase 3: Core Stateless Components (`MM*`)]
**Goal**: Build reusable atoms and molecules.
- **`MMButton.kt`**: Slot-based, **Spring-motion**, focus **weight-axis feedback**.
- **`MMTextField.kt`**: RTL mirroring, A11y labels, semantic error states.
- **`MMText.kt`**: Semantic wrapper mapping roles (Display, Headline, Body) to typography tokens.
- **`MMNavigation.kt`**: Adaptive scaffold implementation wrapping `NavigationSuiteScaffold`.

### [Phase 4: Testing & CI/CD]
**Goal**: Automate quality assurance.
- **Snapshot Matrix**: Roborazzi verification for **16 permutations** (LTR/RTL x Port/Land x 1.0/2.0 Scale x Light/Dark).
- **CI/CD**: `.github/workflows/design-system-ci.yml` (verify Screenshots + Dokka build).

### [Phase 5: Global Migration & Comprehensive Audit]
**Goal**: Refactor the entire app UI and certify quality.
- **Refactor**: Replace legacy `shared/` components and eliminate ALL `androidx.compose.material3` leaks in feature screens.
- **Comprehensive Quality Audit**:
    1. **Visual/System**: Android 15 Edge-to-Edge compliance and WindowInsets check.
    2. **UX & Adaptive**: Verify scroll state preservation and layout reflow during rotations.
    3. **TalkBack Walkthrough**: Manual focus traversal check on all migrated screens.
    4. **Localization & Robustness**: Verify UI stability with **Pseudo-localization** (long string overflow check) and RTL mirroring.
    5. **Performance**: Recomposition count audit via Layout Inspector.

---

## 4. Technical Implementation Guardrails
1. **Icon Mirroring**: Mandatory for directional icons.
2. **Zero Hardcoded Heights**: Prohibition of `height(fixed_dp)` in text-bearing components to support 200% scaling.
3. **LocalContentColor**: Standardized usage for iconography.
4. **Super-Hoisting**: Mandatory statelessness and `Modifier` as the first optional parameter.

---

## 5. Draft Contents for Review

### Draft: `design-system-governance.md` (Skill)
```markdown
---
name: design-system-governance
description: Codifies A11y, RTL, Adaptive, and Reusability standards.
---
# Design System Governance Rules

## 1. Component Architecture (Super-Hoisting)
- **Statelessness**: Every component MUST be stateless (state hoisting).
- **Slot API Pattern**: Use slots for content.
- **Modifier Requirement**: Every component signature MUST include `modifier: Modifier = Modifier` as its first optional parameter.

## 2. Accessibility (A11y) & Low Vision
- **Contrast**: Mandatory **WCAG AA** (4.5:1 ratio).
- **Scaling**: Support 200% font scaling without clipping. Use **sp** for all text.
- **Pinch-to-zoom**: Implement in-app scaling via `Modifier.transformable` with DataStore persistence.
- **Semantics**: Merge descendants for complex items. Group cues beyond color.

## 3. RTL & Adaptive Support
- **RTL**: Absolute prohibition of `left/right`. Use `start/end` exclusively. Mirror directional icons.
- **Adaptive**: Use `NavigationSuiteScaffold`. Support Landscape/Portrait reflow.
- **Insets**: Components must handle `WindowInsets` for Edge-to-Edge (Android 15+).
```

---

## 6. Verification Plan (Phase 1 focus)
- **Dependency Integrity**: Verify `libs.versions.toml` sync without conflicts.
- **Skill Registration**: Verify `design-system-governance.md` is registered.
- **Dokka Init**: Verify `./gradlew dokkaHtml` generates base library documentation.
- **Documentation Paths**: Verify all files are in `docs/features/design_system/`.
