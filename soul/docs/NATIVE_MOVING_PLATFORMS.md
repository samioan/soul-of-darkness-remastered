# Native type-33 movement and live rider control

The first-level preview now loads its type-33 platform, UID 64, with sprite
2:28 at paint priority 3. `MovingPlatform` implements direction modes 0..7 and
the rider-controlled mode 8 used by this record. Support remains the stable
address of its owned `ContactBody`; the world uses a list so insertion cannot
invalidate a rider's support. World teardown clears the support before releasing
the platform. Type-50 swinging movement is documented in NATIVE_SWINGING_PLATFORMS.md.

Original `Actor.update` carries an attached rider by the previous velocity's
signed pixel shifts **before** integrating the platform. Fixed-point motion
then runs before the type-33 handler. `S/updateMovingPlatform` checks support
twice, before/after control. Rail modes wait, travel to collision-bound limits,
stop and reverse. Modes 4/5 preserve the original extra <<8 in their diagonal
velocity calculation; modes 6/7 lack it. Mode 8 resolves tiles, approaches an
unattached player's height, then accelerates up for a standing rider or down
for a crouched rider. Rider X steers horizontal velocity and selects poses.
Bounds/clock refresh follows the original paint boundary.

The live combat world retains enemy/platform registry order, previous camera
buckets and the prototype 240-wide simulation view. Full-width drawing reveals
the same platform in widescreen. A rider hitting a ceiling stops platform Y
velocity at the original Game.updateWorldActors boundary. Accepted damage from
pose-0 platforms uses live combat ownership. Attached secondary actors, other
platform types, generalized spawn/UID/save ownership and full region resets
remain outside this pass. Collision policy is the level-0 mode; pack 9's stored
dimension mismatch remains checked rather than padded.

The later NATIVE_ICE_ATTACHMENTS.md pass adds type-33 ice-child links,
parent/child update ordering and cleanup. It also verifies that ordinary shipped
ice shots cannot trigger this branch because their attack rectangle is a point.
The branch itself is covered by explicitly controlled original-world fixtures.

`MovingPlatformCheck` invokes original player and platform updates in controlled
order with real sprite data and compares generated Java. Eighteen scenarios
cover all rail directions, boarding/standing/crouch, steering, jump, high/low
approach, pause, tile walls and ceilings: **2,880 ticks / 11,520 snapshots** with
**264,963 original/reference comparisons**. Independent assertions require
boarding. This fixture has no linked secondary actors or full Game runtime.
The native checker compares complete player/platform snapshots, controller
direction/timer, health and support identity.

`--platform-script` is a headless-only controlled fixture starting above the
actual level-0 platform. It crouches to descend, presses Up to stand and ascends.
After ten seconds it remains supported at player Y=1251, platform Y=1270,
platform velocity -1280, with full health and two collected pickups. Twelve
actual host runs preserve state/pixels at widths 240/569, 30/60/144 FPS and both
interpolation settings. This is not a recording of a play-through from spawn.
Six data-free assertions bring combined player checks to **175**; with native
startup tests CTest now has **36 tests** per build.

```powershell
python tools/check_native_moving_platforms.py --exe soul/port/build/Release/soul_assets.exe --port-exe soul/port/build/Release/soul_port.exe
```

The three level-1 type-33 variants now also match original bytecode on the shipped collision map:
the expanded oracle has 26,880 rows and 618,261 original/reference expectations, plus 384 new native
240/569 render surfaces. See [NATIVE_LEVEL1_PLATFORMS.md](NATIVE_LEVEL1_PLATFORMS.md).
Remaining work includes general contacts/region ownership and wider level progression;
these controlled platform fixtures do not establish a full level route.
