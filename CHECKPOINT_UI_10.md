# Checkpoint UI-10 — Notification Center

Status: 🟢 UI presentation complete / static review passed.

Implemented in `ui/notifications`:
- Notification UI model with unread state and optional anime/episode target.
- Notification center presentation.
- Empty state for no notifications.
- Unread visual treatment.
- Interactive notification rows.
- Mark-all-read callback boundary.
- Back navigation callback boundary.

Scope:
- UI-only.
- No provider, playback, backend, billing, ads, or persistence logic.
- Ready for integration with the existing Home notification action at a deliberate integration checkpoint.

Validation:
- Static source review passed.
- Android Build has not been rerun after UI-09/UI-10 changes.
- Provider E2E not run (not required for UI work).

Next:
- Deliberate integration into the main app after the UI branch build is validated.
