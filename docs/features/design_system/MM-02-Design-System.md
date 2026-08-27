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

## Accessibility (A11y) Traceability
- **Contrast**: WCAG 2.1 AA (4.5:1) compliance for all semantic colors.
- **Touch Targets**: 48x48dp minimum for interactive zones.
- **LVM (Low Vision Mode)**: Native support for 2.0x scaling with reflow.
