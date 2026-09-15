# CHECKPOINT UI-14 — Global Android Back System

## Status
🟢 Navigation boundary fixed; static source review passed.

## Implemented
- Added a global Android BackHandler to the UI shell.
- Back dismisses Episode Gate first.
- Back closes Notification/Edit Profile/Theme overlays.
- Back closes Diamond & Premium.
- Back returns Anime Detail and Other User Profile to the previous UI root.
- Back from Calendar/Social/Library/Profile returns to Home.
- At the Home root, the custom BackHandler is disabled so Android handles normal Activity exit instead of recursively dispatching Back.

## Audit result
- Preflight reviewed the current `ui13-settings` MainActivity and UI-13 checkpoint before changing code.
- Found a concrete implementation bug in the previous BackHandler: it attempted to dispatch Android Back from inside an always-enabled BackHandler and referenced `LocalBackDispatcherOwner` without a valid import/use.
- Replaced it with a state-scoped BackHandler that intercepts only nested UI states.
- No provider, playback, backend, auth, billing, ads, or persistence logic changed.

## Validation
- Static source review: passed.
- Android Build: pending; latest known successful build predates UI-09 through UI-14 changes.
- GitHub Actions: not dispatched automatically.
- Provider E2E: not run.

## Commit boundary
- `2415b746a3d05e0352e95fa25ff6a44362227775` — safe global Android BackHandler implementation.
- `CHECKPOINT_UI_14.md` — navigation checkpoint.

## Next
Continue the remaining V1 navigation/UX audit, then run the existing Android Build manually before deliberate integration into the main UI baseline.
