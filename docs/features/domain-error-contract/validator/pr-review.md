# PR Review Feedback Log — `domain-error-contract`

## PR Review Round #1

* **PR:** #7 (`feature/domain-error-contract` -> `feature/MM-02-design-system`)
* **Reviewer:** `@albertmartorell1975` (Review `#6027406632` + explicit developer approval to apply all 5 refinements)
* **Contract Status:** Zero spec changes required (`spec.md` remains unchanged; `Result.fold` remains available in `:domain/Result.kt` per spec while ViewModels unify on `when (result)`).

### Actionable Findings

#### 1. Extract Named HTTP Status Constants (`401`, `403`)
* **Files & Lines:**
  * `data/src/main/java/com/martorell/albert/meteomartocompose/data/util/ExceptionMapper.kt` (L34)
  * `app/src/main/java/com/martorell/albert/meteomartocompose/data/error/AndroidErrorMapper.kt` (L59)
* **Reviewer Comment (`#4670175273`):** *"Tenim valors 'fixos' (402,403...)"*
* **Required Change:**
  * Define named `const val` constants in `:data` (e.g., `HTTP_UNAUTHORIZED = 401` and `HTTP_FORBIDDEN = 403` in `ExceptionMapper.kt` or a dedicated pure `:data` constants object `HttpStatusCodes`) and reference those constants in both `ExceptionMapper.kt` (`toDomainError`) and `AndroidErrorMapper.kt` (`FirebaseRemoteConfigServerException`).

#### 2. Remove Redundant `withContext(Dispatchers.IO)` in `CityRoomDataSource`
* **File & Line:** `app/src/main/java/com/martorell/albert/meteomartocompose/data/cityweather/CityRoomDataSource.kt` (L33, L42, L76, L91, L102, L119, L132, L146, L155)
* **Reviewer Comment (`#4670202059`):** *"Room per defecte, no fa ja un canvi de Dispatcher?"*
* **Required Change:**
  * Every method on `CityDao` is already `suspend` (or returns `Flow`), so Room (`2.7.2`) already dispatches them safely off the main thread onto its internal query/transaction executors (`withTransaction` is also main-safe).
  * Remove the redundant `withContext(Dispatchers.IO)` wrappers in `CityRoomDataSource` (including `loadCity`, `addCity`, `refreshCity`, `updateCity`, `makeAllCitiesAsNotJustAdded`, `updateFavorite`, `isCurrentCityFavorite`, `removeCityAsFavorite`, `updateAlertStatus`) and clean up unused `Dispatchers` / `withContext` imports.

#### 3. Eliminate `@SuppressLint("MissingPermission")` in `LocationServerDataSourceImpl`
* **File & Line:** `app/src/main/java/com/martorell/albert/meteomartocompose/data/cityweather/LocationServerDataSourceImpl.kt` (L16)
* **Reviewer Comment (`#4670217850`):** *"Aquest warning el podem evitar?"*
* **Required Change:**
  * Remove `@SuppressLint("MissingPermission")` (and unused `SuppressLint` import).
  * Inject `@ApplicationContext private val context: Context` into `LocationServerDataSourceImpl`.
  * Before calling `fusedLocationProviderManager.lastLocation`, guard explicitly with `ContextCompat.checkSelfPermission` for `Manifest.permission.ACCESS_FINE_LOCATION` and `Manifest.permission.ACCESS_COARSE_LOCATION` in the Android Lint-recognized form (`if (checkSelfPermission(FINE) != PERMISSION_GRANTED && checkSelfPermission(COARSE) != PERMISSION_GRANTED) return Result.Error(DomainError.Location.PermissionDenied)`).
  * Verify `:app:lintPreDebug` passes with `0` `MissingPermission` warnings on `LocationServerDataSourceImpl.kt`.

#### 4. Unify `Result.fold` Call Sites to `when (result)` Across ViewModels
* **Files & Lines:**
  * `app/src/main/java/com/martorell/albert/meteomartocompose/ui/screens/auth/LoginViewModel.kt` (L79)
  * `app/src/main/java/com/martorell/albert/meteomartocompose/ui/screens/auth/SignUpViewModel.kt` (L79)
  * `app/src/main/java/com/martorell/albert/meteomartocompose/ui/screens/city/CityWeatherViewModel.kt` (L237)
  * `app/src/main/java/com/martorell/albert/meteomartocompose/ui/screens/favorites/FavoritesDetailViewModel.kt` (L56)
* **Reviewer Comment (`#4670261774`):** *"en aquí fem servir el fold, mentre que en altres casos, l'hem eliminat"*
* **Required Change:**
  * Replace all 4 `Result.fold(...)` call sites in ViewModels with exhaustive `when (result)` (`is Result.Success -> ...`, `is Result.Error -> ...`) so all ViewModels share a single consistent Kotlin style, removing unused `fold` imports from those ViewModels.
  * Keep `Result.fold` intact in `domain/.../Result.kt` (do NOT alter the `:domain` public API specified in `spec.md`).

#### 5. Remove Unused String Resource `city_forecast_error`
* **File & Line:** `app/src/main/res/values/strings.xml` (L41)
* **Source:** Non-blocking Low note from Validator Report, approved by developer for Round #1 cleanup.
* **Required Change:**
  * Remove `<string name="city_forecast_error">Failed to find out the city forecast</string>` after confirming `0` references remain anywhere in the repository.

### Test & Quality Constraints for Round #1
* Maintain `0` new `@Test` over the already-exhausted feature budget (`6` new `@Test` total already added in Round #0; all `31` pure JVM unit tests + `36` Roborazzi previews must stay green).
* Do NOT modify `spec.md`, do NOT modify Roborazzi snapshots (`36/36` unchanged), do NOT add KDoc to newly introduced public symbols (defer any needed KDoc updates to `workflow-documenter`).
* Run full verification:
  * `./gradlew assemblePreDebug --no-daemon`
  * `./gradlew testPreDebugUnitTest lintPreDebug --parallel --build-cache --configuration-cache`
  * `./gradlew :domain:test :usecases:test :data:test --no-daemon`
  * `./gradlew :app:verifyRoborazziPreDebug --no-daemon`
