# CHECKPOINT UI-20 — Home Header Photo Correction

## Status
🟡 UI correction complete; static source review passed; Android Build pending.

## Correction
- The supplied Dantotsu screenshot was used only as a reference for the profile-photo/header treatment, not as a request to copy the entire Home layout.
- The broader UI-19 Home reshape was reverted at the Home screen level while preserving history.
- Home now supports an optional `avatarUrl` in `HomeUiState` and renders the real profile photo when supplied.
- When no photo is supplied, the existing initials fallback remains.
- The Home account shortcut visually uses a Key label/icon instead of Diamond, while the existing monetization callback/state remains unchanged.

## Scope boundary
- UI-only.
- No provider, playback, backend, auth, billing behavior, or monetization logic changed.
- No Home-wide redesign based on the supplied screenshot.

## Validation
- Static source review: passed.
- Android Build: pending.
- GitHub Actions: not dispatched automatically.
- Provider E2E: not run.

## Commit boundary
- `73e787f4aed02461074c6ab9d50cc3eec1075606` — add Home avatar UI field.
- `10ba9948f15048cb8273e847630c4ba12c6341df` — restore Home layout; add profile photo and Key presentation.

## Next
Run Android Build manually on `ui13-settings`. After a successful build, verify the header visually and continue the Home discussion from the user's actual intended scope.
