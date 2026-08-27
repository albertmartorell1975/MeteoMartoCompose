# Audit Report & Design System Proposal (MM-02)

## 1. Codebase Audit: Current State

### Component Inventory (`ui/screens/shared/`)
| Current Component | Status | Recommendation |
| :--- | :--- | :--- |
| `ErrorScreen` | Fragmented | Migrate to `MMErrorState` (Foundation). |
| `CityTextView` | Utility | Refactor into `MMText` with semantic typography tokens. |
| `TextFieldCustom` | Partial Wrapper | Refactor into `MMTextField` with full M3 slot support. |
| `SnackBarCustom` | Utility | Refactor into `MMSnackbar` extension. |
| `AlertDialogCustom` | Wrapper | Refactor into `MMDialog`. |
| `CircularProgressIndicatorCustom` | Overlay | Refactor into `MMLoadingOverlay`. |

### Theme & Foundation Audit
- **Colors**: Currently uses default M3 template colors (`Purple80`, etc.). Lacks semantic naming relevant to a weather app (e.g., `Sunny`, `Rainy`, `Warning`).
- **Typography**: Only `bodyLarge` is customized. No distinct branding fonts.
- **Spacing**: Inconsistent use of `dimensionResource(R.dimen.*)` and hardcoded `16.dp`.
- **Architecture**: Shared components are mixed with screen-specific logic (`CustomError` enum dependency in `ErrorScreen`).

---

## 2. Proposed Token Structure (`foundation/`)

We will implement a `MeteoMartoDesignSystem` object to hold these tokens, ensuring they are decoupled from the Material 3 `MaterialTheme` while still mapping to it.

### Colors (Semantic)
- `Primary`: Main branding color.
- `Secondary`: Accent color.
- `Surface`: Backgrounds and cards.
- `Error`: Critical alerts.
- `Success`: Positive states (e.g., data updated).

### Spacing (Scale)
- `None`: 0.dp
- `XS`: 4.dp
- `S`: 8.dp
- `M`: 16.dp (Standard gutter)
- `L`: 24.dp
- `XL`: 32.dp

---

## 3. Screenshot Testing: Roborazzi PoC

**Why Roborazzi?**
- **Native Compose Support**: Works seamlessly with `ComposeTestRule`.
- **Local & CI**: Fast execution on local JVM without needing an emulator for simple layout checks.
- **Ease of Use**: Integrates directly with JUnit 4/5.

### Implementation Plan
1. Add `io.github.takahirom.roborazzi` plugin to `libs.versions.toml`.
2. Configure `roborazzi` block in `app/build.gradle.kts`.
3. Create a base `MMComponentTest` class for capturing previews.

---

## 4. Migration Strategy

1. **Phase 2**: Define `MMColors`, `MMTypography`, and `MMSpacing` in `ui/design-system/foundation/`.
2. **Phase 3**: Create `MM*` components.
   - **Constraint**: Components must be **stateless**.
   - **Constraint**: `MMButton` must wrap `Button` but expose a `MeteoMarto` specific API.
3. **Phase 4**: Setup Roborazzi and capture "Golden Images" for all new components.
4. **Phase 5**: Replace all usages of `shared/` components and raw M3 components in `ui/screens/` with `MM*` equivalents.
5. **Phase 6**: Deprecate and delete `ui/screens/shared/`.
