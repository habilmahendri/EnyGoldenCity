---
name: kmp
description: Kotlin Multiplatform (KMP) + Compose Multiplatform skill for EnyGoldenCity. Use when creating modules, sourceSets (commonMain/jsMain/wasmJsMain/webMain), expect/actual Platform, Compose UI, ViewModel/UiState, navigation, dependencies via libs.versions.toml, or building wasmJs/jsBrowser targets. Delegates to 15 official kotlin-* skills when deeper review is needed.
---

# KMP Skill — EnyGoldenCity (Project Router)

> Skill ini adalah router project-specific untuk EnyGoldenCity + wrapper atas 15 skills official dari `mmiani/kotlin-kmp-claude-agent-skills` (Apache-2.0) yang sudah ter-install di `.opencode/skills/kotlin-*`. Untuk review mendalam, delegate ke skill specialist di bawah.

Kotlin `2.4.10` (K2), Compose Multiplatform `1.11.1`, Material3 `1.11.0-alpha07`, `androidx-lifecycle` `2.11.0-beta01`, Wrappers `2026.8.4`.

Project: `shared` (library, js+wasmJs) + `webApp` (executable, webMain) — Web-only. No android/ios/jvm targets yet.

## 1. Module & SourceSet Structure

```
shared/
  src/commonMain/kotlin/com/enygoldencity/ -> common logic, App.kt, Platform.kt (expect), Greeting.kt
  src/jsMain/kotlin/                        -> actual js: JsPlatform via web.navigator.navigator
  src/wasmJsMain/kotlin/                    -> actual wasm: WasmPlatform
  src/commonMain/composeResources/          -> Compose Resources (drawable/vector)
  src/commonTest/kotlin/                    -> kotlin.test
webApp/
  src/webMain/kotlin/com/enygoldencity/main.kt -> main() { ComposeViewport { App() } }
  src/webMain/resources/index.html, styles.css
```

**Rules:**
- Shared code SELALU di `shared/src/commonMain`. Jangan taruh logic platform-specific di commonMain — pakai `expect/actual`.
- `shared/build.gradle.kts` = `js {browser()} + wasmJs {browser()}` TANPA `binaries.executable()`. `webApp/build.gradle.kts` WAJIB `binaries.executable()` untuk kedua target.
- Tambah target baru (misal `androidTarget()`, `jvm()`, `iosX64()`) harus di `shared/build.gradle.kts` + tambah sourceSet (`androidMain`, `jvmMain`) + update `settings.gradle.kts` jika module baru.

## 2. Platform Abstraction (expect/actual)

Template di `shared/src/commonMain/kotlin/com/enygoldencity/Platform.kt:1-7`:
```kotlin
interface Platform { val name: String }
expect fun getPlatform(): Platform
```
- `shared/src/jsMain/kotlin/com/enygoldencity/Platform.js.kt:5-14` -> parsing `navigator.userAgent`
- `shared/src/wasmJsMain/kotlin/com/enygoldencity/Platform.wasmJs.kt:3-4` -> hardcode `Web with Kotlin/Wasm`

Saat tambah platform baru, buat file `Platform.android.kt`, `Platform.jvm.kt` dengan `actual fun getPlatform()`.

## 3. Dependency Management

Semua versi terpusat di `gradle/libs.versions.toml:1-26`. Jangan hardcode versi di `build.gradle.kts`.

```kotlin
// shared/build.gradle.kts
commonMain.dependencies {
  implementation(libs.compose.runtime)
  implementation(libs.compose.foundation)
  implementation(libs.compose.material3)
  implementation(libs.compose.ui)
  implementation(libs.compose.components.resources)
  implementation(libs.androidx.lifecycle.viewmodelCompose)
  implementation(libs.androidx.lifecycle.runtimeCompose)
}
jsMain.dependencies { implementation(libs.wrappers.browser) }

// webApp/build.gradle.kts
commonMain.dependencies { implementation(project(":shared")) }
```

Tambah library baru: deklarasi di `[libraries]` di `libs.versions.toml`, lalu pakai `libs.xxx` di module.

