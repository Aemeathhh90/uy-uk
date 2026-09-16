# CHECKPOINT UI-39 — Gate Lifecycle & Premium Routing

## Scope
- Episode Gate now keeps its Dialog mounted during exit so the modal exit animation can actually run before the gate is removed.
- Watch Together free-user Premium entry now opens the existing Premium Hub UI locally instead of leaving the callback visually inert.
- Existing backend/provider boundaries are unchanged.

## Base
- Previous checkpoint: UI-38 overlay lifecycle build validation
- Previous validation commit: `8769a3f43a42b8696a9576167dc40be2c29b7365`

## Current commits
- `54ef923bf2fe96512bc8a9c3cfa9f07a15b8c09c` — wire Watch Together Premium Hub entry
- `78f3a1f8e835401ce91d3d5d33491613f06dd9b9` — make Episode Gate exit animation lifecycle safe

## Validation
- Source/diff audit completed.
- Android Build has NOT been run after UI-39 changes yet.
- Provider E2E has NOT been run automatically.
- Therefore UI-39 is **not build-green yet**.

## Next validation point
Run one Android Build after the current UI batch grows to a suitable validation milestone; do not build for every small UI commit.
