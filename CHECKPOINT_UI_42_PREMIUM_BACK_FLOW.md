# CHECKPOINT UI-42 — Premium Back Flow

## Scope
- Expose `onBack` from `DiamondPremiumScreen` to the underlying `PremiumScreen`.
- Connect Watch Together's Premium Hub back action to its local overlay state.
- Preserve the existing Premium purchase callback and Watch Together/backend boundary.

## Base
- UI-41 checkpoint: `9953f140bdc4aeb39c94fb1a9f3225139f42b054`

## Implementation
- `app/src/main/java/com/kakaanime/app/ui/monetization/DiamondPremiumScreen.kt`
- `app/src/main/java/com/kakaanime/app/ui/social/WatchTogetherScreen.kt`
- Premium Hub opened from Watch Together can now return to Watch Together through the existing back affordance.
- No provider, backend, realtime, billing, or Media3 changes.

## Validation
- Source audit completed.
- Android Build: NOT RUN after UI-42.
- Provider E2E: NOT RUN automatically.

## Status
- UI-42 is checkpointed but NOT build-green yet.
- Continue batching UI work before the next Android Build.
