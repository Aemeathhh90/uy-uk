# CHECKPOINT UI-35 — Home Audit & Polish

## Status
- Home discovery hierarchy aligned with the locked V1 direction.
- Removed the previous `Trending Now` presentation from the Home default flow.
- Home default discovery order is now `Lanjut Nonton → New Episode → Ongoing → Anime Tamat` after the profile header.
- Episode/status badges are resolved from UI data: `NEW EP`, `ONGOING`, `HIATUS`, `TAMAT`.
- Existing Key presentation remains unchanged.

## Scope
- UI-only changes in `HomeV1Screen.kt`.
- Existing `HomeUiState` contract remains compatible; no backend/provider changes.
- Featured/This Season is intentionally not fabricated because the current UI state does not carry a season/featured contract.
- Continue Watching still consumes the existing real progress-oriented UI list supplied by the integration layer.

## Validation
- Code-level audit completed for this change.
- Android Build: NOT RUN after UI-35 change.
- Provider E2E: NOT RUN and not required for UI validation.
- Checkpoint is NOT GREEN until manual Android Build passes.
