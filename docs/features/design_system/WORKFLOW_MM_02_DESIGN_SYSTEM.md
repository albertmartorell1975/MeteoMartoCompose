# Workflow: MM-02 Design System

This document serves as the granular checklist and architectural roadmap for the implementation of the MeteoMarto Design System.

## Phase 1: Infrastructure & Governance
- [x] **MANDATORY**: Consult `AGENTS.md` for quality and verification rules.
- [x] Create directory structure for Design System documentation.
- [x] Add Roborazzi, Dokka, DataStore, and Material3 Adaptive to `libs.versions.toml`.
- [x] Apply plugins and configure Roborazzi in `app/build.gradle.kts`.
- [x] Create `design-system-governance` skill.
- [x] Initialize `MM-02-Design-System.md` Technical Record and ADRs.
- [x] Gradle Sync and verification.
- [x] **MANDATORY**: Execute `compiler` skill verification suite.
- [x] **MANDATORY**: Request Commit & Push (Manual or via `git-governance` skill) before advancing.

## Phase 2: Foundation & Tokens (The "Truth")
- [x] **MANDATORY**: Consult `AGENTS.md` for style and logic standards.
- [x] Implement `FndSpacing.kt` (Reference scale: XS=4dp, S=8dp, M=16dp, L=24dp, XL=32dp).
- [x] Implement `FndColors.kt` (Semantic roles mapping Material 3 Expressive).
- [x] Implement `FndTypography.kt` (Mapping Display, Headline, Body using Roboto Flex Variable Font).
- [x] Create `DesignSystem.kt` (CompositionLocals provider).
- [x] Implement `FndDensityProvider.kt` with DataStore persistence for pinch-to-zoom.
- [x] **MANDATORY**: Execute `compiler` skill verification suite.
- [x] **MANDATORY**: Request Commit & Push (Manual or via `git-governance` skill) before advancing.

## Phase 3: Core Stateless Components (`Mm*`)
- [x] **MANDATORY**: Consult `AGENTS.md` for architectural mandates.
- [x] **MmPrimaryButton / MmSecondaryButton / MmTertiaryButton**: Slot-based, Spring-motion, weight-axis focus feedback.
- [x] **Multipreview Infrastructure**: Create reusable `@MmPreview` annotations for (Light/Dark x RTL x Scale).
- [x] **MmText**: Semantic text wrapper for typography tokens (The Typographic Boundary).
- [x] **MmTextField**: RTL mirroring, A11y labels, semantic error states.
- [x] **MmNavigation**: Adaptive scaffold wrapping `NavigationSuiteScaffold`.
- [x] **MANDATORY**: Execute `compiler` skill verification suite.
- [x] **MANDATORY**: Request Commit & Push (Manual or via `git-governance` skill) before advancing.

## Phase 4: Testing & CI/CD
- [x] **MANDATORY**: Consult `AGENTS.md` for DoD requirements.
- [x] **Implement Pragmatic Snapshot Matrix** (ADR 09):
    - [x] Classify components by risk level (Visual vs. Interactive vs. Adaptive).
    - [x] Configure key permutations: L/D Theme, 2.0x Font Scale (A11y), and RTL.
    - [x] Verify structure on different screen sizes (Compact/Medium/Expanded).
- [x] Generate KDoc via Dokka.
- [x] Configure `.github/workflows/design-system-ci.yml`.
- [x] **MANDATORY**: Execute `compiler` skill verification suite.
- [x] **MANDATORY**: Request Commit & Push (Manual or via `git-governance` skill) before advancing.

## Phase 5: Screen-by-Screen Migration & Comprehensive Audit
- [ ] **MANDATORY**: Consult `AGENTS.md` for UI/UX Engineer standards.
- [ ] **Auth Module Migration**:
    - [ ] **LoginScreen**: Migrate to `MmNavigation`, `MmTextField`, `MmPrimaryButton`, `MmTertiaryButton`, and `MmLoadingOverlay`.
    - [ ] **SignUpScreen**: Migrate to `MmNavigation`, `MmTextField`, `MmPrimaryButton`, `MmErrorState`, and `MmLoadingOverlay`.
    - [ ] **TermsScreen**: Migrate to `MmNavigation`, `MmText.BodyLarge`, and `MmPrimaryButton`.
- [ ] **City Module Migration**:
    - [ ] **CityWeatherScreen**: Migrate to `MmText`, `FndSpacing`, and adaptive components.
    - [ ] **HighTemperatureAlertScreen**: Migrate to `MmText` and `MmPrimaryButton`.
- [ ] **Favorites Module Migration**:
    - [ ] **FavoriteEmptyState**: Migrate to `MmText`.
    - [ ] **FavoriteItem**: Migrate to `MmText` and `FndSpacing`.
- [ ] **Architectural Enforcement**:
    - [ ] **Stateless Screen Mandate**: Remove local `Scaffold` and `TopAppBar` from feature screens (hoist to `MmNavigation`).
    - [ ] **Typographic Boundary**: Replace all `androidx.compose.material3.Text` leaks with `MmText`.
    - [ ] **Token Compliance**: Replace hardcoded `dp`/`sp` and `R.dimen`/`R.color` with `FndSpacing` and `FndColors`.
- [ ] **Comprehensive Quality Audit**:
    - [ ] **Global UI Zoom**: Validate layout integrity during pinch-to-zoom (Density changes).
    - [ ] Android 15 Edge-to-Edge compliance.
    - [ ] Rotation state preservation (UX).
    - [ ] TalkBack walkthrough (A11y).
    - [ ] Pseudo-localization check (Overflow).
    - [ ] Recomposition count audit (Performance).
- [ ] **Cleanup**: 
    - [ ] Delete `ui/screens/shared/` directory.
    - [ ] Remove obsolete resources from `res/values/`.
- [ ] **MANDATORY**: Execute `compiler` skill verification suite.
- [ ] **MANDATORY**: Request Commit & Push (Manual or via `git-governance` skill) before advancing.

---
**Documentation Sync (MANDATORY):** Run `git status .agents/skills/` and update `.agents/skills/README.md` if there are any changes.
