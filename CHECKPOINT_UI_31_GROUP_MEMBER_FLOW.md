# CHECKPOINT UI-31 — Group Member Flow

## Status
- UI implementation complete on branch `ui31-group-member-flow`.
- Base checkpoint: UI-30 `77371eae33b79165543150ba8a866e46320a03eb`.
- Android Build: NOT RUN in this change.
- Provider E2E: NOT RUN.

## Scope locked
- Group Chat now has a dedicated **Info Grup** entry point from the group chat header.
- Group Info shows the current member list and member count.
- **Tambah Anggota** opens a member picker using available friends who are not already in the group.
- Multiple friends can be selected before pressing **Selesai**.
- Added members are immediately reflected in Group Info and the open Group Chat subtitle/member state.
- A remove-member affordance exists for non-self members in the UI-only demo flow.
- Current user cannot remove themself from the displayed group member list.
- Direct messages remain unchanged and do not show group-management controls.
- Chat content remains text + emoji only.

## Data boundary
- UI-only implementation.
- Networking, authentication, permissions/roles, invite delivery, persistence, membership synchronization, and server-side validation remain outside this UI repository.
- The real data layer must enforce who is allowed to add/remove members and persist membership changes.

## Validation
- Code-level audit completed after implementation.
- Android Build must be run manually on this branch before calling the checkpoint GREEN or integrating it into `Test`.
- Provider E2E is not part of this UI validation.
