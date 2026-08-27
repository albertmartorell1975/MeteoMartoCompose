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
- [ ] **MANDATORY**: Consult `AGENTS.md` for style and logic standards.
- [ ] Implement `MMSpacing.kt` (Reference scale: XS=4dp, S=8dp, M=16dp, L=24dp, XL=32dp).
- [ ] Implement `MMColors.kt` (Semantic roles mapping Material 3 Expressive).
- [ ] Implement `MMTypography.kt` (Mapping Display, Headline, Body using Roboto Flex Variable Font).
- [ ] Create `DesignSystem.kt` (CompositionLocals provider).
- [ ] Implement `MMDensityProvider.kt` with DataStore persistence for pinch-to-zoom.
- [ ] **MANDATORY**: Execute `compiler` skill verification suite.
- [ ] **MANDATORY**: Request Commit & Push (Manual or via `git-governance` skill) before advancing.

## Phase 3: Core Stateless Components (`MM*`)
- [ ] **MANDATORY**: Consult `AGENTS.md` for architectural mandates.
- [ ] **MMButton**: Slot-based, Spring-motion, weight-axis focus feedback.
- [ ] **MMTextField**: RTL mirroring, A11y labels, semantic error states.
- [ ] **MMText**: Semantic text wrapper for typography tokens.
- [ ] **MMNavigation**: Adaptive scaffold wrapping `NavigationSuiteScaffold`.
- [ ] **MANDATORY**: Execute `compiler` skill verification suite.
- [ ] **MANDATORY**: Request Commit & Push (Manual or via `git-governance` skill) before advancing.

## Phase 4: Testing & CI/CD
- [ ] **MANDATORY**: Consult `AGENTS.md` for DoD requirements.
- [ ] Create Roborazzi snapshot matrix (16 permutations per component).
- [ ] Generate KDoc via Dokka.
- [ ] Configure `.github/workflows/design-system-ci.yml`.
- [ ] **MANDATORY**: Execute `compiler` skill verification suite.
- [ ] **MANDATORY**: Request Commit & Push (Manual or via `git-governance` skill) before advancing.

## Phase 5: Global Migration & Comprehensive Audit
- [ ] **MANDATORY**: Consult `AGENTS.md` for UI/UX Engineer standards.
- [ ] Identify and eliminate all `androidx.compose.material3` leaks in feature screens.
- [ ] Replace `shared/` legacy components with `MM*`.
- [ ] **Comprehensive Quality Audit**:
    - [ ] Android 15 Edge-to-Edge compliance.
    - [ ] Rotation state preservation (UX).
    - [ ] TalkBack walkthrough (A11y).
    - [ ] Pseudo-localization check (Overflow).
    - [ ] Recomposition count audit (Performance).
- [ ] Cleanup: Delete `ui/screens/shared/`.
- [ ] **MANDATORY**: Execute `compiler` skill verification suite.
- [ ] **MANDATORY**: Request Commit & Push (Manual or via `git-governance` skill) before advancing.

---
**Documentation Sync (MANDATORY):** Run `git status .agents/skills/` and update `.agents/skills/README.md` if there are any changes.
