# Level 5 controller, hazard and linked checkpoint state

`ControllerCheck.java` extends construction/save coverage through the shipped
level 5 controller and hazard handlers, checkpoint capture with three live linked
actors, both Restart and Continue restoration, and a resumed hazard update.
It executes unchanged original bytecode and regenerated Java in isolated worlds.
It uses test RMS/ImageIO adapters, not a MIDP runtime or native gameplay.

## Shipped links and activation

Pack 8 supplies controller UID 9 (type 49), hazard UID 21 (type 78, link `aB=9`)
and three type 82 platform slots. The constructor populates Actor `cq[0..2]`
with UIDs 10, 12 and 13 according to their `aw` variants 1, 3 and 2. The fixture
uses these constructor-established references rather than inventing actors or
assigning the slots itself.

The type 49 constructor reads controller mode, linked UID, axis mask and speed
from record shorts 10..13. For UID 9 those are 4, -1, 4 and 2. In
`updateCameraController` (original b.bi(), b.java:7712), player overlap activates
mode 4 and upward scrolling at two pixels per handler call. The fixture places
the player at X=60/Y=5802 in checkpoint UID 32's area, with camera Y=5600 and
viewport bottom 5920. It independently checks controller UID 9, active mode 4,
vertical-axis selection and target camera Y=5598.

`updateHazardArea78` (original b.bc(), b.java:7111) resolves its controller and,
when that controller has flag 16, moves upward by the controller's speed. It
extends its hazard rectangle through `ar`, clamps its position to visible bottom
minus four, and publishes the camera-relative offset on level 5. The fixture
checks hazard Y=5916 and offset=316 after that call. It does not run enemies or
the entire world's update queue.

## Live checkpoint capture and reconstruction

The fixture changes the three existing platform actors' positions/action state
under controlled preconditions, then calls the original checkpoint handler.
It checks the serialized controller UID, current camera Y (5600, rather than its
5598 target), hazard offset and all three linked UIDs. Restoration must point
the slots at newly constructed world actors and recover all seven snapshot
fields: animation, frame, X/Y, action state, flags and `aw`.

Both complete `restartLevel` and Continue's loading stage 79 are exercised.
The latter uses real menu Continue dispatch after actor cleanup/reset. The tests
retain their distinct checkpoint-removal behavior (NEW_GAME_AND_CONTINUE.md).

The original reconstruction path reads signed shorts at 731/733/735. It enables
the matching controller and linked hazard by setting flag 16 and clearing 32.
For an actor linked to the controller it assigns:

```
cameraY = cameraTargetY = viewBounds[1] = savedCameraY - 2
viewBounds[3] = cameraY + 320
hazard.positionY = savedCameraY + savedHazardOffset
hazard.ar = originalSpawnY - hazard.positionY
cameraControllerMode = 4
```

Original e.d(boolean) bytecode offsets 225..320 confirm the short reads, integer
addition, flag updates and assignments. The fixture verifies camera/target/top
5598, visible bottom 5918, hazard Y=5916 and extent correction 764. Camera
initialization retains that state because mode 2 with an active controller
bypasses ordinary player-centered initialization.

**Restoration does not assign `hazardCameraOffsetY`.** The saved offset is read
into a local for placement. The test puts -777 in the global before reconstructing
and confirms it survives. A subsequent hazard handler call updates the global to
316 as the hazard moves to Y=5914. A common `Actor.update` then synchronizes
fixedY to `5914 << 8` before its handler moves the pixel position to 5912 and
rebuilds collision top 5912; the offset becomes 314 while the camera stays fixed.
This preserves the original integration/handler order, including the temporary
difference between fixed-point and pixel coordinates after handler movement.

## Names and coverage boundary

| Original Game field | Name | Evidence |
|---|---|---|
| am | activeCameraControllerUid | Type 49 assigns/clears its UID; camera and reconstruction read it; checkpoint serializes it |
| an | hazardCameraOffsetY | Type 78 publishes positionY-cameraY; checkpoint writes offset 735 |
| ak | cameraControllerMode | Type 49 copies its mode; reconstruction forces 4; camera branches on mode |
| aj | cameraControllerActive | Controller overlap enables it; removal/reset clears it; region/camera logic consult it |
| ao | cameraScrollHorizontal | Controller axis branches select true for horizontal and false for vertical scrolling |

The pass adds five fields with no decompiler patches. Five further reconstruction
fixtures check camera/offset pairs (5600,316), (-32768,-32768), (32767,32767),
(-1,0), (0,-1). Their sums remain Java ints, including -65536 and 65534; neither
result is narrowed back to a short. Extreme values are decoder/arithmetic probes,
not reachable-save claims, and no subsequent simulation is run for them.

Default regeneration passes **353 expectations, 132 level comparisons and 2575
world comparisons** for this checker. Existing lifecycle/menu/level/sprite/actor
checks retain their separate scope. No rendering or audio is performed, and no
real save file is read or written. The linked actor positions are controlled;
their broader platform AI is not exercised by this checkpoint fixture.

Next: checked native resource loading/rendering, full staged loading and deeper
controller/platform branches, then complete boot/gameplay comparisons. The
Windows executable remains the calibration scaffold.
