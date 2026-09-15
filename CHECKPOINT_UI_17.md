# CHECKPOINT UI-17 — Dantotsu-inspired Motion System

Status: 🟡 UI motion implementation complete / static source review passed / Android Build pending.

## Scope
- Added a reusable UI-only motion foundation in `ui/motion/KakaMotion.kt`.
- Added reusable `KakaAnimatedScreen` host for screen-to-screen transitions.
- Applied directional screen motion to Home, Anime Detail, Other User Profile, Calendar, Social, Library, Profile, and Diamond/Premium presentation boundaries.
- Home → Detail uses a deeper slide + fade transition; Back reverses direction.
- Tab switching uses a shorter slide + fade so it stays responsive rather than theatrical.
- Overlay presentation uses a restrained fade + scale boundary.
- Episode Gate presentation uses the same compact fade + scale motion boundary.
- Destination payloads are preserved in the animated target state so reverse transitions do not lose the outgoing Detail/Profile content.
- Motion uses Compose built-in animation APIs only; no new animation dependency was added.
- Home V1 and Anime Detail visual layouts were not rebuilt or redesigned; this milestone is motion refinement only.
- Provider, playback, backend, auth, billing, ads, and monetization execution remain outside the UI repository.

## Reference direction
- Motion behavior is inspired by the original Dantotsu's restrained, smooth UI direction.
- This is not a claim that KakaAnime reproduces Dantotsu's internal implementation.

## Validation
- Branch: `ui13-settings`
- Static source review: passed after payload-preservation fix.
- Android Build: pending; latest successful build predates the UI-09 through UI-17 changes.
- GitHub Actions: not dispatched.
- Provider E2E: not run.

## Review focus for next manual APK test
1. Home → Anime Detail feels continuous and does not show a blank frame.
2. Detail → Back reverses smoothly and keeps the correct anime content during the exit.
3. Tab switching feels quick and does not feel like a full-page animation.
4. Other User Profile and Diamond/Premium transitions feel consistent.
5. Episode Gate remains simple and does not feel over-animated.
6. Check for jank on lower-end devices and during image loading.

## Commits in this milestone
- `451ddcd7d2dc5eadc5216e300c996f5cd493c2da` — `ui: add Dantotsu-inspired motion foundation`
- `1ebca28cfb6a4999d68b61c012b13358630b35d0` — `ui: add reusable screen motion host`
- `bfe3c4fd864e73af68cf4b571d2054f40c729f37` — `ui: integrate Dantotsu-inspired screen and modal motion`
- `40377a31b3541229f1b43ef1523e35351d588b7f` — `fix: bind motion transition scope correctly`
- `99768e292248450224254da0eccde0e9b96493e9` — `fix: preserve destination payload during reverse transitions`
