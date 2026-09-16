# CHECKPOINT UI-43 — Watch Together Copy Cleanup

## Scope
- Correct the Premium Create Watch Room dialog copy for Premium users.
- Free users retain the Premium requirement message and Upgrade action.
- Premium users now see a creation-ready message instead of a contradictory Premium requirement message.

## Base
- UI-42 checkpoint: `521b064cb264ab6b0db04cc7445fb48a4a959686`

## Implementation
- `app/src/main/java/com/kakaanime/app/ui/social/WatchTogetherScreen.kt`
- No provider, backend, realtime, billing, or Media3 changes.

## Validation
- Source audit completed.
- Android Build: NOT RUN after UI-43.
- Provider E2E: NOT RUN automatically.

## Status
- UI-43 is checkpointed but NOT build-green yet.
- Continue batching UI work before the next Android Build.
