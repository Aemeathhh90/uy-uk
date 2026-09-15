# Checkpoint UI-22 — Social Message & Chat Group

## Status
- UI implementation committed.
- Android Build not run by assistant.
- Provider E2E not run.

## Locked decisions
- Recently Message remains a vertical chat list.
- Chat Group remains a vertical group list.
- Recently Message and Chat Group are separate horizontally swipeable panels.
- Chat content is text + emoji only; no stickers, GIFs, photos/videos, or file attachments.
- Social layout outside this refinement remains unchanged.

## Implementation
- Added `SocialMessageUi`.
- Added `SocialChatGroupUi`.
- Added `messages` and `chatGroups` to `SocialUiState`.
- Added a horizontally swipeable conversation section to `SocialScreen`.
- Existing Active Friends and Watch Together sections are preserved.
