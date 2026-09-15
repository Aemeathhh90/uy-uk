# CHECKPOINT UI-28 — Appearance Expansion

Status: implementation checkpoint
Branch: `ui13-settings`

## Scope

Expanded the KakaAnime Appearance screen into one vertically scrollable settings page based on the agreed visual reference.

### Theme
- System
- Light
- Dark
- OLED / Pure Black

### Color
- Accent color presets
- Custom Color entry point
- Accent selection feedback

### Smart Colors
- Material You toggle surface
- Anime/Manga Dynamic Theme toggle surface

### Visual Effects
- Liquid Glass toggle surface
- Device Font toggle surface

### Layout
- UI Scale: Compact / Default / Large
- Theme Variant: Vibrant / Muted / High Contrast
- Live in-page preview
- Instant-apply / save messaging

## Theme foundation
`KakaThemeState` now carries the new appearance options, and OLED mode maps the app background/surface palette to pure black while preserving the selected accent.

The UI-only toggles for Material You, anime-driven colors, Liquid Glass, UI scale, and theme variants establish the presentation/data boundary; actual device wallpaper color extraction, artwork color extraction, persistence, and advanced glass rendering remain data/platform implementation work.

## Validation
- Code-level review completed for the changed UI/theme files.
- Android Build: NOT RUN automatically; run GitHub Actions manually on `ui13-settings`.
- Provider E2E: NOT RUN.

## Integration rule
Keep UI-28 in `uy-uk` until Android Build passes. Do not integrate into `Aemeathhh90/Test` before the build gate is green.
