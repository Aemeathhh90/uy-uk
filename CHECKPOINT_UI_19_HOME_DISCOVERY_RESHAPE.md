# CHECKPOINT UI-19 — Compact Home Discovery Reshape

## Status
🟡 UI implementation complete; static source review passed; Android Build pending.

## Scope
- Reshaped Home from dashboard/card-heavy layout toward a compact anime-discovery flow inspired by the supplied Dantotsu reference.
- Compact account header with avatar/profile entry and notification action.
- Compact shortcut row for Key, Premium, and Watch Together.
- Diamond presentation in Home UI is replaced by a Key presentation; underlying monetization state/callback remains unchanged for now.
- Added compact Premium banner for free users.
- Added hero/trending presentation before search.
- Search remains functional and filter remains available.
- Added compact Global Chat entry.
- Continue Watching renamed visually to `Terakhir Ditonton` and reduced card height.
- Anime shelves use smaller cards to increase discovery density.
- Existing Home callbacks and provider/data-independent models remain intact.

## Reference decision
- The supplied Dantotsu original screenshot/video is used as visual direction for hierarchy, density, and flow.
- This is not a copy of Dantotsu internals.
- KakaAnime identity and existing feature boundaries are preserved.

## Scope boundary
- UI-only.
- No provider, playback, backend, auth, billing logic, ads, or monetization behavior changed.
- The existing `diamonds` state is intentionally retained internally while the Home presentation uses the Key label/icon; a later monetization/domain decision can rename the underlying model if desired.

## Validation
- Static source review: passed.
- Android Build: pending.
- GitHub Actions: not dispatched automatically.
- Provider E2E: not run.

## Commit boundary
- `3ca4f85f63d6adc09ce60339b6127665719bb19d` — `ui: reshape Home toward compact discovery layout`

## Next
Run Android Build manually on `ui13-settings`. If it passes, install the debug APK and compare Home density, hierarchy, scrolling, and motion against the supplied Dantotsu reference before further visual tuning.
