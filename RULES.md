# KakaAnime UI/UX — Work Rules

## Mandatory pre-flight
Before **any audit, investigation, implementation, refactor, dependency change, or workflow change**:

1. Check the current repository first.
2. Check the current branch/ref and latest commit.
3. Inspect the relevant files, architecture, existing checkpoints, and current tests/workflows.
4. Compare the current state with the last known checkpoint before deciding what to change.
5. Do not assume the repository matches an older conversation, screenshot, or memory.

## Audit → Reference → Fix → Validate

1. Audit the actual current code first.
2. Identify the affected layer and separate proven facts from hypotheses.
3. Search UI/Android/Compose references only when they reduce uncertainty.
4. Check compatibility with KakaAnime's current architecture before adopting a reference.
5. Implement the proven change without speculative patch stacking.
6. Validate with Android Build or the smallest relevant test.
7. Record meaningful results in a checkpoint.

## UI/UX scope
This repository owns Home, Anime Detail, episode UI, Search, Favorite, Profile, Social, Diamond, Premium, theme/color customization, navigation, and player UI presentation.

## Testing
- Android Build is the routine validation gate.
- Provider E2E is not required for ordinary UI/UX changes.
- Do not dispatch GitHub Actions from the assistant; the user manually starts workflows when requested.
- Provider failures do not block independent UI/UX work unless the UI change depends on provider behavior.

## Checkpoints
- Every meaningful milestone gets a clear commit/checkpoint.
- Never overwrite historical diagnosis just to make the latest status look cleaner.
- Record the affected screen/layer, intended behavior, implementation, validation, and result when applicable.
- Do not change a locked UI decision without a clear reason and checkpoint update.

## Integration
UI/UX changes are integrated into the main KakaAnime repository only at deliberate checkpoints after Android Build passes.
