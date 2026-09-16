# CHECKPOINT UI-44 — Social Premium State

## Scope
- Preserve the real `SocialUiState.isPremium` value when opening Watch Together.
- Ensure Premium users are not incorrectly shown the Free-user Create Room gate inside the local Social → Watch Together flow.
- Keep Free-user Premium routing unchanged.

## Base
- UI-43 checkpoint: `85cd9f2ead2a3b89223c40de45e16051f5252477`

## Implementation
- `app/src/main/java/com/kakaanime/app/ui/social/SocialScreen.kt`
- `demoWatchTogetherState` now accepts the current premium state instead of hardcoding `false`.
- No provider, backend, realtime, billing, or Media3 changes.

## Validation
- Source audit completed.
- Android Build: NOT RUN after UI-44.
- Provider E2E: NOT RUN automatically.

## Status
- UI-44 is checkpointed but NOT build-green yet.
- Continue batching UI work before the next Android Build.
