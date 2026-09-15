# CHECKPOINT UI-32 — Watch Together

## Status
- UI implementation complete on branch `ui32-watch-together`.
- Base checkpoint: UI-31 `123987f5838eb363ecb418ee3494ee4201fe36f8`.
- Android Build: NOT RUN in this change.
- Provider E2E: NOT RUN.

## Scope locked
- Watch Together has a dedicated UI flow from Social → Nonton Bareng.
- Public rooms are listed and can be joined by Free or Premium users.
- Private rooms can be joined with a room code.
- Create Room is Premium-only.
- Free users see the Create Room lock/upgrade state instead of receiving create access.
- Premium Create Room supports Public or Private visibility.
- Private rooms display a room code in the room UI.
- Room UI shows anime title, episode, host/member role, participant list, live player placeholder, playback position UI, and Leave Room.
- Host/member status is represented in UI models; realtime authority is not implemented here.

## Data boundary
- UI-only implementation.
- Authentication, Premium entitlement enforcement, room persistence, room discovery, invite/code validation, realtime participant state, host authority, and playback synchronization remain outside this UI repository.
- The real data layer must enforce Premium-only room creation server-side.

## Integration note
- Current UI entry is wired inside Social so the Watch Together flow can be previewed without changing the existing app navigation shell.
- Main `Test` integration will later replace demo state/callbacks with real data and realtime contracts.

## Validation
- Code-level audit completed after implementation.
- Android Build must be run manually on this branch before calling the checkpoint GREEN or integrating it into `Test`.
- Provider E2E is not part of this UI validation.
