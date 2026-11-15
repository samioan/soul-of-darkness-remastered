<!--
The body of every GitHub release, with {{TAG}} substituted by .github/workflows/release.yml.
-->
## Soul of Darkness Remastered {{TAG}}

A Windows port of Gameloft's *Soul of Darkness* 1.0.1 (J2ME), the original game's own code
translated to native C++, with widescreen, smooth frame rates, keyboard and gamepad.

### Getting it running

1. Download `SoulOfDarknessRemastered-{{TAG}}-win64.zip` below and unzip it anywhere.
2. Run **SoulOfDarkness.exe**.
3. Point it at your own copy of the 1.0.1 (240x320, HTC P3450) `.jar`, or put the `.jar` next to
   `SoulOfDarkness.exe` before starting it.
4. Press **Play**.

Installed copies update themselves: the launcher offers each new release.

### This download does not include the game

It cannot: the game is Gameloft's. You supply your own copy.

### Notes

- Windows 64-bit. No installer and no Visual C++ redistributable: unzip and run.
- Your JAR is copied into `game\` beside the launcher. Settings and saves live in
  `%LOCALAPPDATA%\SoulOfDarkness101`.
- No complete playthrough has been recorded yet; please report anything that goes wrong.

*Soul of Darkness* is a trademark of Gameloft. This project is not affiliated with or endorsed by
Gameloft.
