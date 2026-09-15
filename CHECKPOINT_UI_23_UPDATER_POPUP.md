# CHECKPOINT UI-23 — Updater Popup

## Status
UI implementation complete; Android Build validation is still pending.

## Locked behavior
- When the data layer reports `isUpdateAvailable = true`, Home can present an update popup automatically.
- Popup uses a centered rounded dialog with the KakaAnime accent color.
- Shows `New Update Available`, version label, and up to five changelog items.
- Actions are `Nanti` and `Update`.
- `Nanti` closes the popup without updating.
- `Update` closes the popup and forwards the action to the caller; actual download/install remains outside the UI repository.
- Popup entrance uses the existing KakaMotion modal motion system.
- No updater/network/download logic is embedded in the UI repository.
- Default `UpdateUiState` is unavailable, so normal launches do not show a fake update.

## Files
- `app/src/main/java/com/kakaanime/app/ui/update/UpdateUiModels.kt`
- `app/src/main/java/com/kakaanime/app/ui/update/UpdateDialog.kt`
- `app/src/main/java/com/kakaanime/app/ui/home/HomeV1Screen.kt`

## Validation
- Code review performed against current `ui13-settings` branch.
- Android Build not run automatically.
- Provider E2E not run.
