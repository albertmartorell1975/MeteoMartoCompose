# Pull Request Review Log: dynamic-weather-theme-tokens

## PR Review Round #1

- **PR:** `#11` (`https://github.com/albertmartorell1975/MeteoMartoCompose/pull/11`)
- **Base / Head:** `feature/MM-02-design-system` <- `feature/dynamic-weather-theme-tokens` (`1c64af6`)
- **Reviewer:** `@albertmartorell1975` (`reviews #6057548141`, `#6057557525`, `2026-11-25`)
- **Contract / Scope Rule:** `docs/features/dynamic-weather-theme-tokens/spec.md` MUST remain untouched (No Planner Rule).

### Finding 1 — Simplify `FndColors.Companion.fromPalette` KDoc
- **File & Line:** `app/src/main/java/com/martorell/albert/meteomartocompose/ui/designsystem/foundation/FndColors.kt:56` (comment `#4694832098`)
- **Reviewer Instruction:** *"Explicació complexa. L'hauriem de fer més simple"*
- **Required Fix (handled cleanly by `workflow-documenter` after implementation/validation, keeping KDoc boundary intact):**
  - Replace the verbose KDoc on `FndColors.Companion` and `fromPalette(palette)` with concise, plain-English wording (e.g., `"Returns the semantic colors for [palette], using a preset or deriving defaults for custom palettes."`). Keep any other KDoc in `FndColors.kt` equally crisp and easy to read.

### Finding 2 — Eliminate Inline Hardcoded Hex Colors in `WeatherPalette.kt` and `FndColors.kt`
- **Files & Lines:**
  - `app/src/main/java/com/martorell/albert/meteomartocompose/ui/designsystem/foundation/FndColors.kt:98` (comment `#4694839584`: *"Tenim els colors hardcoded?"*)
  - `app/src/main/java/com/martorell/albert/meteomartocompose/ui/designsystem/foundation/WeatherPalette.kt:28` (comment `#4694864898`: *"colors hardcoded?"*)
- **Required Fix:**
  - Create `app/src/main/java/com/martorell/albert/meteomartocompose/ui/designsystem/foundation/FndColorPrimitives.kt` containing `internal object FndColorPrimitives` as the single reference-token table for all raw hex `Color(...)` values (using Compose's `Color.White` instead of `Color(0xFFFFFFFF)`, and declaring each shared hex such as `#D32F2F`, `#1976D2`, `#BA1A1A`, `#2E7D32`, `#81C784`, `#003300`, etc. once with a clear semantic primitive name).
  - Refactor `WeatherPalette.kt` and `FndColors.kt` so they reference `FndColorPrimitives.*` and `Color.White` exclusively — zero inline `Color(0x...)` literals remaining in either file, while preserving the exact ARGB color values (`0` visual/Roborazzi pixel diffs).
  - Do NOT add KDoc to `FndColorPrimitives.kt` during implementation (defer to `workflow-documenter`).

### Finding 3 — Remove Unused Public `object FndFontAxes` (and Audit Unused Foundation Objects/Classes)
- **File & Line:** `app/src/main/java/com/martorell/albert/meteomartocompose/ui/designsystem/foundation/FndTypography.kt:196` (comment `#4694860621`: *"Realment el fem servir o realment ens fa falta aquest objecte?"*)
- **Additional Reviewer Instruction:** *"Per altra banda si hi ha algún objecte o classe que no fem servir, s'elimina."*
- **Required Fix:**
  - Remove public `object FndFontAxes` from `FndTypography.kt` (confirmed unused anywhere outside `robotoFlexFont` lines 24–26) and replace its 4 constants with `private const val` inside `FndTypography.kt` (`FONT_WIDTH_DEFAULT`, `FONT_SLANT_DEFAULT`, `FONT_OPSZ_DEFAULT`, `FONT_OPSZ_TAG`) to preserve zero magic literals and identical Roboto Flex rendering.
  - Keep `FndFontWeight` (actively used by `MmButton.kt` and `FndTypography.kt`) and all other actively used foundation classes/objects (`DesignSystem`, `FndColors`, `FndShapes`, `FndTypography`, `FndSpacing`, `FndDimensions`, `FndMotion`, `FndDensityProvider`, `FndDesignSystemViewModel`, `WeatherPalette`). Audit the touched foundation files during implementation to ensure zero other unused classes, objects, properties, or helpers remain.

### Deferred Item (Option A Confirmed by Developer — Not Part of Round #1 Code Diff)
- **Review Summary (`#6057557525`):** *"Podem crear una documentació del design systemn que estem creant?"*
- **Resolution:** Developer confirmed **Option A** — keep PR #11 focused strictly on Foundation tokens + Round #1 fixes (`spec.md` unchanged), and produce the unified Design System documentation (`docs/design-system.md` replacing outdated `docs/features/design_system/MM-02-Design-System.md`) with/right after `design-system-components-alignment` once `Mm*` components are aligned. Recorded in `feature_list.json` (`design-system-components-alignment.notes`).
