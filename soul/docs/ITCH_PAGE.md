# itch.io page copy

The text for the store page, kept in the repo so it can be revised like anything else rather than
living only in a web form. Paste into itch's description field; it takes Markdown. Adapted from
shadowkey-decomp's and tes-travels-decomp's versions of this doc.

Page settings that matter:

- **Kind of project**: Downloadable. Not "HTML" -- there is nothing to run in a browser.
- **Pricing**: free. This is someone else's game; charging for a port of it would be indefensible
  whatever the licence situation.
- **Platforms**: Windows only for now.
- **Upload**: the `SoulOfDarknessRemastered-<tag>-win64.zip` from the matching GitHub release,
  marked "Windows" and **not** "This file will be played in the browser".
- **Cover image**: `soul/port/launcher/assets/banner_source.jpg`, cropped to itch's 630x500.
- **Screenshots**: five widescreen gameplay shots (16:9, no screens from builds before v0.1.5,
  whose narrow rooms showed extended tiles).
- Leave **"Generate itch.io app manifest"** off -- the launcher already updates itself from GitHub,
  and two update mechanisms fighting over the same folder is a bug waiting to happen.

---

## Soul of Darkness -- Remastered

**Gameloft's gothic action-platformer for Java phones, on PC, rebuilt from the original game's own
code. Version 1.0.0 -- the complete game.**

In 2010 Gameloft put a full Castlevania-style adventure on Java phones: a cursed hero, a
vampire-haunted castle, two weapons to upgrade, a magic bar, transformations, bosses and eleven
chapters, all on a 240x320 screen. It has been effectively unplayable for years unless you still
have a compatible phone (or an emulator) and the original game file.

This is that game, running natively on Windows: not an emulator, but the original game's own
decompiled code, verified against the original bytecode and translated to native C++.

### The whole game

Every chapter from the prologue to the end credits, every boss, the weapon and magic upgrades, the
transformations, the parallax backgrounds, the music and sound effects -- with the original's rules,
numbers and quirks. The engine is checked tick for tick against the original game: at the original
screen width it matches it field for field.

### What the PC adds

All of it optional -- width 240 with smooth motion off is the game exactly as the phone drew it.

- **Widescreen without stretching** -- auto, 4:3, 16:9 or 21:9. The view widens at the same height,
  so you see more of every level; the hero, the enemies and the interface keep their pixels. Rooms
  narrower than the screen get a soft, blurred backdrop instead of black bars.
- **Smooth frame rates** -- your display's refresh rate, a fixed rate or unlimited. The game still
  runs at its original speed; the frames in between are interpolated.
- **Keyboard and gamepad** -- any XInput controller, mapped like a console platformer, with
  rebindable controls on an in-game OPTIONS screen drawn in the game's own style.
- **Borderless fullscreen or a window** -- switched any time with F11 or Alt+Enter.
- **Original bugs fixed** -- the next level no longer starts with boss music, music comes back
  after a quick restart, and the weapon upgrade screen no longer shows stale messages.

### What you need

**This download does not include the game.** It cannot -- the game belongs to Gameloft. You supply
your own copy of Soul of Darkness 1.0.1 for 240x320 phones (the HTC P3450 build,
`SoulOfDarkness_HTC_P3450_EN_IGPL_EU_101`, a `.jar` file).

1. Download and unzip anywhere.
2. Run **SoulOfDarkness.exe**.
3. Choose your `.jar` -- or put it next to SoulOfDarkness.exe first and it is found by itself. The
   launcher checks it and keeps its own copy.
4. Pick a window size or fullscreen and press **Play**.

Press F1 in the game for the controls.

### Notes

- **No installer.** Unzip and play; uninstalling is deleting the folder (and, if you want, the
  settings and save in `%LOCALAPPDATA%\SoulOfDarkness101`).
- **It updates itself** from the GitHub releases: an Update button appears in the launcher when a
  new version is out. Your game file, settings and saves are never touched.

### Version 1.0.0 -- complete

This is a finished release: the whole game, start to credits, plus the PC options above. Future
updates may still come (fixes, polish), and the launcher installs them for you, but nothing is
missing for a full playthrough.

Source, the record of how it was reverse engineered and verified, and the issue tracker:
<https://github.com/samioan/soul-of-darkness-remastered>

---

*Soul of Darkness* is a trademark of Gameloft. This is an unofficial fan project, not affiliated
with, endorsed by, or supported by Gameloft or the game's original developers and publishers.
