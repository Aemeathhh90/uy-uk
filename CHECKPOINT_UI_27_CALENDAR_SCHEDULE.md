# CHECKPOINT UI-27 — Calendar Schedule Redesign

Status: UI implementation complete; Android Build pending.

## Scope locked
- Calendar is presented as a Schedule screen inspired by the supplied visual reference, without copying the reference app wholesale.
- Header uses Schedule title/subtitle with search and overflow actions.
- Seven-day date strip uses larger selectable day cards and a distinct selected/today treatment.
- Selected day shows episode count and a vertical schedule timeline.
- Timeline rows include airing time, timeline dot/connector, portrait poster, title, episode, rating, genre/status metadata, airing status, and optional countdown label.
- Empty schedule state remains supported.
- Existing New Updates data remains supported below the schedule when supplied.
- Theme and accent remain driven by KakaAnime Material theme; no fixed reference-app palette was introduced.
- Schedule/network/data logic remains outside the UI repository.
- Existing anime click callback remains the navigation boundary.

## Validation
- Code-level audit completed after implementation.
- Branch: `ui13-settings`.
- Implementation commits: `77d2b7e0b9e86f3d67d16340f9226a26f4167f57`, `6068a188d07fdd33692724566555627008ceed7f`.
- Android Build: NOT RUN automatically; must be run manually through GitHub Actions.
- Provider E2E: NOT RUN.

## Integration rule
Keep UI-27 in `uy-uk` until the branch passes normal Android Build validation. Do not merge into `Aemeathhh90/Test` before a deliberate integration checkpoint.
