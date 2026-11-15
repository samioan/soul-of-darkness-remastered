Soul of Darkness - desktop port (unofficial)
============================================

This package contains no game data. You need your own copy of Gameloft's
"Soul of Darkness" for J2ME (a .jar file).

Which JAR
---------
soul_of_darkness.exe plays version 1.0.1 for 240x320
(the HTC P3450 build, SoulOfDarkness_HTC_P3450_EN_IGPL_EU_101), in English, with
weapon upgrades, transformations, eleven chapters, parallax backgrounds, ten
music tracks and sound effects.
    SHA-256 3da9ea7fe8dfaa2c090e8b3714eaed15127f51dc8b67ed9ef9fed51172e4bea4

Other 240x320 builds with the same resource layout are offered with a warning
and are untested.

Starting
--------
1. Put the .jar next to the program (any file name), or start the program and
   pick the .jar when asked. Nothing is extracted or modified.
2. Run soul_of_darkness.exe.

Settings
--------
Open OPTIONS in the game's main menu or pause menu:
  DISPLAY   width (auto, 240 original, 427, 569, 747), frame rate, smooth
            motion, fullscreen, scaling, fps counter, pause in background
  AUDIO     music volume, mute
  KEYBOARD  select an action, press Enter, then the new key
  GAMEPAD   select an action, press Enter, then the new button

Default controls
----------------
Move            arrows or A / D          d-pad or left stick
Jump            Space or Up              A
Crouch          Down or S                d-pad / stick down
Attack          J, E or Z                X or RT
Magic           K or X                   B
Switch weapon   Q, L or C                Y, LB or RB
Upgrade weapon  U or Backspace           Back      (the "right soft key")
Confirm         Enter                    A (in menus)
Pause / back    Esc                      Start
Skip cutscene   U or Backspace           Back
Phone keypad    0-9, numpad, numpad * /  (the original keys still work, so
                                          hints such as "press 0" are true)

Shortcuts: F1 help, F3 fps counter, F6 mute, F8 width, F9 frame rate,
F10 smooth motion, F11 or Alt+Enter fullscreen.

The game always simulates at its original rate (20 ticks per second, 15 during
cutscenes); higher frame rates only add in-between frames. Width 240 with
smooth motion off is the original presentation.

Files
-----
Settings and the save record live in %LOCALAPPDATA%\SoulOfDarkness101:
settings.ini and dvseed.sav.
A settings.ini placed next to the program takes precedence. Run with --help
for command line options.

soul_engine_check.exe is the project's verification tool; it is not needed to
play.
