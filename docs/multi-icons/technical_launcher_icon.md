# Technical Documentation: Dynamic Launcher Icon System

## Purpose
The Android API Levels app uses a dynamic launcher icon to display the device's API level. This is achieved by switching between multiple `activity-alias` entries, each with a unique icon, at runtime.

## How It Works

### 1. Manifest Aliases
- In `AndroidManifest.xml`, there is an `activity-alias` for each supported API level (23–40), plus a default alias for fallback.
- Each alias:
  - Points to the main activity (`MainActivityCompose`)
  - Has a unique icon (e.g., `@drawable/ic_launcher_api_29`)
  - Is initially disabled except for the default alias

### 2. Icon Generation
- The script `scripts/generate_dynamic_launcher_icons.py` generates icons for all supported API levels and the default.
- It creates both adaptive and legacy icons, placing them in the correct `res/drawable*` folders.
- To add support for a new API level, update the `LEVELS` list in the script and re-run it.

### 3. Runtime Alias Switching (Deferred Strategy)
- On every app start, `LauncherIconManager.updateLauncherIcon(activity)` is called.
- To prevent the Android system from killing the app process when an alias is disabled (the "close-in-face" problem), we use a **deferred disabling strategy**:
  1.  **Enable Immediately**: The correct alias for the current `Build.VERSION.SDK_INT` is enabled as soon as the app starts.
  2.  **Defer Disable**: The previously active alias is *not* disabled while the user is inside the app. Instead, a `ActivityLifecycleCallbacks` listener is registered.
  3.  **Cleanup on Stop**: When the user leaves the app (e.g., presses Home, and `onActivityStopped` is triggered), the obsolete aliases are finally disabled.
- SharedPreferences (`last_alias`) are used to track the currently active alias and avoid redundant lifecycle registration.
- For API levels above 40, the system enables the default alias (generic icon).

### 4. Launcher Behavior
- Most launchers will update the icon immediately, but some may cache icons and show stale icons for a short time.
- During the very first session where an icon changes, you might briefly see two icons if you go to the launcher before the app is stopped. Once the app is stopped and restarted, only the correct one will remain.
- If two icons persist, it is likely due to launcher cache or a bug in the lifecycle cleanup logic.

### 5. Debugging and Development
- For Play Store release, use the standard deferred switching.
- For Android Studio debug, set the run configuration to launch `MainActivityCompose` directly to avoid intent errors when an alias is swapped.
- If you see `Activity class ...Alias does not exist`, check your run configuration and ensure you are launching the activity, not a specific (potentially disabled) alias.

### 6. Adding Support for New API Levels
- Update the `LEVELS` list in `generate_dynamic_launcher_icons.py`.
- Add new `activity-alias` entries in `AndroidManifest.xml`.
- Re-run the icon generation script.
- Add new icon resources to version control.

## File Reference
- `AndroidManifest.xml`: Alias declarations.
- `LauncherIconManager.kt`: Alias switching logic (with deferred cleanup).
- `generate_dynamic_launcher_icons.py`: Icon generation script.
- `res/drawable*`: Icon resources.

## Troubleshooting
- Duplicate icons: Check if the user has already left the app at least once since the first launch.
- Stale icons: Clear launcher cache or reboot the device.
- Intent errors: Ensure the run configuration is targeting the base activity.

---

For questions, contact the project maintainer or review the code in `/app/src/main/java/it/marcozanetti/androidapilevels/ui/`.
