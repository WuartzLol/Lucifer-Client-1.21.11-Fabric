# Lucifer Client — Fabric 1.21.11

A crimson-themed, client-side Minecraft mod starter focused on modular UI, CPvP information widgets, HUD preferences, and lightweight diagnostics. It currently provides a ClickGUI/module framework and configurable module stubs. Do not interpret a setting card as proof that the underlying game feature is fully implemented.

## Toolchain
- Minecraft Java Edition 1.21.11
- Java 21
- Fabric Loader / Fabric API versions configured in `gradle.properties`
- Gradle 8.14 (or compatible wrapper installation)

## Build

From the project root with Java 21 and Gradle installed:

```sh
gradle build --no-daemon
```

The output JAR should be in `build/libs/`. The project includes a GitHub Actions workflow at `.github/workflows/build.yml` for cloud builds. Push the repository to GitHub, open **Actions**, run **Build Lucifer Client**, then download the `lucifer-client-build` artifact from a successful run.

## Controls
- Right Shift: open/close ClickGUI.
- Left-click a module card: toggle enabled state.
- Right-click a module card: open its settings.
- In settings, click Boolean controls to toggle and click numeric controls to nudge values.
- Search by typing while the module list is visible; Backspace removes a character.

## Categories
Combat, Movement, Render, HUD, Utility, Performance.

## Scope and status
This is an architectural starter, not a complete CPvP client. Several module entries expose configuration controls before their full behavior exists. The current build has not been compiled in this environment; the configured Minecraft/Fabric dependency versions and mappings must be validated by the first Gradle build. No automated attacks, aim manipulation, reach modification, packet manipulation, or knockback suppression are implemented.

## Compatibility
Minecraft/Fabric APIs change between versions. Test with the exact Fabric Loader, Fabric API, and third-party mod versions you intend to use. Compatibility with Sodium or any other mod is not guaranteed until tested.
