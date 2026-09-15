# CHECKPOINT UI-18 — Profile Motion Refinement

## Status
- 🟡 UI implementation complete
- 🟢 Static source review passed
- ⏳ Android Build pending

## Scope
Refined Profile navigation to feel like a deeper content destination, consistent with the Anime → Detail motion language.

## Changes
- Avatar entry from Home is marked as a deep Profile navigation path.
- Back from that Profile returns through the existing reverse screen transition boundary.
- Bottom-navigation Profile remains a normal tab transition and is not treated as deep navigation.
- Profile avatar now has a restrained scale/fade entrance (`0.86 -> 1.0`, `0 -> 1`) aligned with the existing 260ms motion timing.
- Existing Profile layout, callbacks, data boundaries, provider, playback, backend, billing, ads, and monetization were not expanded.

## UX intent
The profile should feel connected to the avatar that was tapped rather than behaving like an unrelated tab. The current implementation deliberately uses safe Compose-native motion rather than introducing a new dependency or a broad navigation refactor.

## Validation
- Static source review: passed.
- Android Build: pending; no GitHub Actions dispatch performed.
- Provider E2E: not run; not required for this UI-only change.

## Manual APK focus
1. Home -> tap profile avatar -> Profile
2. Profile -> Android Back -> Home
3. Home -> bottom Profile -> Profile (should remain normal tab motion)
4. Social -> other user -> Other Profile -> Back
5. Check for blank frame, visible jump, or sluggish avatar entrance.
