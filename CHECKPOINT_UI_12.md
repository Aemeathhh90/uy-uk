# CHECKPOINT UI-12 — Profile Settings Navigation

## Status
🟢 UI presentation/integration boundary complete; static review passed.

## Implemented
- Profile now exposes a compact `Tampilan` entry for the self profile.
- `Tampilan` opens the existing Theme Customization screen.
- Home notification action opens Notification Center.
- Notification rows can mark themselves read and navigate to a linked anime in the demo shell.
- `Tandai dibaca` clears all unread notifications.
- My Profile edit action opens Edit Profile.
- Saved profile values immediately feed Home, Social, and My Profile demo state.

## Scope boundary
- UI-only/demo state.
- No backend, provider, playback, auth, billing, ads, or persistence changes.
- Existing provider/playback integration remains untouched.

## Validation
- Static source review: passed.
- Android Build: pending; the last validated build predates UI-09 through UI-12 changes.
- GitHub Actions: not dispatched automatically.
- Provider E2E: not run.

## Commit boundary
- `6753403a26a1f171cd4ff9e0ecd44682c7142655` — wire notification, edit profile, and theme screens
- `0ee4ca58ea16605b3851a2fa7a538d439bd757dc` — add profile appearance entry
- `a63bbcd73a527c18cbc20ab33b0f2845964cc2fa` — connect profile appearance navigation

## Next
Run the existing Android Build manually when ready. If the build passes, this UI branch can be considered build-validated before deliberate integration into the main UI baseline.