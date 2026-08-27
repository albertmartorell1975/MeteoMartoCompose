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
