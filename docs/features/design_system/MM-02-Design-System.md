# MM-02: Android Design System Technical Record

## Context
Evolving from fragmented shared components in `ui/screens/shared/` to a formal, modular, and testable Design System layer. This record documents key architectural decisions (ADRs) and compliance traceability.

## Behavioral Matrix (The "Source of Truth")

| Feature | Android 12+ (API 31) | Android 11 & Lower |
| :--- | :--- | :--- |
| **Color Palette** | **Dynamic**: Material You (Wallpaper-based). | **Static**: `MMColors` Reference Tokens. |
| **Font Scaling** | **Non-linear (A14+)**: Adaptive scaling via `sp`. | **Linear**: Uniform scaling. |
| **Edge-to-Edge** | **Native (A15+)**: Mandatory system bar transparency. | **Custom**: `WindowInsets` logic. |
| **Navigation** | **Adaptive**: Automatic Bar vs. Rail transition. | **Fixed**: Logic-based transition. |
| **Motion (Spring)**| **Standard**: Available on all versions via Compose library. | **Standard**: Honors "Remove animations" setting. |

## Key Decisions (ADRs)

### ADR 01: Component Abstraction Strategy
**Decision**: Wrap Material 3 components into explicit `MM*` equivalents.
**Rationale**: Ensures brand consistency, default A11y compliance, and prevents UI leaks into feature screens.

### ADR 02: Stateless by Contract
**Decision**: All Design System components MUST be stateless.
**Rationale**: Maximizes reusability and simplifies screenshot testing permutations.

### ADR 03: Roborazzi for Screenshot Testing
**Decision**: Use Roborazzi for JVM-based visual regression.
**Rationale**: Faster feedback loop compared to emulator-based tests, allowing for a comprehensive 16-permutation matrix.

### ADR 04: Screenshot Tool Comparison & Selection
**Decision**: Roborazzi was selected over other market alternatives for its unique balance of speed and simulation power.

| Tool | Environment | Simulation Engine | Interactions (Click/Scroll) | Permutation Logic |
| :--- | :--- | :--- | :--- | :--- |
| **Roborazzi** | **JVM** | **Robolectric** | **YES** (Rich support) | **Automated** (Preview Scanner) |
| **Paparazzi** | JVM | Layoutlib | NO | Manual/Boilerplate |
| **Shot** | Emulator | Android Runtime | YES | Manual (Slow) |
| **Google Official** | JVM | Compose Engine | Limited | Emerging |

**Why Roborazzi for this Design System?**
1.  **Paparazzi Alternative**: While Paparazzi is slightly faster, it cannot simulate interactions. For a Design System where components have focus/pressed states (expressive motion), being able to "click" before the screenshot is vital.
2.  **Shot Alternative**: Shot requires an emulator, which makes the CI/CD pipeline expensive and slow. We need instant feedback on every commit.
3.  **Google Official Tool**: Currently lacks the mature automated scanner for 16 permutations (LTR/RTL x 2.0x Scale, etc.) that Roborazzi provides natively in 2026.

### ADR 05: Slot API Pattern for Content
**Decision**: Use "Slots" (Composable lambdas) for all components that wrap content (e.g., buttons, cards, containers).
**Rationale**: Maximizes component flexibility and decoupling. The design system manages the "container" (layout, borders, interaction effects, motion), while the caller defines the "content" (text, icons, complex layouts). This aligns with the Material 3 standard and ensures components don't need to be modified when new content requirements arise.

### ADR 06: MMText as Typographic Boundary
**Decision**: Mandate the use of `MMText` (to be implemented) as the exclusive boundary for typography in feature screens.
**Rationale**: Prevents direct leaks of Material 3 `Text` components, ensuring that all text in the app strictly adheres to the `MMTypography` tokens and the Roboto Flex variable font configuration.

### ADR 07: Density vs. Font Scaling Precision (UI Zoom)
**Decision**: Explicitly implement a "Global UI Zoom" model where the user-defined scale affects both structural density (`dp`) and font scaling (`sp`).
**Rationale**: Based on Architecture Audit v2 (Point 6), we acknowledge the distinction between purely visual font scaling and structural layout density. While modifying `LocalDensity` affects paddings, dimensions, and touch targets globally (risking `dp` to `px` conversion deviations), we have chosen this "ambitious" path to provide a holistic pinch-to-zoom experience.
**Validation**: 
- **Font Scaling**: Ensures text remains legible at higher scales.
- **Layout Density**: Scales the entire UI container to maintain visual proportions during zoom.
- **Risk Mitigation**: All components must use `sp` for text and `dp` for structural elements. We mandate a minimum touch target of 48dp, which, even when scaled structurally, remains functionally safe as long as the base dimension is correct.

### ADR 08: Material 3 Adaptive Integration
**Decision**: Incorporate `Material 3 Adaptive` and `NavigationSuiteScaffold` as first-class citizens of the design system.
**Rationale**: Adds a responsive dimension for Window Size Classes, ensuring the app scales gracefully from mobile to tablets and foldables.

