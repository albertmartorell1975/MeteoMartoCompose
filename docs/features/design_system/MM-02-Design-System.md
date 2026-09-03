# MM-02: Android Design System Technical Record

## Context & Vision
Evolving from fragmented shared components in `ui/screens/shared/` to a formal, modular, and testable Design System layer. This record documents key architectural decisions (ADRs) and compliance traceability.

### Behavioral Matrix (The "Source of Truth")

| Feature | Android 12+ (API 31) | Android 11 & Lower |
| :--- | :--- | :--- |
| **Color Palette** | **Dynamic**: Material You (Wallpaper-based). | **Static**: `MMColors` Reference Tokens. |
| **Font Scaling** | **Non-linear (A14+)**: Adaptive scaling via `sp`. | **Linear**: Uniform scaling. |
| **Edge-to-Edge** | **Native (A15+)**: Mandatory system bar transparency. | **Custom**: `WindowInsets` logic. |
| **Navigation** | **Adaptive**: Automatic Bar vs. Rail transition. | **Fixed**: Logic-based transition. |
| **Motion (Spring)**| **Standard**: Available on all versions via Compose library. | **Standard**: Honors "Remove animations" setting. |

---

## Architectural Decisions (ADRs)

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
1.  **Interaction Support**: Unlike Paparazzi, it can simulate clicks/focus before the snapshot, vital for verifying "Expressive Motion".
2.  **Infrastructure Efficiency**: Doesn't require emulators (like Shot), making CI/CD faster and cheaper.
3.  **Maturity**: In 2026, it provides the most mature automated scanner for 16-permutation matrices.

### ADR 05: Slot API Pattern for Content
**Decision**: Use "Slots" (Composable lambdas) for all components that wrap content (e.g., buttons, cards, containers).
**Rationale**: Maximizes component flexibility and decoupling. Aligns with M3 standards.

### ADR 06: MMText as Typographic Boundary
**Decision**: Mandate the use of `MMText` as the exclusive boundary for typography in feature screens.
**Rationale**: Prevents direct leaks of Material 3 `Text` components and ensures token compliance.

### ADR 07: Density vs. Font Scaling Precision (UI Zoom)
**Decision**: Explicitly implement a "Global UI Zoom" model where the user-defined scale affects both structural density (`dp`) and font scaling (`sp`).
**Rationale**: Provides a holistic pinch-to-zoom experience. While altering `LocalDensity` has risks, it ensures visual proportions are maintained during zoom.

### ADR 08: Material 3 Adaptive Integration
**Decision**: Incorporate `Material 3 Adaptive` and `NavigationSuiteScaffold` as first-class citizens.
**Rationale**: Ensures the app scales gracefully from mobile to tablets and foldables based on Window Size Classes.

### ADR 09: Pragmatic Snapshot Testing
**Decision**: Move away from a rigid 16-permutation rule for every component.
**Rationale**: Avoids "snapshot fatigue" and CI/CD waste. Focuses on dimensions (A11y, RTL, Scale) that actually impact the user experience.

> [!TIP]
> **"For Everyone" Explanation:**
> Instead of taking 16 automatic photos of every button (where many would be nearly identical), we only capture situations where the design is actually at risk. 
> For example: What happens if the user has giant text for accessibility? Or if they use a language that reads from right to left? 
> We don't take photos "just because," but to ensure the app remains beautiful and functional where it is most difficult to achieve. This allows us to be faster without sacrificing quality.

---

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

---

## Design Foundations (The Tokens)

### CompositionLocal Hierarchy
The design system propagates tokens through the composition tree using specialized keys. This ensures that every component can access the "source of truth" without manual parameter passing.

| Token Type     | Key                  | Propagation Method          | Change Frequency   |
| :------------- | :------------------- | :-------------------------- | :----------------- |
| **Spacing**    | `LocalMMSpacing`     | `staticCompositionLocalOf`  | **Low**            |
| **Colors**     | `LocalMMColors`      | `compositionLocalOf`        | **Medium** (Theme) |
| **Typography** | `LocalMMTypography`  | `staticCompositionLocalOf`  | **Low**            |
| **Density**    | `LocalDensity`       | Overridden via `Provider`   | **Medium** (Zoom)  |

### Typography Reference (Material 3)
We strictly follow the **Material 3 Type Scale**. These values are the foundation for `MMTypography.kt`.

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

### Variable Font Foundations (Roboto Flex)
We use **Roboto Flex**, a variable font that allows fine-grained control over typography.

| Axis | Name | Range | Selected Default | Rationale |
| :--- | :--- | :--- | :--- | :--- |
| **`wght`** | Weight | 100 - 1000 | `MMFontWeight.NORMAL` (400) | Standard weight for high legibility in body text. |
| **`wdth`** | Width | 25 - 150 | `MMFontAxes.WIDTH_DEFAULT` (100) | Maintains standard character proportions. |
| **`slnt`** | Slant | -90 - 90 | `MMFontAxes.SLANT_DEFAULT` (0) | Default upright posture. |
| **`opsz`** | Optical Size | 8 - 144 | `MMFontAxes.OPSZ_DEFAULT` (14) | Optimizes glyph shapes for reading distances. |

