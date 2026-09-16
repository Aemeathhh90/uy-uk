# CHECKPOINT UI-36 — Player + Notification Audit

## Scope
Audit ringan pada UI-only Player dan Notification Center setelah Social/Watch Together.

## Result
- Player tetap UI-only dan tidak mencampur Media3/provider logic.
- Playback states tetap mencakup Loading, Ready, Buffering, Error, dan Completed.
- 1080p tetap digate oleh `isPremium` di UI.
- Skip Intro/Outro tetap callback-based dan hanya muncul saat state mengizinkan.
- Notification Center dipoles agar mengisi ruang layar secara konsisten.
- Notification list memakai area scroll sendiri dengan bobot layout yang jelas.
- Empty notification state juga mengisi area konten yang tersedia.

## Validation
- Code change committed to `ui33-premium-hub`.
- Android Build belum dijalankan setelah perubahan ini.
- Provider E2E tidak dijalankan sesuai aturan.

## Commit
- `046a23f0ca031c1604818d250f9ec78542bc6edd` — notification layout polish