### ADR 09: Pragmatic Snapshot Testing
**Decision**: Move away from a rigid 16-permutation rule for every component.
**Rationale**: Avoids combinatorial explosion and "snapshot fatigue". Coverage will be determined based on the component's significant behavioral dimensions (e.g., a simple spacer doesn't need 16 snapshots, but a complex Button does).

## Governance & Implementation Rules

To maintain high architectural standards and visual consistency, all design system contributions must adhere to the rules defined in the **`design-system-governance`** skill.

### 1. Component Architecture & Boundaries
- **Stateless by Contract**: Every `MM*` component MUST be stateless. No internal `mutableStateOf`.
- **Slot API Usage**: Components receiving content must use slots to allow for dynamic internal layouts.
- **Modifier Requirement**: Every component signature MUST include `modifier: Modifier = Modifier` as its first optional parameter.
- **Typographic Boundary**: Feature modules MUST NOT depend directly on Material 3 `Text`. Use `MMText` to ensure token compliance.
- **M3 Dependency Rule**: Feature modules MUST NOT depend directly on Material 3 components when an equivalent `MM*` component exists.

### 2. Accessibility (A11y), Adaptive & Scaling
- **Universal RTL**: Absolute prohibition of `left/right`. Use `start/end` exclusively. Mirror directional icons.
- **Scaling & Reflow**: Support 200% font scaling via `sp`. Use `wrapContentHeight()` to avoid clipping.
- **Touch Targets**: Minimum interactive area of 48x48dp.
- **Adaptive Layouts**: Use `NavigationSuiteScaffold` for automatic Bar/Rail transitions based on Window Size Classes.

### 3. Verification & "Gates"
- **Architecture Gates**: Progress between phases requires meeting specific quality gates (e.g., API stabilization before global migration).
- **Multipreview Infrastructure**: Establish reusable Previews (Theme x RTL x Scale) before automating with Roborazzi.
- **Pragmatic Snapshots**: Define significant permutations per component for Roborazzi verification, avoiding unnecessary redundant snapshots.
- **Stateless Previews**: `@Preview` functions must remain stateless to support automated scanning.

### Automated Screenshot Generation (Lessons Learned)
To ensure the visual integrity of the design system without increasing the maintenance burden, we utilize the **Roborazzi Automated Preview Scanner**.

```kotlin
roborazzi {
    generateComposePreviewRobolectricTests {
        enable = true // Currently disabled during migration
        packages = listOf("${android.namespace}.ui")
    }
}
```

#### ⚠️ Critical Troubleshooting & Requirements:
*   **JDK 21 (MANDATORY)**: As the project targets SDK 36 (Android 16), **Java 21** is required.
*   **Test Application Isolation**: Robolectric tests now use `TestMeteoMartoApp` (defined in `app/src/test/resources/robolectric.properties`) to avoid instantiating the real `MeteoMartoApp`. This prevents infrastructure leaks (Firebase/Hilt) during test setup.
*   **Stateless Previews**: Even with application isolation, `@Preview` functions MUST be **100% Stateless** to be scannable by Roborazzi without requiring dependency injection.

#### Why this is critical for the project:
*   **Automatic Synchronization**: In a Design System, the `@Preview` is the source of truth. Every preview automatically becomes a test case.
*   **Permutation Scaling**: Roborazzi handles the complexity of the 16 different permutations matrix (RTL, Landscape, Scaling, etc.).
*   **JVM-Speed Feedback**: Tests run on the JVM, providing seconds-fast visual regression suites for CI/CD.

#### Developer Workflow:
- **Recording**: When a new component or visual state is finalized, run `./gradlew recordRoborazziDebug` to store the new reference images.
- **Verification**: During development or PR checks, run `./gradlew verifyRoborazziDebug`.

## Accessibility (A11y) Traceability
- **Contrast**: WCAG 2.1 AA (4.5:1) compliance for all semantic colors.
- **Touch Targets**: 48x48dp minimum for interactive zones.
- **LVM (Low Vision Mode)**: Native support for 2.0x scaling with reflow.

## Infrastructure & Persistence (Phase 2)

To ensure visual preferences are persistent and reactive, we utilize **Jetpack DataStore**.

### Single Source of Truth (SSOT)
Visual scaling (pinch-to-zoom) is managed as part of the infrastructure layer. 
- **`UserPreferences`**: Domain-level interface defining the contract for visual preferences.
- **`UserPreferencesImpl`**: Infrastructure-level implementation using `DataStore<Preferences>`.
- **`DesignSystemViewModel`**: Exposes the `fontScale` as a `StateFlow<Float>`, ensuring the UI reacts instantly to changes.

### CompositionLocal Hierarchy

The design system propagates tokens through the composition tree using specialized keys. This ensures that every component can access the "source of truth" without manual parameter passing.

| Token Type     | Key                  | Propagation Method          | Change Frequency   |
| :------------- | :------------------- | :-------------------------- | :----------------- |
| **Spacing**    | `LocalMMSpacing`     | `staticCompositionLocalOf`  | **Low**            |
| **Colors**     | `LocalMMColors`      | `compositionLocalOf`        | **Medium** (Theme) |
| **Typography** | `LocalMMTypography`  | `staticCompositionLocalOf`  | **Low**            |
| **Density**    | `LocalDensity`       | Overridden via `Provider`   | **Medium** (Zoom)  |

