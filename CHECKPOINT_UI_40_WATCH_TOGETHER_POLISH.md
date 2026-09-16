# CHECKPOINT UI-40 — Watch Together Polish

## Scope
- Make the Watch Together room content vertically scrollable so the participant/action area remains reachable on smaller screens.
- Preserve the current UI-only/backend boundary.
- Preserve the existing Premium routing from UI-39.
- Fix local demo host identity so a newly created room uses the current Social username instead of a hard-coded host name.

## Base
- Previous checkpoint: UI-39 `0621fa2051ff87c354bb48cafa4ed69811a83d56`

## Changes
- `app/src/main/java/com/kakaanime/app/ui/social/WatchTogetherScreen.kt`
  - Room screen content now uses `verticalScroll(rememberScrollState())`.
- `app/src/main/java/com/kakaanime/app/ui/social/SocialScreen.kt`
  - Local room creation passes `state.username` into the demo room.
  - Local created room hostName and host participant now use the current username.

## Commits
- `7dfd0a1d8fac1ab693b94cdddbea4c0fe5e94a23` — `fix: make watch together room content scrollable`
- `c44e19979466a15425929e45e407c047d2f25568` — `fix: preserve watch together host identity`

## Validation
- Source/diff audit completed.
- Android Build: NOT RUN after UI-40 changes; wait for the next batched validation milestone.
- Provider E2E: NOT RUN automatically.

## Boundary
- Watch Together remains UI/demo state in `uy-uk`.
- Real room persistence, membership, realtime synchronization, host authority, and Media3 synchronization remain integration/backend responsibilities.
