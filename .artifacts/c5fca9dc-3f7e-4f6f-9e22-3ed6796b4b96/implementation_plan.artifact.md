# Implement SignUpScreen following LoginScreen pattern

This plan refactors the `SignUpScreen` and its associated `SignUpViewModel` to follow the established architectural pattern used in `LoginScreen`. This includes using the MeteoMarto Design System components, implementing reactive state management, and adding field validation.

## User Review Required

> [!NOTE]
> The `SignUpViewModel` will be refactored to use `MutableStateFlow.update` and will include validation logic similar to `LoginViewModel`.
> `SignUpInteractors` will be updated to include `ValidateEmailUseCase` and `ValidatePasswordUseCase`.

## Proposed Changes

### [Component: Use Cases]

#### [MODIFY] [SignUpInteractors.kt](file:///Users/AlbertMartorell/Development/Android/MeteoMartoCompose/usecases/src/main/java/com/martorell/albert/meteomartocompose/usecases/signup/SignUpInteractors.kt)
- Add `validateEmailUseCase` and `validatePasswordUseCase` to the `SignUpInteractors` data class.

---

### [Component: Dependency Injection]

#### [MODIFY] [RegisterModule.kt](file:///Users/AlbertMartorell/Development/Android/MeteoMartoCompose/app/src/main/java/com/martorell/albert/meteomartocompose/di/auth/RegisterModule.kt)
- Update `provideSignUpInteractors` to inject and provide the new validation use cases.

---

### [Component: UI - Auth]

#### [MODIFY] [SignUpViewModel.kt](file:///Users/AlbertMartorell/Development/Android/MeteoMartoCompose/app/src/main/java/com/martorell/albert/meteomartocompose/ui/screens/auth/SignUpViewModel.kt)
- Refactor `UiState` to include `isEmailValid` and `isPasswordValid`.
- Replace manual state assignments with `_state.update { ... }`.
- Add an `events` channel for `SignUpError`.
- Implement `performSignUp` (replacing `signUpClicked` with a non-suspending version that handles its own scope).
- Implement field validation methods: `validateEmail`, `clearEmailError`, `validatePassword`, `clearPasswordError`.
- Rename `setUser` to `setEmail` for consistency.

#### [MODIFY] [SignUpScreen.kt](file:///Users/AlbertMartorell/Development/Android/MeteoMartoCompose/app/src/main/java/com/martorell/albert/meteomartocompose/ui/screens/auth/SignUpScreen.kt)
- **SignUpScreen (Stateful Wiring)**:
    - Add `snackbarHostState` and `goToLogin` parameters.
    - Implement `LaunchedEffect` for navigation on `validUser`.
    - Collect and handle `SignUpEvent.SignUpError` via Snackbar.
- **SignUpContent (Stateless Content)**:
    - Replace standard Material 3 components with `MmDesignSystem` components.
    - Wire `onFocusChanged` for email and password fields to trigger validation/clear errors.
    - Use `MeteoMartoTheme` for consistent spacing and dimensions.
    - Add a `MmTertiaryButton` for "Go to Login".
    - Apply `Modifier Propagation Mandate` (applied to root only).

---

## Verification Plan

### Automated Tests
- Run existing unit tests for `:usecases` and `:app`.
- I will create a simple unit test for `SignUpViewModel` if time permits, or at least verify it compiles and handles state correctly.

### Manual Verification
- Deploy the app and navigate to the Sign Up screen.
- Verify that field validation works (e.g., error messages appear on blur).
- Verify that the loading overlay appears during sign up.
- Verify that sign up success leads to the Dashboard.
- Verify that sign up failure shows a Snackbar.