## Typography Reference (Material 3)

To ensure we don't "pull values out of thin air," we strictly follow the **Material 3 Type Scale**. These values are the foundation for `MMTypography.kt`.

**Official Reference**: [Material 3 Typography Tokens](https://m3.material.io/styles/typography/tokens)

| Style | Size (sp) | Line Height (sp) | Letter Spacing (sp) | Weight (Default) |
| :--- | :--- | :--- | :--- | :--- |
| **Display Large** | 57 | 64 | -0.25 | Regular |
| **Display Medium** | 45 | 52 | 0 | Regular |
| **Display Small** | 36 | 44 | 0 | Regular |
| **Headline Large** | 32 | 40 | 0 | Regular |
| **Headline Medium** | 28 | 36 | 0 | Regular |
| **Headline Small** | 24 | 32 | 0 | Regular |
| **Title Large** | 22 | 28 | 0 | Regular |
| **Title Medium** | 16 | 24 | 0.15 | Medium |
| **Title Small** | 14 | 20 | 0.1 | Medium |
| **Body Large** | 16 | 24 | 0.5 | Regular |
| **Body Medium** | 14 | 20 | 0.25 | Regular |
| **Body Small** | 12 | 16 | 0.4 | Regular |
| **Label Large** | 14 | 20 | 0.1 | Medium |
| **Label Medium** | 12 | 16 | 0.5 | Medium |
| **Label Small** | 11 | 16 | 0.5 | Medium |

> [!NOTE]
> **Accessibility Standard**: While `Label Small` follows the M3 standard (11sp), it is recommended for non-critical information only. For better readability, `Label Medium` (12sp) is preferred for functional text.

## Variable Font Foundations (Roboto Flex)

To leverage modern Android expressive capabilities, we use **Roboto Flex**, a variable font that allows fine-grained control over typography without multiple file overhead.

### Technical Axes Reference
These axes follow the **OpenType Variable Font** standard and the specific capabilities of Roboto Flex.

| Axis | Name | Range | Selected Default | Rationale |
| :--- | :--- | :--- | :--- | :--- |
| **`wght`** | Weight | 100 - 1000 | `MMFontWeight.NORMAL` (400) | Standard weight for high legibility in body text. |
| **`wdth`** | Width | 25 - 150 | `MMFontAxes.WIDTH_DEFAULT` (100) | Maintains standard character proportions. |
| **`slnt`** | Slant | -90 - 90 | `MMFontAxes.SLANT_DEFAULT` (0) | Default upright posture. |
| **`opsz`** | Optical Size | 8 - 144 | `MMFontAxes.OPSZ_DEFAULT` (14) | Optimizes glyph shapes for standard reading distances. |

**Official Reference**: [Google Fonts - Roboto Flex Axes](https://fonts.google.com/specimen/Roboto+Flex/tester) | [OpenType OS/2 Weight Class Specification](https://learn.microsoft.com/en-us/typography/opentype/spec/os2#usweightclass)

> [!NOTE]
> **OpenType Standard**: We follow the universal OpenType specification (ISO/IEC 14496-22) for font weights. While the documentation is hosted by Microsoft, it is the global industry standard used by Android, iOS, and Figma to ensure cross-platform typography consistency.

### Design Implementation
- **Dynamic Weight**: We prefer `MMFontWeight.MEDIUM` (500) for Titles/Labels to improve hierarchical scannability.
- **Optical Adaptation**: The `opsz` axis is set to `MMFontAxes.OPSZ_DEFAULT` (14) by default but can be dynamically adjusted for large hero temperatures (Display roles) if needed.

## Motion & Animation (Phase 2)

To ensure a cohesive "feel", all animations follow standardized motion tokens defined in `MMMotion.kt`.

### Spring Physics
We use **Spring Physics** instead of linear interpolations to achieve a natural, tactile feel that aligns with Android 16's expressive style.

| Token | Description | Specification |
| :--- | :--- | :--- |
| **`SpringExpressive`** | Standard bounce for interaction feedback. | `DampingRatioMediumBouncy`, `StiffnessMediumLow` |

## Components (Phase 3)

### Buttons
Buttons are categorized by their visual emphasis in the UI hierarchy.

| Component | Hierarchy | Description |
| :--- | :--- | :--- |
| **`MMPrimaryButton`** | High Emphasis | For the main action of a screen. |
| **`MMSecondaryButton`**| Medium Emphasis | For supporting actions. |
| **`MMTertiaryButton`** | Low Emphasis | For auxiliary actions or dismissals. |

**Key Features**:
- **Variable Font Feedback**: Animates the `wght` axis of Roboto Flex (400 to 600) when pressed or focused.
- **Spring Physics**: Uses `MMMotion.SpringExpressive` for consistent feedback.
- **Stateless**: All states are hoisted to the caller.
