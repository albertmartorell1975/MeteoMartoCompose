# Implementation Plan - Phase 4: Testing & CI/CD (Design System)

Implement a pragmatic and automated verification suite for the MeteoMarto Design System using Roborazzi, Dokka, and GitHub Actions.

## User Review Required

> [!IMPORTANT]
> **Roborazzi Snapshots**: This phase will generate "Golden Images". You will need to commit these images to the repository for the CI to work correctly.
> **GitHub Actions**: I will create the `.github/workflows/design-system-ci.yml` file. You will need to push this to GitHub to activate the pipeline.

## Proposed Changes

### [Design System Previews]

Enhance existing previews to follow the **Pragmatic Snapshot Matrix** (ADR 09).

#### [MODIFY] [MMButton.kt](file:///Users/AlbertMartorell/Development/Android/MeteoMartoCompose/app/src/main/java/com/martorell/albert/meteomartocompose/ui/components/designsystem/MMButton.kt)
- Add a "States" preview to show Enabled and Disabled states in a single snapshot.

#### [MODIFY] [MMTextField.kt](file:///Users/AlbertMartorell/Development/Android/MeteoMartoCompose/app/src/main/java/com/martorell/albert/meteomartocompose/ui/components/designsystem/MMTextField.kt)
- Ensure it covers Error and Disabled states in previews.

#### [MODIFY] [MMNavigation.kt](file:///Users/AlbertMartorell/Development/Android/MeteoMartoCompose/app/src/main/java/com/martorell/albert/meteomartocompose/ui/components/designsystem/MMNavigation.kt)
- Ensure it uses `@MMDevicePreview` to validate adaptive behavior (Rail vs Bar).

---

### [CI/CD Infrastructure]

#### [NEW] [design-system-ci.yml](file:///Users/AlbertMartorell/Development/Android/MeteoMartoCompose/.github/workflows/design-system-ci.yml)
- Configure GitHub Action to:
    - Set up JDK 21.
    - Run `./gradlew :app:verifyRoborazziPreDebug`.
    - Upload artifacts on failure.
    - Run Dokka documentation generation for the Design System Styleguide.

---

### [Semantic Documentation Paths]

#### [MODIFY] [root build.gradle.kts](file:///Users/AlbertMartorell/Development/Android/MeteoMartoCompose/build.gradle.kts)
- Rename documentation output to `technical-reference`.
- Implement on-demand generation via `-PgenerateDocs`.

#### [MODIFY] [app/build.gradle.kts](file:///Users/AlbertMartorell/Development/Android/MeteoMartoCompose/app/build.gradle.kts)
- Implement conditional filtering for Design System mode.
- Disable Dokka by default to optimize compilation speed.

## Verification Plan

### Automated Tests
- Run `./gradlew :app:dokkaGenerateHtml -PgenerateDocs -PdesignSystemDocs` and verify output in `app/build/dokka/design-system/`.
- Run `./gradlew :dokkaGenerateHtml -PgenerateDocs` and verify output in `build/dokka/technical-reference/`.
- Run `./gradlew :app:recordRoborazziPreDebug` to generate initial snapshots.
- Execute `compiler` skill verification suite.

### Manual Verification
- Inspect the generated KDoc in both semantic directories.
- Review the generated Roborazzi images.
