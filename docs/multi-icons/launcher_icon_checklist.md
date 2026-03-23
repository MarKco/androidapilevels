# Pre-release Checklist: Android API Levels Launcher Icon

This checklist helps you verify that the dynamic launcher icon system works as intended before releasing a new version of the app.

## 1. Build and Install
- [ ] Run the icon generation script: `python3 scripts/generate_dynamic_launcher_icons.py`
- [ ] Build the app: `./gradlew :app:assembleRelease`
- [ ] Install the APK/AAB on a real device (not just emulator)

## 2. Launcher Icon Behavior
- [ ] On first install, the correct API level icon (or generic one) appears.
- [ ] After launching the app, the icon updates to the device's specific API level icon (if it wasn't already).
- [ ] The app does **not** close abruptly when you launch it for the first time (the "deferred disabling" strategy).
- [ ] After going back to the Home screen (Home button), the old icon disappears (if it was being switched).
- [ ] For API levels above 40, the generic icon is shown.

## 3. Alias State
- [ ] After leaving the app at least once, only one `activity-alias` is enabled (check via `adb shell pm list packages -e` or similar tools).
- [ ] No persistent duplicate icons appear after device reboots.

## 4. Launcher Compatibility
- [ ] Test on multiple launchers (Pixel Launcher, OneUI, MIUI, Nova, etc.).
- [ ] After leaving the app and coming back, only one icon is visible.

## 5. Debug/Development
- [ ] For Android Studio: set run configuration to launch `MainActivityCompose` directly.
- [ ] No `Activity class ...Alias does not exist` errors when launching from the IDE.

## 6. Edge Cases
- [ ] Uninstall and reinstall the app: only one icon appears.
- [ ] Update from a previous version: only one icon remains after the first usage session.
- [ ] Change device language/region: icon remains correct.

## 7. Play Store Release
- [ ] Confirm all icons and aliases are present in the release build.
- [ ] Confirm no debug/test icons are included.

---

If any step fails, review the technical documentation and implementation files in `/docs` and `/app/src/main/java/it/marcozanetti/androidapilevels/ui/`.
