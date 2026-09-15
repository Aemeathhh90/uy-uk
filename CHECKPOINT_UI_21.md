# CHECKPOINT UI-21 — Home Content Hierarchy & Status Badges

## Scope
Home KakaAnime visual/content hierarchy refinement. This checkpoint records the agreed design decisions before implementation.

## Locked Home structure
1. Header Profile — remains at the top.
2. Featured — large hero area, uses anime from This Season, selected randomly, swipeable, with anime background image.
3. Continue Watching — personal section based on the user's latest real watching/progress activity, not random; horizontally swipeable; compact but large enough to clearly show title, episode, progress, and continue action.
4. New Episode — focuses on newly released episodes.
5. Ongoing — focuses on anime whose series status is ongoing.
6. Anime Tamat — focuses on completed anime.

## Sections intentionally not used on Home
- Trending Movies
- Top Rated
- Most Favourite
- Previous Season / This Season / Next Season selector
- Genres
- Calendar

## Status badge decision
Anime cards use a small diagonal/corner ribbon at the top-left of the poster/card.

Supported status/update labels:
- NEW EP — indicates a newly available episode/update.
- ONGOING — indicates the anime series is still running.
- HIATUS — indicates the anime is temporarily paused.
- TAMAT — indicates the anime is completed.

NEW EP is an update indicator and may coexist with a series status such as ONGOING. Ongoing/Hiatus/Tamat are series-status indicators and must not be treated as equivalent to New Episode.

## Badge visual rules
- Badge is diagonal/ribbon-like and placed at the top-left.
- Keep it small enough that the poster remains the primary visual focus, while text remains clearly readable.
- Default styling must work in both Dark UI and White UI.
- Badge styling is theme/accent-aware; do not hard-code fixed brand colors that override the user's selected accent.
- Status meaning should remain understandable from text, not color alone.
- Final badge geometry, typography, and exact sizing will be tuned from the approved visual reference before implementation.

## Continue Watching behavior
- Order by latest meaningful watching/progress activity.
- Do not randomize.
- Do not promote an anime merely because the user opened its detail page without watching/progressing.
- Horizontal swipe may expose multiple in-progress anime.
- It must not be visually reduced to a tiny standard anime shelf.

## Reference boundary
Dantotsu original is used as a visual/motion reference only. KakaAnime keeps its own Home information architecture and does not copy Dantotsu's Trending/Top Rated/Most Favourite, season selector, Genres, or Calendar layout.

## Implementation status
Not implemented yet. This is a design checkpoint only.

## Validation
Android Build: not run for this checkpoint because no code change was made.
Provider E2E: not applicable.
