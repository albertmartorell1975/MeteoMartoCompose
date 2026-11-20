# Operational Constraints

Record only durable `MUST` and `MUST NOT` rules specific to `MeteoMartoCompose` that future sessions and subagents must obey. Do not duplicate general rules already covered in `AGENTS.md` or `.agents/skills/`.

- **MUST**: Run Gradle verification tasks against the `pre` product flavor (`./gradlew testPreDebugUnitTest lintPreDebug --parallel --build-cache --configuration-cache`) because `flavors.gradle` defines `pre` and `pro` dimensions.
- **MUST**: Branch and target Pull Requests for the current modernization backlog (`feature_list.json`) against `feature/MM-02-design-system` as the temporary `base_branch` until the full wave is ready for a final PR into `develop`.
- **MUST**: Use **JDK 21** (`jbr-21` locally in Android Studio; Temurin JDK 21 in GitHub Actions CI) for Gradle builds and Roborazzi/Robolectric tests (`jvmToolchain(21)` in `:app`). Do not run Gradle 8.14.3 on JDK 25.
- **MUST**: Treat `DESIGN.md` as the single visual source of truth — `docs/ui/meteomarto_design_contract.txt` owns the 5 KISS palettes and static/dynamic rules, `docs/ui/screens/` owns layouts, `Roboto Flex` is the sole font family, and no text size may fall below `12.sp`.
- **MUST**: Evaluate `ExtremeAlert` strictly when the **current location** temperature exceeds Firebase Remote Config's threshold (`temperature > threshold`), never via a hardcoded `35°C` value or for remote favorite cities.
- **MUST**: Map OpenWeather condition IDs (`800` = `SUNNY`; `200..232`, `300..321`, `500..531`, `600..622` = `RAIN`; all others = `BASELINE`) and persist `weatherConditionId` + `isNight` via explicit Room `Migration`s.
- **MUST**: Keep non-domain constants in `AppConstants.kt` or `config.xml`, and prefix missing translations with `"TODO: "` using the primary language fallback.
- **MUST NOT**: Convert full-screen layout mockups under `docs/ui/screens/` or `docs/features/<feature-id>/ui/screens/` into Android drawable resources; only convert/install graphic assets from `ui/resources/` as `app/src/main/res/drawable/ic_<name>.webp`.
- **MUST NOT**: Add new Robolectric functional tests or screen-level `*ScreenshotTest.kt` files; reserve Roborazzi strictly for `ui.designsystem.components` `@Preview` composables.
