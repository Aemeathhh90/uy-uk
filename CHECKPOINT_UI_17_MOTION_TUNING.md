# CHECKPOINT UI-17 Motion Tuning

Status: 🟡 Motion tuning complete / static source review passed / Android Build pending.

## Refinement
- Reduced full-screen movement to subtle directional movement.
- Deep navigation (Home ↔ Detail/Profile/Monetization) now uses a restrained 1/8 enter and 1/14 exit distance instead of a full container slide.
- Tab navigation uses an even lighter 1/18 enter and 1/20 exit movement.
- Timing tuned to 260ms enter / 180ms exit with shorter fades to reduce perceived pauses.
- Modal scale tightened to 97% → 100% on entry and 100% → 98.5% on exit, keeping the gate/panel feeling light.
- No new dependency and no provider/playback/backend changes.

## UX target
The motion should feel connected and premium rather than visibly animated: quick response, gentle movement, no dramatic page sweep, and no blank-frame pause.

## Validation
- Branch: `ui13-settings`
- Static source review: passed.
- Android Build: pending; latest successful build predates UI-09 through current motion work.
- GitHub Actions: not dispatched.
- Provider E2E: not run.

## Manual APK review
Pay particular attention to Home → Detail → Back. If the device still shows a perceptible pause or the movement feels too strong/weak, tune only the motion constants before touching the screen layouts.
