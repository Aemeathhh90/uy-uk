# CHECKPOINT UI-34 — Key Terminology Migration

## Status
- User-facing monetization terminology migrated from Diamond to Key in the UI.
- Premium balance now displays Key with a key icon.
- Free-video cost and ad reward labels now use Key.
- Episode gate now uses Key terminology and key icon.

## Compatibility boundary
- Internal `MonetizationUiState` field names and `EpisodeAccessReason.NO_DIAMONDS` remain unchanged intentionally.
- Backend/data contract migration is deferred to the integration/data layer; this checkpoint only changes user-facing UI terminology.
- Existing `DiamondPremiumScreen` shell-facing API remains unchanged.

## Validation
- Code-level audit completed for the modified Premium and episode-gate UI files.
- Android Build: NOT RUN after UI-34 changes.
- Provider E2E: NOT RUN; not part of this UI validation.
- Do not mark this checkpoint GREEN until the manual Android Build passes.
