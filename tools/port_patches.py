"""Desktop-port changes, applied to soul/src before translation.

Each entry is (file, old, new, expected_match_count); tools/gen_port.py refuses to run when a count
differs. soul/src itself is never modified. Rules:

  * No field is added to Game/Actor/BSprite (port state lives in tools/port_java).
  * Every helper is a no-op at PortConfig.screenWidth == 240 with directTiles off; that
    configuration must stay state-identical to the original bytecode (tools/check_engine.py).
  * 240 is never replaced globally. Each site was read and classified: world clip, tile-layer
    extent, full-screen fill, on-screen test, or 240-wide interface layout (left alone, centred).

This build draws three tile layers (far background through a scroll buffer, a parallax middle
layer, the main map), background actors between them, then actors, weather, a bottom icon bar and
the interface. Order of entries matters where one pattern is a prefix of another; those are noted.
"""

W = "PortConfig.screenWidth"
WORLD_LAYER = "PortNative.layerWorld(PortConfig.worldTx - cameraX, -cameraY);"


def actor_layer(expr: str) -> str:
    return f"PortNative.layerActor({expr}, {expr}.positionX - cameraX + PortConfig.worldTx, {expr}.positionY - cameraY);"


PORT_PATCHES: list[tuple[str, str, str, int]] = [
    # ------------------------------------------------------------------ frame setup
    ("Game.java",
     "            graphics = var1;\n            stateFrame++;\n",
     "            graphics = var1;\n            stateFrame++;\n            PortConfig.beginFrame(graphics, state);\n", 1),
    ("Game.java",
     "            graphics = null;\n            L();\n",
     "            PortConfig.endFrame(graphics);\n            graphics = null;\n            L();\n", 1),

    # ------------------------------------------------------------------ OPTIONS screen (tools/port_java/PortMenu.java)
    # state 40 runs the options screen
    ("Game.java",
     "               case 32:\n                  L(eI);\n            }\n",
     "               case 32:\n                  L(eI);\n                  break;\n               case 40:\n                  PortMenu.update();\n            }\n", 1),
    # menu entry 99: its label, its action, and one more button slot in the pause list. The entry only
    # exists after the host calls portMenus(); until then the menus are byte-for-byte the original ones.
    ("Game.java",
     "      return k(0, fD[var0][var1]);\n",
     "      return fD[var0][var1] == 99 ? PortMenu.TITLE : k(0, fD[var0][var1]);\n", 1),
    ("Game.java",
     "         for (int var3 = 0; var3 < 6; var3++) {\n            b[33].a(graphics, 66, 60, 38 + var3 * 35, 0, 0, 0);\n",
     "         for (int var3 = 0; var3 < fD[1].length; var3++) {\n            b[33].a(graphics, 66, 60, 38 + var3 * 35, 0, 0, 0);\n", 1),
    ("Game.java",
     "               case 12:\n                  a(8);\n                  break;\n               case 13:\n                  eN = true;\n            }\n",
     "               case 12:\n                  a(8);\n                  break;\n               case 13:\n                  eN = true;\n                  break;\n"
     "               case 99:\n                  PortMenu.open();\n            }\n", 1),
    ("Game.java",
     "   private static void aa() {\n",
     "   static void portMenus() {\n"
     "      fD = new byte[][]{{1, 4, 5, 6, 99, 7, 8, 9}, {12, 13, 6, 99, 7, 0, 9}, {1, 2, 3, 4, 5, 6, 99, 7, 8, 9}};\n"
     "   }\n\n   private static void aa() {\n", 1),

    # "#SK" in tutorial strings is the right softkey's name (string 29)
    ("Game.java",
     "      String var2 = fr[0][29];\n",
     "      String var2 = PortConfig.softKeyName(fr[0][29]);\n", 1),

    # ------------------------------------------------------------------ view bounds follow the wide view
    ("Game.java",
     "      viewBounds[2] = cameraX + 240;\n      viewBounds[3] = cameraY + 320;\n   }\n",
     "      viewBounds[2] = cameraX + 240;\n      viewBounds[3] = cameraY + 320;\n      PortConfig.updateView();\n   }\n", 1),
    ("Actor.java",
     "            Game.viewBounds[2] = Game.cameraX + 240;\n",
     "            Game.viewBounds[2] = Game.cameraX + 240;\n            PortConfig.updateView();\n", 1),
    ("Game.java",
     "   private static void aa() {\n      if (aB == -1 || be[levelIndex] != 2) {\n",
     "   private static void aa() {\n      PortConfig.resetView();\n      if (aB == -1 || be[levelIndex] != 2) {\n", 1),
    # activation/visibility distance scales with the visible width (120 = half of the original screen)
    ("Actor.java",
     "               int var1 = this.positionX - (Game.cameraX + 120);\n",
     "               int var1 = this.positionX - PortConfig.viewCenterX();\n", 1),
    ("Actor.java",
     "               this.ak = var1 / 120 + var2 / 160;\n",
     "               this.ak = var1 / PortConfig.viewHalfWidth() + var2 / 160;\n", 1),

    # ------------------------------------------------------------------ world layer: far background
    ("Game.java",
     "   private static void as() {\n      if (Actor.en) {\n         a(cameraX + Actor.eo >> 6, 20, true);\n",
     "   static void portTile(Graphics var0, int var1, int var2, int var3) {\n"
     "      a(var0, var1, var2, var3);\n"
     "   }\n"
     "\n"
     "   private static void portFar(int var0, int var1) {\n"
     "      if (PortConfig.screenWidth <= 240 && !PortConfig.directTiles) {\n"
     "         a(var0, var1, true);\n"
     "      } else {\n"
     "         PortConfig.drawFar(graphics, var0, var1);\n"
     f"         {WORLD_LAYER}\n"
     "      }\n"
     "   }\n"
     "\n"
     "   private static void as() {\n"
     "      PortConfig.beginWorld(graphics);\n"
     "      if (Actor.en) {\n"
     "         portFar(cameraX + Actor.eo >> 6, 20);\n", 1),
    ("Game.java", "            a(cameraX >> 6, 20, true);\n", "            portFar(cameraX >> 6, 20);\n", 1),
    ("Game.java",
     "            a(cameraX * (cC - 13) / (cy - 13), cameraY * (cE - 17) / (cz - 17), true);\n",
     "            portFar(cameraX * (cC - 13) / (cy - 13), cameraY * (cE - 17) / (cz - 17));\n", 1),
    # levels without a far layer clear to black instead
    ("Game.java",
     "            b(0);\n            b(0, 0, 240, 320);\n         }\n\n         if (bo != -1 && bo != -3) {\n",
     "            b(0);\n            b(PortConfig.worldLeft(), 0, PortConfig.worldRight() - PortConfig.worldLeft(), 320);\n"
     "         }\n\n         if (bo != -1 && bo != -3) {\n", 1),
    ("Game.java",
     "            j(0, 0, 240, 320);\n            u();\n",
     "            PortConfig.clipWorld(graphics);\n            u();\n", 1),

    # ------------------------------------------------------------------ middle and main tile layers (drawn directly)
    ("Game.java",
     "      int var5 = var0 / 20;\n      int var6 = var1 / 20;\n      int var7 = (var0 + 240 - 1) / 20;\n"
     "      int var8 = (var1 + 320 - 1) / 20;\n      j(0, 0, 240, 320);\n      if (var1 < 0) {\n         var1 += 20;\n      }\n\n"
     "      int var3 = var5 * 20 - var0;\n",
     "      int var5 = PortConfig.firstColumn(var0);\n      int var6 = var1 / 20 - PortConfig.tileMargin();\n"
     "      int var7 = PortConfig.lastColumn(var0);\n      int var8 = (var1 + 320 - 1) / 20 + PortConfig.tileMargin();\n"
     "      PortConfig.clipWorld(graphics);\n      if (var1 < 0) {\n         var1 += 20;\n      }\n\n"
     "      PortNative.layerTiles(var2, PortConfig.worldTx - var0, -var1);\n"
     "      int var3 = var5 * 20 - var0;\n", 1),
    # the original only wrapped columns past the right end; the wide view can start left of column 0
    ("Game.java",
     "         int var10 = var9;\n         if (var2 == 2 && var9 >= cD) {\n",
     "         int var10 = var9;\n         if (var9 < 0) {\n            var10 = PortConfig.wrapColumn(var9, var2 == 2 ? cD : cy);\n"
     "         } else if (var2 == 2 && var9 >= cD) {\n", 1),
    # margin rows (interpolation only) may lie outside the layer
    ("Game.java",
     "            if (var2 != 0) {\n               if (var2 == 1) {\n                  a(graphics, fm, fn, eC, var3, var4, var10 + var11 * cy);\n",
     "            if (var2 != 0 && (PortConfig.tileMargin() == 0 || PortConfig.rowInside(var11, var2 == 1 ? fm.length : fh.length, var2 == 1 ? cy : cD))) {\n"
     "               if (var2 == 1) {\n                  a(graphics, fm, fn, eC, var3, var4, var10 + var11 * cy);\n", 1),
    ("Game.java",
     "            var4 += 20;\n         }\n\n         var3 += 20;\n      }\n   }\n",
     f"            var4 += 20;\n         }}\n\n         var3 += 20;\n      }}\n\n      {WORLD_LAYER}\n   }}\n", 1),

    # ------------------------------------------------------------------ actors
    ("Game.java",
     "            bQ[var0].v();\n",
     f"            {actor_layer('bQ[var0]')}\n            bQ[var0].v();\n", 1),
    ("Game.java",
     "               bQ[var0].q.v();\n",
     f"               {actor_layer('bQ[var0].q')}\n               bQ[var0].q.v();\n", 1),
    ("Game.java",
     "   private static void an() {\n      j(0, 0, 240, 320);\n",
     "   private static void an() {\n      PortConfig.clipWorld(graphics);\n", 1),
    ("Game.java",
     "         if ((var3 = ch[var5]) != null && (var3.c & 4096) == 0 && var3.type != 41) {\n            var3.v();\n         }\n      }\n   }\n",
     "         if ((var3 = ch[var5]) != null && (var3.c & 4096) == 0 && var3.type != 41) {\n"
     f"            {actor_layer('var3')}\n            var3.v();\n         }}\n      }}\n\n      {WORLD_LAYER}\n   }}\n", 1),
    # actors drawn in front of the darkness rectangles
    ("Game.java",
     "         if ((var1.c & 4096) != 0 && var1.type != 41) {\n            var1.v();\n         }\n",
     f"         if ((var1.c & 4096) != 0 && var1.type != 41) {{\n            {actor_layer('var1')}\n            var1.v();\n"
     f"            {WORLD_LAYER}\n         }}\n", 1),
    # darkness rectangles: on-screen test and left clamp in world-relative x
    ("Game.java",
     "      if (var3 + var5 >= var1 && var4 + var6 >= var2 && var3 <= var1 + 240 && var4 <= var2 + 320) {\n"
     "         int var8;\n         if ((var8 = var3 - var1) < 0 && var7 == 0) {\n            var8 = 0;\n            var5 = var5 + var3 - var1;\n         }\n",
     "      if (var3 + var5 >= var1 + PortConfig.worldLeft() && var4 + var6 >= var2 && var3 <= var1 + PortConfig.worldRight() && var4 <= var2 + 320) {\n"
     "         int var8;\n         if ((var8 = var3 - var1) < PortConfig.worldLeft() && var7 == 0) {\n            var8 = PortConfig.worldLeft();\n"
     "            var5 = var5 + var3 - var1 - PortConfig.worldLeft();\n         }\n", 1),
    ("Game.java",
     "      a(0, 0, 240, 320);\n      if (modalActive && (bJ == 9 || bJ == 12)) {\n",
     "      PortConfig.clipWorld(graphics);\n      if (modalActive && (bJ == 9 || bJ == 12)) {\n", 1),
    # spell charge effect drawn at the player
    ("Game.java",
     "            a(0, 0, 240, 320);\n\n            for (int var14 = 0; var14 < 20; var14++) {\n",
     "            PortConfig.clipWorld(graphics);\n\n            for (int var14 = 0; var14 < 20; var14++) {\n", 1),
    ("Game.java",
     "            a(0, 0, 240, 320);\n         } else {\n            if (var0 == 0) {\n",
     "            PortConfig.clipWorld(graphics);\n         } else {\n            if (var0 == 0) {\n", 1),

    # ------------------------------------------------------------------ end of the world layer; weather is screen space
    ("Game.java",
     "      ar();\n      P();\n      O();\n",
     f"      {WORLD_LAYER}\n      ar();\n      PortConfig.endWorldForWeather(graphics);\n      P();\n      O();\n", 1),
    # rain: 30 streaks spread across the width (240 / 30 = 8). The more indented loop is the rain; do it first.
    ("Game.java",
     "                  dI[var0] = (short)(var0 * 8);\n",
     f"                  dI[var0] = (short)(var0 * {W} / 30);\n", 1),
    ("Game.java",
     "                  while (dI[var3] > 240) {\n                     dI[var3] = (short)(dI[var3] - 240);\n",
     f"                  while (dI[var3] > {W}) {{\n                     dI[var3] = (short)(dI[var3] - {W});\n", 1),
    ("Game.java",
     "                     dI[var3] = (short)(dI[var3] + 240);\n",
     f"                     dI[var3] = (short)(dI[var3] + {W});\n", 1),
    ("Game.java",
     "                  b(dW[0]);\n                  b(0, 0, 240, 320);\n",
     f"                  b(dW[0]);\n                  b(0, 0, {W}, 320);\n", 1),
    ("Game.java",
     "                  b(dW[var2]);\n                  b(0, 0, 240, 320);\n",
     f"                  b(dW[var2]);\n                  b(0, 0, {W}, 320);\n", 1),
    # snow: 20 flakes across 320 (16 apart); now across the width plus the same 80 pixel overscan
    ("Game.java",
     "               dI[var0] = (short)(var0 * 16);\n",
     f"               dI[var0] = (short)(var0 * ({W} + 80) / 20);\n", 1),
    ("Game.java",
     "               while (dI[var3] > 240) {\n                  dI[var3] = (short)(dI[var3] - 240);\n",
     f"               while (dI[var3] > {W}) {{\n                  dI[var3] = (short)(dI[var3] - {W});\n", 1),
    ("Game.java",
     "                  dI[var3] = (short)(dI[var3] + 240);\n",
     f"                  dI[var3] = (short)(dI[var3] + {W});\n", 1),

    # ------------------------------------------------------------------ interface drawn over the world, centred
    # bottom icon bar
    ("Game.java",
     "         graphics.setColor(0);\n         b(0, 293, 320, 28);\n",
     "         PortConfig.beginUiBar(graphics);\n         graphics.setColor(0);\n         b(0, 293, 320, 28);\n", 1),
    ("Game.java",
     "            }\n         }\n      }\n\n      if (modalActive && bJ == 14) {\n         j(0, 0, 240, 320);\n         f();\n      }\n",
     "            }\n         }\n\n         PortConfig.endUi(graphics);\n      }\n\n      if (modalActive && bJ == 14) {\n"
     "         PortConfig.beginUi(graphics);\n         j(0, 0, 240, 320);\n         f();\n         PortConfig.endUi(graphics);\n      }\n", 1),
    # dialogue, tutorial panels and cutscene softkeys
    ("Game.java",
     "      if (bJ == 5 && modalActive) {\n         g();\n      }\n\n      if (modalActive) {\n         if (bJ == 17) {\n            j();\n"
     "         } else if (bJ == 18) {\n            i();\n         }\n      }\n\n      if (hasActiveBackgroundScript()) {\n"
     "         a(bJ != 17 && db ? k(0, 20) : \"\", bJ == 5 ? k(0, 17) : \"\", true);\n      }\n",
     "      PortConfig.beginUi(graphics);\n"
     "      if (bJ == 5 && modalActive) {\n         g();\n      }\n\n      if (modalActive) {\n         if (bJ == 17) {\n            j();\n"
     "         } else if (bJ == 18) {\n            i();\n         }\n      }\n\n      if (hasActiveBackgroundScript()) {\n"
     "         a(bJ != 17 && db ? k(0, 20) : \"\", bJ == 5 ? k(0, 17) : \"\", true);\n      }\n\n"
     "      PortConfig.endUi(graphics);\n", 1),
    # cutscene / level-end bars span the screen whatever translation is active
    ("Game.java",
     "         j(0, 0, 240, 320);\n         b(-587202560);\n         b(0, 0, 240, bs);\n         b(0, 320 - bs, 240, bs);\n",
     f"         j(PortConfig.screenLeft(graphics), 0, {W}, 320);\n         b(-587202560);\n"
     f"         b(PortConfig.screenLeft(graphics), 0, {W}, bs);\n         b(PortConfig.screenLeft(graphics), 320 - bs, {W}, bs);\n", 1),
    # health bar above the transformed player, and the stun stars, follow the player in world space
    ("Game.java",
     "         player.C();\n",
     "         PortConfig.pushWorld(graphics);\n         player.C();\n         PortConfig.endWorld(graphics);\n", 1),
    ("Game.java",
     "         b[33].a(graphics, 4, stateFrame % 4 < 2 ? 0 : 1, player.positionX - cameraX, player.positionY - cameraY - 70, 0, 0, 0);\n",
     "         PortConfig.pushWorld(graphics);\n"
     "         b[33].a(graphics, 4, stateFrame % 4 < 2 ? 0 : 1, player.positionX - cameraX, player.positionY - cameraY - 70, 0, 0, 0);\n"
     "         PortConfig.endWorld(graphics);\n", 1),
    # softkey icons belong to the bottom bar; the HP/MP gauge stays at the screen's left edge
    ("Game.java",
     "            X();\n            Y();\n         }\n\n         c();\n",
     "            X();\n            Y();\n         }\n\n         PortConfig.beginUi(graphics);\n         c();\n         PortConfig.endUi(graphics);\n", 1),
    # boss bar, then: whatever the caller draws after the world (panels, menus) uses the centred layout
    ("Game.java",
     "         f(Actor.E.as, Actor.F);\n      }\n   }\n",
     "         PortConfig.beginUi(graphics);\n         f(Actor.E.as, Actor.F);\n      }\n\n      PortConfig.beginUi(graphics);\n   }\n", 1),

    # ------------------------------------------------------------------ Actor: water and hazard areas
    ("Actor.java",
     "      int var9 = Math.max(var5, 0);\n      int var10 = Math.min(240, var7) - var9;\n      var10 = Math.min(240, var10);\n",
     "      int var9 = Math.max(var5, PortConfig.worldLeft());\n      int var10 = Math.min(PortConfig.worldRight(), var7) - var9;\n"
     f"      var10 = Math.min({W}, var10);\n", 2),
    ("Actor.java",
     "      if (var9 <= 240 && var11 <= 340 && var10 > 0 && var12 > 0) {\n",
     "      if (var9 <= PortConfig.worldRight() && var11 <= 340 && var10 > 0 && var12 > 0) {\n", 2),
    # the 120x160 water tint was drawn four times to cover the screen
    ("Actor.java",
     "            Game.a(Game.j, 0, 0, 20);\n            Game.a(Game.j, 120, 160, 20);\n            Game.a(Game.j, 120, 0, 20);\n"
     "            Game.a(Game.j, 0, 160, 20);\n            Game.a(0, 0, 240, 320);\n",
     "            PortConfig.tileOverlay(Game.j);\n            PortConfig.clipWorld(Game.graphics);\n", 1),
    # remaining full-screen clip resets inside these two painters (deeper indent first)
    ("Actor.java", "               Game.a(0, 0, 240, 320);\n", "               PortConfig.clipWorld(Game.graphics);\n", 2),
    ("Actor.java", "         Game.a(0, 0, 240, 320);\n", "         PortConfig.clipWorld(Game.graphics);\n", 2),
    # a projectile ends when it leaves the screen; a repeated segment is drawn only when on screen
    ("Actor.java",
     "               if (this.collisionBounds[2] < Game.cameraX) {\n                  this.aq = 3;\n               }\n"
     "            } else if (this.collisionBounds[0] > Game.cameraX + 240) {\n",
     "               if (this.collisionBounds[2] < Game.cameraX + PortConfig.worldLeft()) {\n                  this.aq = 3;\n               }\n"
     "            } else if (this.collisionBounds[0] > Game.cameraX + PortConfig.worldRight()) {\n", 1),
    ("Actor.java",
     "            if (this.positionX + 80 >= Game.cameraX && this.positionX <= Game.cameraX + 240) {\n",
     "            if (this.positionX + 80 >= Game.cameraX + PortConfig.worldLeft() && this.positionX <= Game.cameraX + PortConfig.worldRight()) {\n", 1),
]
