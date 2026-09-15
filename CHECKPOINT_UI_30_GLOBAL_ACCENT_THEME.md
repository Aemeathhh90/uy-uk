# CHECKPOINT UI-30 — Global Accent Theme Consistency

Status: IMPLEMENTED — Android Build pending.

## Scope
- Strengthen KakaAnime Material3 color scheme so Appearance Accent Color propagates through primary, on-primary, primary-container, secondary, secondary-container, and tertiary roles.
- Derive readable on-primary/on-secondary content colors from the selected accent luminance.
- Keep Dark/Light/OLED background and surface behavior intact.
- This is a theme-layer change; screens such as Home, Calendar, Social, Detail, Library, Profile, Player, dialogs, chips, and buttons that consume MaterialTheme color roles inherit the selected accent automatically.

## Important boundary
- Semantic error/warning colors remain independent of the user's accent.
- Any future screen element using a hardcoded Color instead of MaterialTheme must be migrated separately.
- UI-29 checkpoint remains preserved and is not overwritten.

## Validation
- Code audit: completed for Home, Calendar, Social, Detail, Profile, and central theme.
- Android Build: NOT RUN after UI-30 implementation.
- Provider E2E: NOT RUN; not required for this UI-only change.
- Keep UI-30 in `uy-uk/ui13-settings` until Android Build passes.
