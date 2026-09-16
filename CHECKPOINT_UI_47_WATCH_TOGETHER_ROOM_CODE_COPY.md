# CHECKPOINT UI-47 — Watch Together Room Code Copy

## Scope
- Wire the existing private-room copy callback into the Social → Watch Together local flow.
- Use the Android clipboard from the UI layer so tapping Copy Room Code actually copies the displayed room code.
- Preserve the existing Watch Together/backend boundary; no backend, realtime, billing, or Media3 changes.

## Base
- UI-46 checkpoint: `9754ca4fa2c2b026c2e4fc87f64d7110e9eda221`

## Implementation
- `app/src/main/java/com/kakaanime/app/ui/social/SocialScreen.kt`
- Added `LocalClipboardManager` wiring.
- `WatchTogetherRoomScreen(onCopyRoomCode = ...)` now receives the clipboard action.

## Validation
- Source audit completed.
- Android Build: NOT RUN after UI-47.
- Provider E2E: NOT RUN automatically.

## Status
- UI-47 is checkpointed but NOT build-green yet.
- Continue batching UI work before the next Android Build.