## 4. Compose UI Conventions (untuk Web)

- Entry: `webApp/src/webMain/kotlin/com/enygoldencity/main.kt:7-10` -> `ComposeViewport { App() }` dengan `@OptIn(ExperimentalComposeUiApi::class)`.
- `shared/src/commonMain/kotlin/com/enygoldencity/App.kt:22-49` adalah template — refactor ke feature-based jika scale:
  ```
  shared/src/commonMain/kotlin/com/enygoldencity/
    feature/home/HomeScreen.kt
    feature/home/HomeViewModel.kt
    ui/theme/Theme.kt
  ```
- WAJIB `MaterialTheme` di root App.
- State: jangan `remember { mutableStateOf }` untuk state business — pakai `ViewModel` + `UiState` + `collectAsStateWithLifecycle()` (dari `lifecycle-runtimeCompose`).
- Recomposition stability: `data class UiState` immutable, `Modifier` param paling akhir, hindari lambda tidak stabil.
- Resources: `Res.drawable.compose_multiplatform` via `org.jetbrains.compose.resources.painterResource` (generated di `enygoldencity.shared.generated.resources`).
- Web CSS di `webApp/src/webMain/resources/styles.css:1-7` harus `overflow:hidden` untuk Compose viewport full-screen.

## 5. Architecture (MVVM/MVI)

```
commonMain/
  data/         -> repository impl, expect/actual data source
  domain/       -> usecase, model, repository interface
  presentation/ -> ViewModel, UiState, UiEvent, UiEffect
  ui/           -> Composable stateless
```

- `UiState` single source of truth: `data class HomeUiState(val isLoading: Boolean, val greeting: String)`
- ViewModel: `class HomeViewModel: ViewModel() { private val _state = MutableStateFlow(...); val state = _state.asStateFlow(); fun onEvent(e: UiEvent) { _state.update { ... } } }`
- Gunakan `_state.update { }` atomik, jangan `_state.value = _state.value.copy()`.
- Collect di Composable: `val state by viewModel.state.collectAsStateWithLifecycle()`

## 6. Navigation

Belum ada navigation di project ini. Jika butuh:
- Untuk Web: pakai `androidx.navigation` compose atau `precompose` atau `Voyager` yang support wasmJs. Tambah di `libs.versions.toml` dan `commonMain.dependencies`.
- Jangan pakai `androidx.navigation` yang hanya support Android.

## 7. Build & Run

```bash
./gradlew :webApp:wasmJsBrowserDevelopmentRun  # Wasm, modern browsers, lebih cepat
./gradlew :webApp:jsBrowserDevelopmentRun      # JS, fallback
./gradlew :shared:jsBrowserDevelopmentRun      # cek shared saja
./gradlew build                                # build semua target
./gradlew :shared:wasmJsTest                   # test wasm
```

`index.html:18` load `webApp.js` — untuk wasm output akan jadi `webApp.wasmJs.js` tergantung task. Pastikan `styles.css` ter-link.

## 8. Testing

- `shared/src/commonTest` pakai `kotlin.test`. Tambah `kotlin-test-junit` jika butuh JUnit.
- ViewModel test: pakai `kotlinx-coroutines-test` + Turbine: `viewModel.state.test { assertEquals(...) }`
- Compose UI test: pakai `compose.uiTest` (support web terbatas — prefer ViewModel unit test).

## 9. Common Pitfalls di Project Ini

- Typo `App.kt:44` `Text("Compose:Compose: $greeting")` -> harus `Text("Compose: $greeting")`
- `shared` sudah depend `lifecycle-viewmodelCompose` tapi belum dipakai — segera migrasi `remember { mutableStateOf }` ke ViewModel.
- Jangan tambah `androidx.compose.ui:ui-tooling-preview` untuk logic runtime — hanya preview.
- `wrappers-browser` hanya di `jsMain` — jika butuh di `wasmJsMain`, tambah juga di `wasmJsMain.dependencies`.
- Config `kotlin.code.style=official`, `org.gradle.configuration-cache=true` — jangan matikan tanpa alasan.

