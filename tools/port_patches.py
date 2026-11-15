"""Desktop-port changes applied to the reference Java before translation (see tools/gen_port.py).

Each entry is (file, old, new, expected_match_count). The generator refuses to run when a
count differs, so a regenerated or renamed reference cannot silently drop a port change.
soul/src itself is never modified.

Rules these patches follow:
  * No field is added to Game/Actor/BSprite (port state lives in tools/port_java/PortConfig.java),
    so the class-file field order used by tools/check_engine.py stays comparable.
  * Every helper is a no-op at PortConfig.screenWidth == 240 with english == false; that
    configuration must remain state-identical to the original bytecode.
  * 240 is never replaced globally. Each site below was read and classified: world clip,
    full-screen fill, visibility, or 240-wide interface panel (left alone and centred instead).
"""

W = "PortConfig.screenWidth"

PORT_PATCHES: list[tuple[str, str, str, int]] = [
    # ------------------------------------------------------------------ frame setup
    ("Game.java",
     "         stateFrame++;\n         tickDeltaMs = 83;\n",
     "         stateFrame++;\n         tickDeltaMs = 83;\n         PortConfig.beginFrame(graphics, state);\n", 1),
    ("Game.java",
     "         graphics = null;\n         latchInput();\n",
     "         PortConfig.endFrame(graphics);\n         graphics = null;\n         latchInput();\n", 1),

    # ------------------------------------------------------------------ text
    ("Game.java",
     "      return stringTables[var0][var1];\n",
     "      return PortConfig.text(var0, var1, stringTables[var0][var1]);\n", 1),

    # ------------------------------------------------------------------ view bounds follow the wide view
    ("Game.java",
     "      viewBounds[2] = cameraX + 240;\n      viewBounds[3] = cameraY + 320;\n   }\n",
     "      viewBounds[2] = cameraX + 240;\n      viewBounds[3] = cameraY + 320;\n      PortConfig.updateView();\n   }\n", 1),
    ("Actor.java",
     "               Game.viewBounds[0] = Game.cameraX;\n               Game.viewBounds[2] = Game.cameraX + 240;\n",
     "               Game.viewBounds[0] = Game.cameraX;\n               Game.viewBounds[2] = Game.cameraX + 240;\n"
     "               PortConfig.updateView();\n", 1),
    # a new level starts from a fresh (unsmoothed) view
    ("Game.java",
     "   private static void initializeCamera() {\n",
     "   private static void initializeCamera() {\n      PortConfig.resetView();\n", 1),
    # activation/visibility distance scales with the visible width (120 = half of the original screen)
    ("Actor.java",
     "      int var1 = this.positionX - (Game.cameraX + 120);\n",
     "      int var1 = this.positionX - PortConfig.viewCenterX();\n", 1),
    ("Actor.java",
     "      this.cameraDistanceBucket = var1 / 120 + var2 / 160;\n",
     "      this.cameraDistanceBucket = var1 / PortConfig.viewHalfWidth() + var2 / 160;\n", 1),

    # ------------------------------------------------------------------ world layer
    ("Game.java",
     "   private static void drawWorld() {\n"
     "      if (Actor.cu) {\n"
     "         a(cameraX + Actor.cv % 240, 0, true);\n"
     "         d(cameraX + Actor.cv % 240, 0, 2);\n"
     "      } else if (levelModes[levelIndex] == 3) {\n"
     "         a(cameraX, cameraY < 0 ? 0 : cameraY, true);\n"
     "         d((cameraX >> 3) % (bV * 20), cameraY < 0 ? 0 : cameraY, 2);\n"
     "      } else {\n"
     "         a(cameraX, cameraY, true);\n"
     "         d(cameraX, cameraY < 0 ? 0 : cameraY, 1);\n"
     "      }\n"
     "\n"
     "      setClip(0, 0, 240, 320);\n",
     "   static void portTile(Graphics var0, int var1, int var2, int var3) {\n"
     "      a(var0, var1, var2, var3);\n"
     "   }\n"
     "\n"
     "   private static void portTiles(int var0, int var1) {\n"
     "      if (PortConfig.screenWidth <= 240 && !PortConfig.directTiles) {\n"
     "         a(var0, var1, true);\n"
     "      } else {\n"
     "         PortConfig.drawTiles(graphics, var0, var1);\n"
     "         PortNative.layerWorld(PortConfig.worldTx - cameraX, -cameraY);\n"
     "      }\n"
     "   }\n"
     "\n"
     "   private static void drawWorld() {\n"
     "      PortConfig.beginWorld(graphics);\n"
     "      if (Actor.cu) {\n"
     "         portTiles(cameraX + Actor.cv % 240, 0);\n"
     "         d(cameraX + Actor.cv % 240, 0, 2);\n"
     "      } else if (levelModes[levelIndex] == 3) {\n"
     "         portTiles(cameraX, cameraY < 0 ? 0 : cameraY);\n"
     "         d((cameraX >> 3) % (bV * 20), cameraY < 0 ? 0 : cameraY, 2);\n"
     "      } else {\n"
     "         portTiles(cameraX, cameraY);\n"
     "         d(cameraX, cameraY < 0 ? 0 : cameraY, 1);\n"
     "      }\n"
     "\n"
     "      PortConfig.clipWorld(graphics);\n", 1),
    # the (empty) background hook only resets the clip
    ("Game.java",
     "      boolean var8 = false;\n      setClipInternal(0, 0, 240, 320);\n      boolean var3 = false;\n",
     "      boolean var8 = false;\n      PortConfig.clipWorld(graphics);\n      boolean var3 = false;\n", 1),
    ("Game.java",
     "   private static void drawActors() {\n      setClipInternal(0, 0, 240, 320);\n",
     "   private static void drawActors() {\n      PortConfig.clipWorld(graphics);\n", 1),
    # label the draw calls of each actor so presentation can interpolate its motion between ticks
    ("Game.java",
     "         if ((var3 = paintActors[var5]) != null) {\n            var3.paint();\n         }\n      }\n   }\n",
     "         if ((var3 = paintActors[var5]) != null) {\n"
     "            PortNative.layerActor(var3, var3.positionX - cameraX + PortConfig.worldTx, var3.positionY - cameraY);\n"
     "            var3.paint();\n         }\n      }\n\n"
     "      PortNative.layerWorld(PortConfig.worldTx - cameraX, -cameraY);\n   }\n", 1),
    # spell charge effect drawn at the player
    ("Game.java",
     "            setClip(0, 0, 240, 320);\n\n            for (int var14 = 0; var14 < 20; var14++) {\n",
     "            PortConfig.clipWorld(graphics);\n\n            for (int var14 = 0; var14 < 20; var14++) {\n", 1),
    ("Game.java",
     "            setClip(0, 0, 240, 320);\n         } else {\n            if (var0 == 3) {\n",
     "            PortConfig.clipWorld(graphics);\n         } else {\n            if (var0 == 3) {\n", 1),
    # energy-barrier lightning: on-screen test in world-relative x
    ("Game.java",
     "      if (var0 <= 320 && var3 >= 0 && var2 > 0 && var2 <= 240) {\n",
     "      if (var0 <= 320 && var3 >= 0 && var2 > PortConfig.worldLeft() && var2 <= PortConfig.worldRight()) {\n", 1),
    # end of the world layer; rain and the modal message box are screen space
    ("Game.java",
     "      D();\n      if (aS && bc == 14) {\n         setClipInternal(0, 0, 240, 320);\n         d();\n      }\n",
     "      PortConfig.endWorld(graphics);\n      D();\n      if (aS && bc == 14) {\n         PortConfig.beginUi(graphics);\n"
     "         setClipInternal(0, 0, 240, 320);\n         d();\n         PortConfig.endUi(graphics);\n      }\n", 1),
    # stun stars follow the player in world space
    ("Game.java",
     "         sprites[35].drawAnimationFrame(graphics, 2, stateFrame % 4 < 2 ? 0 : 1, player.positionX - cameraX - 0, player.positionY - cameraY - 0, 0, 0, 0);\n",
     "         PortConfig.pushWorld(graphics);\n"
     "         sprites[35].drawAnimationFrame(graphics, 2, stateFrame % 4 < 2 ? 0 : 1, player.positionX - cameraX - 0, player.positionY - cameraY - 0, 0, 0, 0);\n"
     "         PortConfig.endWorld(graphics);\n", 1),
    # dialogue box, cutscene softkeys and the boss bar keep their 240 layout, centred
    ("Game.java",
     "      if (bc == 5 && aS) {\n         e();\n      }\n",
     "      if (bc == 5 && aS) {\n         PortConfig.beginUi(graphics);\n         e();\n         PortConfig.endUi(graphics);\n      }\n", 1),
    ("Game.java",
     "         drawSoftkeyBar(bc != 17 && ch ? getString(0, 20) : \"\", bc == 5 ? getString(0, 17) : \"\", true);\n",
     "         PortConfig.beginUi(graphics);\n"
     "         drawSoftkeyBar(bc != 17 && ch ? getString(0, 20) : \"\", bc == 5 ? getString(0, 17) : \"\", true);\n"
     "         PortConfig.endUi(graphics);\n", 1),
    ("Game.java",
     "         c(Actor.activeBoss.health, Actor.bossInitialHealth);\n",
     "         PortConfig.beginUi(graphics);\n         c(Actor.activeBoss.health, Actor.bossInitialHealth);\n         PortConfig.endUi(graphics);\n", 1),
    # screen fade
    ("Game.java",
     "         fillRect(0, 0, 240, 320);\n         aw--;\n",
     f"         fillRect(0, 0, {W}, 320);\n         aw--;\n", 1),
    # cutscene / level-end bars span the screen
    ("Game.java",
     "            setClipInternal(0, 0, 240, 320);\n            setColor(-587202560);\n            fillRect(0, 0, 240, aN);\n            fillRect(0, 320 - aN, 240, aN);\n",
     f"            setClipInternal(0, 0, {W}, 320);\n            setColor(-587202560);\n            fillRect(0, 0, {W}, aN);\n            fillRect(0, 320 - aN, {W}, aN);\n", 1),
    # the two remaining, less indented pairs (the more indented block above must be applied first)
    ("Game.java", "         fillRect(0, 0, 240, aN);\n", f"         fillRect(0, 0, {W}, aN);\n", 2),
    ("Game.java", "         fillRect(0, 320 - aN, 240, aN);\n", f"         fillRect(0, 320 - aN, {W}, aN);\n", 2),
    # rain and lightning flashes cover the screen (30 drops spread across the width; 240 / 30 = 8)
    ("Game.java",
     "                  cK[var0] = (short)(var0 * 8);\n",
     f"                  cK[var0] = (short)(var0 * {W} / 30);\n", 1),
    ("Game.java",
     "                  while (cK[var3] > 240) {\n                     cK[var3] = (short)(cK[var3] - 240);\n",
     f"                  while (cK[var3] > {W}) {{\n                     cK[var3] = (short)(cK[var3] - {W});\n", 1),
    ("Game.java",
     "                     cK[var3] = (short)(cK[var3] + 240);\n",
     f"                     cK[var3] = (short)(cK[var3] + {W});\n", 1),
    ("Game.java",
     "                  fillRect(0, 0, 240, 320);\n                  cS = false;\n",
     f"                  fillRect(0, 0, {W}, 320);\n                  cS = false;\n", 1),
    ("Game.java",
     "                  directGraphics.setARGBColor(cV[var2]);\n                  fillRect(0, 0, 240, 320);\n",
     f"                  directGraphics.setARGBColor(cV[var2]);\n                  fillRect(0, 0, {W}, 320);\n", 1),

    # ------------------------------------------------------------------ panels drawn over the world
    ("Game.java",
     "      drawWorld();\n      setClip(0, 0, 240, 320);\n      g(7, 20, 226, 265);\n",
     "      drawWorld();\n      PortConfig.beginUi(graphics);\n      setClip(0, 0, 240, 320);\n      g(7, 20, 226, 265);\n", 1),
    ("Game.java",
     "            playSoundOnce(-1);\n            restartLevel();\n         }\n      }\n   }\n",
     "            playSoundOnce(-1);\n            restartLevel();\n         }\n      }\n\n      PortConfig.endUi(graphics);\n   }\n", 1),
    ("Game.java",
     "      drawWorld();\n      a(53, 80, 134, 157, 187);\n",
     "      drawWorld();\n      PortConfig.beginUi(graphics);\n      a(53, 80, 134, 157, 187);\n", 1),
    ("Game.java",
     "      if (!confirmationOpen) {\n         navigateMenu(menuItems[1].length, false);\n      }\n   }\n",
     "      if (!confirmationOpen) {\n         navigateMenu(menuItems[1].length, false);\n      }\n\n      PortConfig.endUi(graphics);\n   }\n", 1),
    ("Game.java",
     "      drawWorld();\n      g(0, 0, 240, 320);\n      drawSoftkeys(\"\", getString(0, 17));\n",
     "      drawWorld();\n      PortConfig.beginUiOpaque(graphics);\n      g(0, 0, 240, 320);\n      drawSoftkeys(\"\", getString(0, 17));\n", 1),
    ("Game.java",
     "         aM = 0;\n         finishLevel();\n      }\n   }\n",
     "         aM = 0;\n         finishLevel();\n      }\n\n      PortConfig.endUi(graphics);\n   }\n", 1),
    ("Game.java",
     "      drawWorld();\n      k = true;\n      directGraphics.setARGBColor(-587202560);\n",
     "      drawWorld();\n      PortConfig.beginUi(graphics);\n      k = true;\n      directGraphics.setARGBColor(-587202560);\n", 1),
    ("Game.java",
     "            dr = false;\n            cm = true;\n         }\n      }\n   }\n",
     "            dr = false;\n            cm = true;\n         }\n      }\n\n      PortConfig.endUi(graphics);\n   }\n", 1),

    # ------------------------------------------------------------------ hazard-area fill (Actor type 78)
    ("Actor.java",
     "      int var9 = Math.max(var5, 0);\n      int var10 = Math.min(240, var7) - var9;\n      var10 = Math.min(240, var10);\n",
     "      int var9 = Math.max(var5, PortConfig.worldLeft());\n      int var10 = Math.min(PortConfig.worldRight(), var7) - var9;\n"
     f"      var10 = Math.min({W}, var10);\n", 1),
    ("Actor.java",
     "      if (var9 <= 240 && var11 <= 340 && var10 > 0 && var12 > 0) {\n         graphics.setColor(16711680);\n         Game.setClip(0, 0, 240, 320);\n",
     "      if (var9 <= PortConfig.worldRight() && var11 <= 340 && var10 > 0 && var12 > 0) {\n         graphics.setColor(16711680);\n"
     "         PortConfig.clipWorld(Game.graphics);\n", 1),
    ("Actor.java",
     "               Game.setClip(0, 0, 240, 320);\n            }\n         }\n\n         Game.setClip(0, 0, 240, 320);\n",
     "               PortConfig.clipWorld(Game.graphics);\n            }\n         }\n\n         PortConfig.clipWorld(Game.graphics);\n", 1),
]