### Motion & Animation
All animations follow standardized motion tokens defined in `MMMotion.kt`.

| Token | Description | Specification |
| :--- | :--- | :--- |
| **`SpringExpressive`** | Standard bounce for interaction feedback. | `DampingRatioMediumBouncy`, `StiffnessMediumLow` |

**Error Feedback (Shake)**:
To capture user attention during validation failures, we use a 10-pixel displacement sequence:
- **`OFFSET_POSITIVE`** (10f): Initial rightward movement.
- **`OFFSET_NEGATIVE`** (-10f): Counter-leftward movement.
- **`OFFSET_ZERO`** (0f): Return to equilibrium.

---

## Infrastructure & Persistence

### Single Source of Truth (SSOT)
Visual scaling (pinch-to-zoom) is managed as part of the infrastructure layer. 
- **`UserPreferences`**: Domain-level interface defining the contract for visual preferences.
- **`UserPreferencesImpl`**: Infrastructure-level implementation using **Jetpack DataStore**.
- **`DesignSystemViewModel`**: Exposes the `fontScale` as a `StateFlow<Float>`, ensuring the UI reacts instantly.

---

## Core Components

### Buttons
Categorized by visual emphasis in the UI hierarchy.
- **`MMPrimaryButton`**: High Emphasis. Features dynamic font weight feedback (Roboto Flex axis 400 to 600) when pressed or focused.
- **`MMSecondaryButton`**: Medium Emphasis.
- **`MMTertiaryButton`**: Low Emphasis (Cancel, auxiliary actions).

### MMTextField
- **Typographic Boundary**: Encourages the use of [MMText] for labels.
- **Expressive Motion**: Features a "shake" animation when error state is triggered.
- **Stateless**: All input and error states are hoisted.

### MMNavigation
Adaptive scaffold that automatically switches between Bottom Bar, Nav Rail, or Nav Drawer based on device size.
- **Material 3 Adaptive**: Wraps `NavigationSuiteScaffold`.
- **Integrated Scaffold**: Provides slots for `topBar` and `floatingActionButton`.

---

## Integration & Migration Strategy (Phase 5)

To ensure long-term maintainability and architectural purity during the global migration:

1. **Shell vs. Logic Separation**: `Navigation.kt` (Logic) acts as the "Brain"; `MMNavigation.kt` (UI Shell) acts as the "Clothing".
2. **The "Empty Shell" Pattern**: For screens without global navigation (e.g. Login), `MMNavigation` must still be used as the root container with an empty list of items to ensure consistent theme propagation.
3. **Stateless Screen Mandate**: Feature screens MUST NOT contain their own `Scaffold` or `TopAppBar`. They receive a `Modifier` and focus exclusively on internal content.

---

## Automation & Maintenance

### Visual Regression (Roborazzi)
Utilizes the **Roborazzi Automated Preview Scanner** for JVM-speed visual regression.

#### 📁 Storage & Persistence
- **Golden Images Path**: `app/src/test/snapshots/`
- All visual snapshots are committed to the repository to serve as the baseline for CI/CD.

#### ⚠️ Critical Environment Requirements:
*   **JDK 21 (MANDATORY)**: As the project targets SDK 36 (Android 16), **Java 21** is required for Robolectric/Roborazzi.
*   **Test Application Isolation**: Robolectric tests use `TestMeteoMartoApp` to avoid infrastructure leaks (Firebase/Hilt).
*   **Private Previews**: We use `includePrivatePreviews = true` in Gradle to ensure all component variants are captured without leaking internal Previews to the public API.
*   **Stateless Previews**: `@Preview` functions MUST be **100% Stateless** to be scannable.

#### Developer Workflow:
- **Recording**: Run `./gradlew :app:recordRoborazziPreDebug` to store reference images.
- **Verification**: Run `./gradlew :app:verifyRoborazziPreDebug` during development or before PRs.

### CI/CD Integration
The **GitHub Actions** pipeline (`design-system-ci.yml`) is triggered on every **Push** or **Pull Request** to `main`, `develop`, or any `feature/**` branch that modifies Design System components or tokens.

1. **Verification**: Compares current UI against "Golden Images".
   - Uses the `--no-daemon` flag for isolated execution.
2. **Failure Handling**: If a mismatch is found, the build fails and an artifact `roborazzi-comparison-results` is uploaded (retained for 5 days).
3. **Automated Documentation**: Generates the "Design System Styleguide" on every successful run.
   - Uploads the documentation as an artifact `design-system-styleguide` (retained for 7 days).

### Security & Permissions
The CI workflow operates under strict **GITHUB_TOKEN** permissions (`contents: read`, `actions: write`) to ensure repository integrity while allowing artifact management.
### Documentation (Dokka)
We use Dokka V2 to generate technical documentation directly from the source code.

#### How to Generate:
- **Design System Styleguide** (Visual): `./gradlew :app:dokkaGenerateHtml -PgenerateDocs -PdesignSystemDocs`
- **Full Technical Reference** (Architecture): `./gradlew :dokkaGenerateHtml -PgenerateDocs`

#### Access Paths:
- **Styleguide**: `app/build/dokka/design-system/index.html`
- **Technical Reference**: `build/dokka/technical-reference/index.html`
