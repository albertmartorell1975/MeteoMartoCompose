# Workflow: MM-02 Design System

This document serves as the granular checklist and architectural roadmap for the implementation of the MeteoMarto Design System.

## Phase 1: Infrastructure & Governance
- [x] Create directory structure for Design System documentation.
- [ ] Add Roborazzi, Dokka, DataStore, and Material3 Adaptive to `libs.versions.toml`.
- [ ] Create `design-system-governance` skill.
- [ ] Initialize `MM-02-Design-System.md` Technical Record.
- [ ] Gradle Sync and verification.

## Phase 2: Foundation & Tokens (The "Truth")
- [ ] Implement `MMSpacing.kt` (Reference scale: XS=4dp, S=8dp, M=16dp, L=24dp, XL=32dp).
- [ ] Implement `MMColors.kt` (Semantic roles mapping Material 3 Expressive).
- [ ] Implement `MMTypography.kt` (Mapping Display, Headline, Body using Roboto Flex Variable Font).
- [ ] Create `DesignSystem.kt` (CompositionLocals provider).
- [ ] Implement `MMDensityProvider.kt` with DataStore persistence for pinch-to-zoom.

## Phase 3: Core Stateless Components (`MM*`)
- [ ] **MMButton**: Slot-based, Spring-motion, weight-axis focus feedback.
- [ ] **MMTextField**: RTL mirroring, A11y labels, semantic error states.
- [ ] **MMText**: Semantic text wrapper for typography tokens.
- [ ] **MMNavigation**: Adaptive scaffold wrapping `NavigationSuiteScaffold`.

## Phase 4: Testing & CI/CD
- [ ] Create Roborazzi snapshot matrix (16 permutations per component).
- [ ] Generate KDoc via Dokka.
- [ ] Configure `.github/workflows/design-system-ci.yml`.

## Phase 5: Global Migration & Comprehensive Audit
- [ ] Identify and eliminate all `androidx.compose.material3` leaks in feature screens.
- [ ] Replace `shared/` legacy components with `MM*`.
- [ ] **Comprehensive Quality Audit**:
    - [ ] Android 15 Edge-to-Edge compliance.
    - [ ] Rotation state preservation (UX).
    - [ ] TalkBack walkthrough (A11y).
    - [ ] Pseudo-localization check (Overflow).
    - [ ] Recomposition count audit (Performance).
- [ ] Cleanup: Delete `ui/screens/shared/`.

---
**Documentation Sync (MANDATORY):** Run `git status .agents/skills/` and update `.agents/skills/README.md` if there are any changes.
