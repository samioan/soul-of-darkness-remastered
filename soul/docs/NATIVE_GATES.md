# Native linked gates

The first-level preview now constructs, draws and supplies collision for type-31
gate UID 154 at (1468,1500), using the existing platform sprite 2:28, animation 4.
Its record has no linked UIDs. It stays closed until its script integration is
implemented; this pass does not unlock the end of the first level.

## Original behavior

Construction copies the sprite collision rectangle into a fixed doorway and
immediately fills those collision cells with 24. Animation 4/14 selects an
80-pixel travel counter; other animations select 100. Direction 9 starts in
action 104 and offsets the stored Y origin by the collision height.

`Actor.be` (`updateLinkedGate`) lazily resolves up to two target UIDs. A first
target in action 101 opens the gate if the second resolved target is absent or
also in 101. The activation tick only sets action 102. A subsequent handler
sets velocity to four pixels per tick after the common fixed-point integration.
Directions 1/2 move left/right, 4 moves up, and 8 moves down. Direction 9's
normal shipped entry is the stationary action-104 branch.

Every action-102 handler refreshes sprite bounds, clears the fixed doorway,
and refills the remaining rectangle with one edge anchored to that doorway.
Direction 9 skips the clear. The counter decrements by four; reaching zero
does not stop velocity until the next eligible handler, which enters 105.

Releasing a resolved link enters 106, followed by a snap to the stored origin
and action 107. The snap changes the integer coordinate immediately, leaving
fixed-point synchronization until the next update. Action 107 clears O but
retains P, then returns to idle. These original booleans are retained as
`moving` and `collisionMode` without claiming broader actor-physics integration.
Action 103 stops motion and clears the fixed doorway; 104 stops motion and
refreshes its collision fill. Pause and flags 1024/16384 skip the handler.

Painting clips at the original X/Y doorway origin. Directions 2/8 add the
travel span to the boundary; direction 9 uses an upward boundary at its stored
Y origin. The existing BSprite boundary-mask behavior was inspected alongside
`Actor.l(int,int)`, common integration, bounds and cell-centre collision writes
in original `b` bytecode. No generated Java edits or renames were needed.

## Native ownership

`CombatWorld.loadGates` borrows the existing platform sprite and shared mutable
map, constructs stable gate objects, and rebuilds typed slots in record order.
Gates use the original strict update radius two and ordinary flag precedence.
Linked action addresses resolve from supported player/enemy/platform/bridge/
gate owners; this is not a complete UID registry. Cached target addresses remain
stable with these owners. Gate rendering precedes priority-2 bridges/platforms,
and each draw restores the caller's clip. Presentation width does not change
the 83 ms simulation or collision rectangle.

The native record subset accepts nine-short type-31 records, directions
1/2/4/8/9, sprite animations and selected facing/update/draw flags. It excludes
custom rectangular placement, persistent flag 4 and the full script/save
ownership paths. Mutable collision still rejects pack 9's stored/logical grid
mismatch, as documented for bridges.

Level 0 script UID 165 has an existing-actor track targeting gate 154: tick 0
sets position (1470,1500), animation 4 and action 104; tick 25 moves it to
(1470,1415) and sets action 103. That track is parsed by the existing Python/
Java script work but is not executed by the native preview. The first-level
activation sequence remains pending alongside its trigger and actor dependencies.

## Checks

`GateCheck.java` runs with default regeneration and compares original/reference
gate handlers across sixteen controlled scenarios over 1152 ticks. Its 2304
before/after-paint snapshots include integer/fixed coordinates, velocities,
flags, animation clocks, action, direction, travel counter, stored origins,
O/P, sprite/doorway bounds, resolved links and collision-grid CRCs. Scenarios
cover all directions, clear/hold actions, one/two links, late/missing links,
pause/hidden flags, the 100-pixel counter branch and repeated opening. Paint
is an explicit bounds/clock step using the analysis adapter, not a MIDP runtime.
All 2310 original/reference expectations pass, including independent activation,
velocity-delay and return-position assertions. The arena aligns the narrow gate
with tile centres: independent checks require four initial solid cells and zero
after full opening or action 103. The native checker also rejects an oracle
whose upward-motion collision CRC never changes.

`check_native_gates.py` matches these 2304 native rows and 160 independently
composed gate surfaces at widths 240/569. The surface oracle uses original Java
states, the previously verified Python sprite decoder and inspected boundary
clipping rules. It does not claim phone-framebuffer equivalence. Twelve actual
first-level load/region fixtures additionally verify one loaded gate and state/
pixel stability across 30/60/144 FPS with interpolation on/off. Those host runs
do not execute the gate's unfinished script activation.

Fifteen new data-free assertions cover construction collision, delayed motion,
cached links, pause, travel completion, return timing/fixed synchronization,
clear action, caller-clip restoration, rejected directions and live queue/owner
integration. Combined player checks now total 253; CTest remains 45 tests.

Completed validation: default Java regeneration passes its compilation and
registered original/reference checks; the aligned collision fixture also passes
its focused Java comparison. All 45 CTests pass in Debug and Release. Both
configurations pass the new gate state/pixel/host checks and existing linked
trigger, ordered-world, region, combat, transient, bridge and platform regressions.
The local Windows distribution is rebuilt without game assets.

```powershell
python tools/rename_sod.py
.\soul\port\build.bat
python tools/check_native_gates.py --exe soul/port/build/Debug/soul_assets.exe --port-exe soul/port/build/Debug/soul_port.exe
.\soul\port\build_dist.bat
```

Next: native script execution and additional trigger/actor ownership, including
the gate's activation sequence, then checkpoint/save-backed progression.
The executable remains a first-level preview; full gameplay is unfinished.

The subsequent [native script pass](NATIVE_SCRIPTS.md) executes script 165 in
controlled host runs. Normal activation still depends on chest 57 and the
unported linked trigger/reward sequence.
