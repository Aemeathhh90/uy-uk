# CHECKPOINT UI-33 — Premium Hub

## Status
- Premium subscription hub UI added on branch `ui33-premium-hub`.
- Base checkpoint: UI-32 `72200cde074fe1f5edfafcb949a96c9785fb5aa0`.
- Android Build: NOT RUN in this change.
- Provider E2E: NOT RUN.

## Scope locked
- Existing Diamond & Premium entry now presents the Premium hub.
- Premium status header distinguishes active vs free state.
- Diamond balance remains visible for free users.
- Premium benefits shown: 1080p, auto skip intro/outro, no diamond episode cost, Appearance customization, and Watch Together room creation.
- Free users can select Monthly or Yearly plan in UI.
- Subscribe action exposes a plan identifier through a callback only.
- Ad reward action remains a UI callback only.
- Premium billing, entitlement verification, pricing, purchases, restore, and server enforcement remain outside the UI repository.
- Premium hub is vertically scrollable for small phone screens.

## Integration note
- `DiamondPremiumScreen` remains the existing shell-facing API and delegates to the new `PremiumScreen`, so existing navigation does not need a route rewrite.

## Validation
- Code-level audit completed after implementation.
- Android Build must be run manually on this branch before calling the checkpoint GREEN.
- Provider E2E is not part of this UI validation.
