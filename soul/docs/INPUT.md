# Key masks and input timing

Evidence: original `e.Q(int)`, `keyPressed`, `keyReleased`, `A()`, `B(int)`,
`C(int)`, `B()` and `C()` at `soul/decompiled/e.java:5177..5382`. These map to
`mapKeyCode`, `latchInput`, `isKeyHeld`, `wasKeyPressed`, `hasKeyPress` and
`clearInput` in the generated reference. Nokia Canvas overrides retain their
API names.

| MIDP code | Key | Mask |
|---|---|---|
| 48..57 | 0..9 | `1 << (code-48)` (`0x1` .. `0x200`) |
| 35 | # | `0x400` |
| 42 | * | `0x800` |
| -3 | Left | `0x1000` |
| -4 | Right | `0x2000` |
| -1 | Up | `0x4000` |
| -2 | Down | `0x8000` |
| -5 | Center fire | `0x10000` |
| -7 | Right softkey | `0x20000` |
| -6 | Left softkey | `0x40000` |

Other key codes map to zero; these are Nokia codes, not desktop virtual keys.
The reference checker compares the original and regenerated mapping over
every integer from -128 through 255.

Frequently used composite masks:

| Decimal | Hex | Members |
|---|---|---|
| 4112 | `0x1010` | Left / 4 |
| 8256 | `0x2040` | Right / 6 |
| 16388 | `0x4004` | Up / 2 |
| 33024 | `0x8100` | Down / 8 |
| 65568 | `0x10020` | Fire / 5 |
| 327712 | `0x50020` | Left softkey / fire / 5 (menu activation) |
| 270400 | `0x42040` | Left softkey / right / 6 (next Help page) |

These are low-level masks, not final gameplay actions. Jump variants, weapon
switching and magic require the Actor read-through before defining desktop
bindings. A held fire key and a newly pressed fire key have different effects.

The later native fire pass verifies keypad 0 / mask 1 as the weapon-0 magic
edge: desktop K, keyboard 0 and numpad 0 now use that mask after the ability is
unlocked. It is separate from physical attack mask 65568, which takes priority.
Keypad 9 / mask 512 is the original weapon switch; desktop Q, keyboard 9 and
numpad 9 now use it after ice ownership. See NATIVE_ICE_WEAPONS.md for cooldown,
ownership and attack-window gates, and NATIVE_FIRE_MAGIC.md for fire restrictions.

## Buffers and ordering

| Original | Reference | Use |
|---|---|---|
| cA | pendingPressMask | OR of press events since last latch, with local conflict filtering |
| cz | pressedMask | Previous pending presses, consumed by edge-based checks |
| cF | pendingReleaseMask | Release events accumulated since last latch |
| cE | releasedMask | Releases copied by the previous latch |
| cB | heldMask | Held state updated by Canvas callbacks |
| cC | frameHeldMask | Held state consulted by `isKeyHeld`; also updated by callbacks |
| cD | heldFrameCount | Repeat/hold duration counter used with player action logic |
| cG | framesSincePress | Frame counter reset based on the previously latched press mask |

Presses first OR their mapped bit into pending presses, then clear other
direction groups from that pending mask. Fire clears all direction groups
from pending presses. The resulting pending mask is ORed into both held masks.
This does not globally normalize previously held directions: a direction
already in a held mask is not necessarily removed by a later direction press.
Releases accumulate in `pendingReleaseMask` and clear those accumulated bits
from both held masks immediately.

At the end of a successful paint, `latchInput` updates hold counters, copies
`heldMask` to `frameHeldMask`, pending presses to `pressedMask` and pending
releases to `releasedMask`, then clears both pending event masks. Therefore
edge checks in that paint see the prior latch; held checks can see recent
callbacks. Replacing this with a desktop poll immediately before every draw
would change both latency and edge behavior.

`clearInput` zeros all six masks. It does not reset the hold-duration or
frames-since-press counters. It runs on normal screen transitions, returning
from visibility changes, and various menu/script actions. Transition clearing
prevents a single key edge from activating the next menu as well.

The menu cheat detector checks masks `{2,1,1,32}` (keypad 1,0,0,5), resets on
an unexpected press, and enables a separate cheat flag. Preserve the detector
and its event timing in the reference; do not enable it by default in a port.

## Port and verification boundary

The native implementation needs separate pressed/held/released masks on the
simulation clock. Keyboard repeat and presentation FPS must not create extra
gameplay presses. Keep simultaneous-key and focus-loss behavior explicit.

`ReferenceCheck.java` executes mapping, static latching, queries and clearing
against both original bytecode and recompiled reference in isolated class
loaders. It feeds held/pending masks directly for latching tests. It does not
instantiate Canvas or simulate `keyPressed/keyReleased`: the API JARs throw
when used as a runtime. The callback conflict rules above are from source and
bytecode inspection, and still need integration tests once a runtime exists.
