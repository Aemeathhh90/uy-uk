# CHECKPOINT UI-41 — Watch Together Motion

## Scope
- Add the shared KakaMotion modal enter/exit transitions to the Watch Together Create Room overlay.
- Add the same lifecycle-safe mounting pattern to the Watch Together Premium Hub overlay so exit animation can complete before the overlay is removed.
- Preserve the existing Watch Together UI/backend boundary.

## Base
- UI-40 checkpoint: `3d5b91bd5dce092c7d47308d5f53784f850e7614`

## Implementation
- `WatchTogetherScreen.kt`
- Uses existing `KakaMotion.modalEnterTransition` / `modalExitTransition`.
- Uses delayed visibility state for both overlays, matching the lifecycle-safe pattern already established for Episode Gate.
- No provider, backend, realtime, or Media3 changes.

## Validation
- Source audit completed.
- Android Build: NOT RUN after UI-41.
- Provider E2E: NOT RUN automatically.

## Status
- UI-41 is checkpointed but NOT build-green yet.
- Continue batching UI work before the next Android Build.
