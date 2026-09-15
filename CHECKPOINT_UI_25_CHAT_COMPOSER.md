# CHECKPOINT UI-25 — Chat Composer Feedback

Status: UI implementation complete; Android Build pending.

## Scope locked
- Sending a non-empty message immediately appends an own-message bubble in the current chat UI.
- The existing `onSendMessage` callback is still invoked so a future data layer can persist/deliver the message.
- Draft is cleared after send.
- Emoji row closes after send.
- Empty/whitespace-only drafts cannot be sent.
- Message state is scoped to the current `chatId`, preventing direct/group chat content from being mixed when switching conversations.
- No backend, persistence, authentication, or realtime transport was added.

## Validation
- Code-level audit completed after implementation.
- Branch: `ui13-settings`.
- Android Build: NOT RUN automatically; must be run manually.
- Provider E2E: NOT RUN.

## Integration rule
Keep UI-25 in `uy-uk` until the branch passes the normal Android Build validation. Do not merge into `Aemeathhh90/Test` before the deliberate integration checkpoint.