## 10. Checklist Saat Menambah Feature Baru

1. Tambah model di `commonMain`
2. Buat `expect` jika butuh platform API, implement `actual` di `jsMain` & `wasmJsMain`
3. Buat `Repository` interface di `domain`, impl di `data`
4. Buat `ViewModel` + `UiState` di `presentation`
5. Buat Composable di `ui`/`feature`
6. Tambah dependency di `libs.versions.toml` jika perlu
7. Test di `commonTest`
8. Verify `./gradlew :webApp:wasmJsBrowserDevelopmentRun` tidak error

## 11. Routing ke 15 Skills Official (mmiani)

Skill `kmp` ini menangani bootstrap project-specific. Untuk review/implementasi mendalam, delegate ke:

| Trigger | Skill Specialist | Path |
|---|---|---|
| arsitektur, PR, layer boundaries | `kotlin-project-architecture-review` | `.opencode/skills/kotlin-project-architecture-review/SKILL.md` |
| code review bisnis logic, Compose, coroutines | `kotlin-kmp-code-review` (+ `reference/`) | `.opencode/skills/kotlin-kmp-code-review/SKILL.md` |
| buat feature baru, checklist pre-coding | `kotlin-project-feature-implementation` | `.opencode/skills/kotlin-project-feature-implementation/SKILL.md` |
| modularization, module boundaries | `kotlin-project-modularization` | `.opencode/skills/kotlin-project-modularization/SKILL.md` |
| ViewModel/MVI, UiState, effect | `kotlin-project-state-management` | `.opencode/skills/kotlin-project-state-management/SKILL.md` |
| Compose UI, recomposition, preview | `kotlin-ui-compose-multiplatform` | `.opencode/skills/kotlin-ui-compose-multiplatform/SKILL.md` |
| adaptive, windowSizeClass | `kotlin-ui-adaptive-resources` | `.opencode/skills/kotlin-ui-adaptive-resources/SKILL.md` |
| navigation, routes, backstack, deepLink | `kotlin-navigation-compose-multiplatform` | `.opencode/skills/kotlin-navigation-compose-multiplatform/SKILL.md` |
| expect/actual, sourceSet hierarchy | `kotlin-platform-kmp-bridges` | `.opencode/skills/kotlin-platform-kmp-bridges/SKILL.md` |
| App Links, assetlinks.json | `kotlin-platform-app-links-and-deep-links` | `.opencode/skills/kotlin-platform-app-links-and-deep-links/SKILL.md` |
| repository, data source, SSoT | `kotlin-data-kmp-data-layer` | `.opencode/skills/kotlin-data-kmp-data-layer/SKILL.md` |
| kotlin.test, UI test, fakes | `kotlin-testing-kmp` | `.opencode/skills/kotlin-testing-kmp/SKILL.md` |
| Gradle, version catalog, convention plugin | `kotlin-build-kmp-gradle-governance` | `.opencode/skills/kotlin-build-kmp-gradle-governance/SKILL.md` |
| bugfix, root cause, regression | `kotlin-project-bugfix` | `.opencode/skills/kotlin-project-bugfix/SKILL.md` |
| refactor safety, migration | `kotlin-kmp-refactor-safety` | `.opencode/skills/kotlin-kmp-refactor-safety/SKILL.md` |

Contoh: `kmp` + `kotlin-platform-kmp-bridges` untuk `Platform.kt:7` expect/actual; `kmp` + `kotlin-ui-compose-multiplatform` untuk `App.kt:24`.

## References

- KMP: `shared/build.gradle.kts:1-38`, `webApp/build.gradle.kts:1-28`
- Version catalog: `gradle/libs.versions.toml:1-26`
- Entry: `webApp/src/webMain/kotlin/com/enygoldencity/main.kt:1-11`
- UI: `shared/src/commonMain/kotlin/com/enygoldencity/App.kt:1-49`
- Official repo: https://github.com/mmiani/kotlin-kmp-claude-agent-skills (Apache-2.0)
