gi ei# Android API Levels Launcher Icon Management

This document explains how the dynamic launcher icon system works in the Android API Levels app, including the use of activity aliases, icon generation, and runtime switching. It is intended for new developers joining the project.

## Overview

The app displays a launcher icon that shows the Android API level of the device. This is achieved by using multiple `activity-alias` entries in the manifest, each with a different icon and label. At runtime, only one alias is enabled (corresponding to the current API level), ensuring that only one icon appears in the launcher.

## Key Concepts

### 1. Activity Aliases
- Each supported API level (23–40) has a dedicated `activity-alias` in `AndroidManifest.xml`.
- Each alias points to the main activity (`MainActivityCompose`) and has a unique icon resource (e.g., `ic_launcher_api_29`).
- Only one alias is enabled at a time; all others are disabled.
- For API levels above 40, a generic icon is used (the default alias).

### 2. Icon Generation
- The script `scripts/generate_dynamic_launcher_icons.py` generates all icon assets for each API level.
- It creates adaptive and legacy icons for each level, as well as a generic fallback.
- Icons are placed in the appropriate `res/drawable*` folders.

### 3. Runtime Switching
- On every app start, `LauncherIconManager.updateLauncherIcon(context)` is called.
- This function enables only the alias for the current API level and disables all others.
- The logic is idempotent: it only changes state if needed, minimizing flicker and launcher cache issues.
- The system is robust to launcher cache delays: only one alias is ever enabled, but some launchers may show stale icons for a short time.

### 4. Fallback and Edge Cases
- If the device API level is not explicitly supported (e.g., API 41+), the generic icon is used.
- The system is stateless: the correct alias is recalculated at every app start.
- SharedPreferences are used only for telemetry (last alias used), not for logic.

## File Structure
- `app/src/main/AndroidManifest.xml`: Declares all aliases and their icons.
- `app/src/main/java/it/marcozanetti/androidapilevels/ui/LauncherIconManager.kt`: Handles runtime alias switching.
- `scripts/generate_dynamic_launcher_icons.py`: Generates all icon assets.
- `app/src/main/res/drawable*`: Contains all icon resources.

## Best Practices
- Always run the icon generation script after changing supported API levels or icon design.
- Test on multiple launchers (Pixel, OneUI, MIUI, Nova, etc.) to verify single-icon behavior.
- For Play Store release, use strict single-alias mode. For Android Studio debug, set the run configuration to launch `MainActivityCompose` directly.

## Troubleshooting
- If two icons appear, check for multiple enabled aliases (should never happen with current logic).
- If the launcher shows a stale icon, try clearing launcher cache or rebooting the device.
- If you see `Activity class ...DefaultAlias does not exist`, ensure your run configuration targets the main activity, not an alias.

---

For further details, see the technical documentation in this folder.

g