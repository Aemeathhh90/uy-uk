# CHECKPOINT UI-29 — Premium Appearance Gating

## Status
Implementation complete; Android Build pending.

## Scope locked
- Appearance is treated as a Premium feature.
- Free users can choose only:
  - Dark
  - White/Light
- Premium users can access the expanded Appearance customization surface.
- Advanced Appearance sections remain visible to free users but are locked with a Premium/lock surface instead of being silently hidden.
- Locked advanced sections:
  - Accent Color / Custom Color
  - Material You
  - Anime/Manga Dynamic Theme
  - Liquid Glass
  - Device Font
  - OLED / Pure Black
  - UI Scale
  - Theme Variant
  - Appearance Preview
- ThemeCustomizationScreen now exposes `isPremium: Boolean = false` as the UI/data boundary.
- No monetization, entitlement verification, purchase, or backend logic was added to the UI repository.
- The existing monetization model can provide the entitlement when the data layer is wired; current demo shell remains non-Premium.

## UX
- Free users see the basic Light/Dark choices directly.
- Advanced controls remain discoverable and clearly show a lock + Premium label.
- No fake purchase flow was added.

## Validation
- Code-level audit completed.
- Android Build: NOT RUN automatically.
- Provider E2E: NOT RUN.
- Keep UI-29 in `uy-uk` until Android Build passes before integration into `Test`.
