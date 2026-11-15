/**
 * Desktop English text. The supplied v1.3.7 JAR carries Czech strings (pack 15); these replace what
 * the player sees when language=english. Indices are (string table, entry) exactly as Game.getString
 * receives them: table 0 is the interface, tables 2..10 are the nine levels' dialogue.
 *
 * Markup is the game's own: ^ line break, {..} highlight, ~N inline icon, # digit placeholder.
 * Only glyphs present in the game font are used. Key hints name the desktop defaults; the original
 * keypad digits still work on the number row. Credited names are never altered.
 */
final class PortEnglish {
   private static String[][] tables;

   private static final String CONTROLS = "ARROWS OR A/D: MOVE^^SPACE OR UP: JUMP^^DOWN: CROUCH^^J: ATTACK^^Q: SWITCH WEAPON^^K: MAGIC^^ ";

   private static final String[] CREDIT_HEADINGS = new String[]{
      "EXECUTIVE PRODUCERS", "PRODUCERS", "GAME DESIGN MANAGERS", "CREATIVE LEAD", "GAME DESIGN", "ART MANAGERS", "LEAD ARTIST", "ART",
      "PROGRAMMING MANAGERS", "LEAD PROGRAMMER", "PROGRAMMING", "AUDIO LEAD", "AUDIO", "QUALITY ASSURANCE MANAGERS",
      "QUALITY ASSURANCE LEADS", "QUALITY ASSURANCE", "LOCALIZATION MANAGER", "LOCALIZATION", "STUDIO MANAGER", "SPECIAL THANKS"
   };

   private static void build() {
      tables = new String[11][];
      String[] ui = new String[103];
      ui[0] = "MAIN MENU";
      ui[1] = "NEW GAME";
      ui[2] = "CONTINUE";
      ui[3] = "SELECT LEVEL";
      ui[4] = "MORE GAMES!";
      ui[5] = "GAMELOFT LIVE!";
      ui[6] = "SOUND";
      ui[7] = "HELP";
      ui[8] = "ABOUT";
      ui[9] = "EXIT";
      ui[10] = "PRESS ANY KEY";
      ui[11] = "LEVEL";
      ui[12] = "RESUME";
      ui[13] = "RESTART";
      ui[14] = "ARE YOU SURE?";
      ui[15] = "YES";
      ui[16] = "NO";
      ui[17] = "NEXT";
      ui[18] = "BACK";
      ui[19] = "CHANGE";
      ui[20] = "SKIP";
      ui[21] = "OK";
      ui[22] = "ENABLE SOUND?";
      ui[23] = "ON";
      ui[24] = "OFF";
      ui[25] = "LOADING";
      ui[26] = "TRAVELLING...";
      ui[27] = "DO YOU WANT TO RESTART?";
      ui[30] = "LEFT SOFT KEY";
      ui[31] = CONTROLS;
      ui[32] = "~0 HEALS YOUR WOUNDS^^~2 RESTORES YOUR MAGIC";
      ui[33] = "~1^COLLECT 4 LIFE CRYSTALS TO INCREASE YOUR MAXIMUM HEALTH.^^^~3^COLLECT 4 MAGIC CRYSTALS TO INCREASE YOUR MAXIMUM MAGIC.";
      ui[34] = "A GAMEPAD WORKS TOO: A JUMP, X ATTACK, B MAGIC, Y SWITCH WEAPON. PRESS F1 FOR ALL KEYS.";
      ui[35] = "CONTROLS^^ENTER = OK^ARROWS = MOVE^ESC = BACK";
      ui[36] = "MENU";
      ui[37] = "DID YOU LIKE THIS GAME? CHECK OUT MORE GAMES FROM GAMELOFT!";
      ui[38] = "YOUR SAVED GAME WILL BE ERASED. ARE YOU SURE?";
      ui[40] = "DARKNESS HAS ALWAYS CAST AN OMINOUS SHADOW OVER HUMAN HISTORY, AND NO LEGEND WAS MORE TERRIFYING THAN THAT OF THE VAMPIRES.^^"
         + "WHEN KALE WAS A CHILD, VAMPIRES ATTACKED HIS FAMILY AND BOTH HIS PARENTS DIED. KALE DEVOTED HIS LIFE TO HUNTING VAMPIRES AND "
         + "NEVER STOPPED LEARNING HOW TO FIGHT THEM AND HOW TO SURVIVE.^^"
         + "AFTER THREE YEARS OF RELENTLESS SEARCHING, KALE FINALLY FOUND A LEAD IN THE TOWN OF OMINOS. ITS PEOPLE SUFFER CONSTANT ATTACKS "
         + "BY THE UNDEAD AND WEREWOLVES. ''A POWERFUL VAMPIRE MUST BE RULING THEM ALL,'' KALE BELIEVES. GRIPPING HIS FIRE SWORD, HE ENTERS "
         + "THE TOWN TO TAKE HIS REVENGE...";
      String[] chapters = new String[]{
         "CHAPTER", "THE CHALLENGE", "THE INCIDENT", "WANDERING", "SKIRMISH", "ENCOUNTER", "LABYRINTH", "AWAKENING", "THE FALL", "THE ABYSS",
         "CLASH", "SALVATION"
      };

      for (int i = 0; i < chapters.length; i++) {
         ui[41 + i] = chapters[i];
         ui[87 + i] = chapters[i];
      }

      ui[53] = "YOU GOT THE ICE SPEAR! PRESS {K} TO CAST ICE MAGIC! PRESS {Q} TO SWITCH WEAPONS!";
      ui[54] = "GREEN ORBS ARE LIFE STONES. THEY HEAL YOUR WOUNDS!";
      ui[55] = "BLUE ORBS ARE MAGIC STONES. THEY RESTORE YOUR MAGIC!";
      ui[56] = "YOU GOT A LIFE CRYSTAL! FIND # MORE TO INCREASE YOUR MAXIMUM HEALTH.";
      ui[57] = "YOU GOT A MAGIC CRYSTAL! FIND # MORE TO INCREASE YOUR MAXIMUM MAGIC.";
      ui[58] = "MAXIMUM HEALTH INCREASED!";
      ui[59] = "MAXIMUM MAGIC INCREASED!";
      ui[60] = "YOU GOT THE {STONE OF IMMORTALITY}! NOTHING CAN HURT YOU WHILE THE AURA OF IMMORTALITY SURROUNDS YOU.";
      ui[61] = "YOU GOT {MAGIC} POWER! {PRESS K} TO USE MAGIC.";
      ui[62] = CONTROLS;
      ui[63] = CONTROLS;
      ui[64] = "IF YOU CANNOT SOLVE A PUZZLE, TRY ANOTHER WEAPON OR SPELL.";
      ui[65] = "LIFE CRYSTALS AND MAGIC CRYSTALS ARE HIDDEN IN EVERY LEVEL. FIND THEM TO INCREASE YOUR HEALTH AND MAGIC.";
      ui[66] = ui[64];
      ui[67] = "FALLING INTO LAVA MEANS INSTANT DEATH.";
      ui[68] = "YOU CAN ALWAYS RETURN TO A CHAPTER YOU HAVE ALREADY FINISHED.";
      ui[69] = "SOME WEAPONS ARE MORE EFFECTIVE AGAINST CERTAIN ENEMIES. CHOOSING YOUR WEAPON WISELY CAN MAKE A FIGHT EASIER.";
      ui[70] = ui[67];
      ui[71] = "THIS IS A CREATURE OF THE WATER ELEMENT. THE {ICE SPEAR} MIGHT BE MORE EFFECTIVE.";
      ui[72] = "POWER";
      ui[73] = "COMBO";
      ui[74] = "ULTIMATE";
      ui[75] = "MAGIC";
      ui[76] = "LV.";
      ui[77] = "{KALE}";
      ui[78] = "{RITTER}";
      ui[79] = "{LYDIA}";
      ui[80] = "{ZOMBIE}";
      ui[81] = "{IMP}";
      ui[82] = "{WEREWOLF}";
      ui[83] = "{VAMPIRESS}";
      ui[84] = "GAME OVER";
      ui[85] = "LEVEL COMPLETE";
      ui[86] = "MORE GAMES!";
      ui[99] = "ARROWS OR A/D: MOVE^SPACE OR UP: JUMP^DOWN: CROUCH^J: ATTACK^Q: SWITCH WEAPON^K: MAGIC^ ";
      ui[100] = "~1COLLECT 4 LIFE CRYSTALS TO INCREASE YOUR MAXIMUM HEALTH.^^~3COLLECT 4 MAGIC CRYSTALS TO INCREASE YOUR MAXIMUM MAGIC.";
      ui[101] = "PRESS ANY KEY";
      ui[102] = "OPTIONS";
      tables[0] = ui;
      tables[2] = new String[]{
         "SO THIS IS THE CASTLE PEOPLE WERE TALKING ABOUT! I SHOULD EXPLORE IT WITH THE UTMOST CAUTION.",
         "WELL, WELL! WHAT LUCK! I WAS JUST WONDERING WHAT TO HAVE FOR DINNER! FRESH MEAT DOES THE UNDEAD GOOD.",
         "(THIS MONSTER REEKS HORRIBLY!) WHERE IS YOUR MASTER? THE UNDEAD ALWAYS SERVE A VAMPIRE!",
         "HUMANS AND THEIR ILLUSIONS! OUR MASTER IS ALMIGHTY! YOU WILL NOT MEET HIM WHILE YOU LIVE, SO BECOME MY DINNER NOW. "
            + "I WILL BE GENTLE WITH YOU, DO NOT BE AFRAID."
      };
      tables[3] = new String[]{
         "I AM LOOKING FOR YOUR MASTER, THE LORD OF THE VAMPIRES. TELL ME WHERE TO FIND HIM AND I WILL LET YOU GO.",
         "I'LL TELL YOU WHAT A GREAT BOMB WE HAVE!",
         "HA... PATHETIC HUMAN! I WILL NEVER REVEAL THAT OUR MASTER IS TRAVELLING THROUGH THE FOREST OF WAILS!",
         "...YOU JUST TOLD HIM! DON'T YOU UNDEAD EVER THINK?",
         "REALLY? SORRY.",
         "THANKS FOR THE INFORMATION! ANYONE WHO STANDS IN MY WAY WILL GET WHAT THEY DESERVE!",
         "I'LL SHOW YOU THE TASTE OF OUR STEEL!"
      };
      tables[4] = new String[]{
         "WHAT STRANGE POWER HAVE I GAINED? IT FEELS AS IF SOMETHING IS GROWING INSIDE ME... EITHER WAY, I MUST GO ON."
      };
      tables[5] = new String[]{
         "AT LAST I HAVE YOU, VILLAIN! DO YOU REMEMBER ME?",
         "YOU?",
         "YOU KILLED MY PARENTS! I HAVE BEEN SEARCHING FOR YOU FOR THREE YEARS!",
         "TO TELL THE TRUTH, BOY, I DO NOT EVEN REMEMBER. SO MANY PEOPLE HAVE GIVEN THEIR LIVES TO MY IMMORTAL BODY. I THINK YOU SHOULD APPRECIATE THAT.",
         "HOW DARE YOU! I WILL MAKE YOU FEEL ALL OF MY PAIN.",
         "GO AHEAD, BOY! SHOW ME WHAT POWER YOU COMMAND.",
         "ADMIRABLE! YOU ARE STRONGER THAN HUMANS USUALLY ARE. I WILL GLADLY PLAY WITH YOU AGAIN. KINDLY COME TO MY CASTLE, EAST OF THE FOREST. "
            + "YOU WILL BE EXPECTED, HAHAHA...",
         "OH NO! HE'S GONE!"
      };
      tables[6] = new String[]{"HOW WILL I FIND HIM AGAIN? I SHOULD PROBABLY GET OUT OF THE FOREST FIRST."};
      tables[7] = new String[]{
         "THEY SAY I MUST CROSS MOUNT AIUR TO REACH MY GOAL. THIS IS THE ONLY WAY TO THE SUMMIT... I'LL TRY IT."
      };
      tables[8] = new String[]{
         "AH... IT IS FREEZING IN THIS CAVE! THE BLOOD IN MY VEINS IS TURNING TO ICE. IF ONLY THERE WERE ANOTHER WAY TO THE OTHER SIDE OF THE MOUNTAIN! "
            + "I MUST GET THROUGH THE CAVE AS FAST AS I CAN."
      };
      tables[9] = new String[]{
         "AT ITS END THIS ICE CAVE LEADS INTO ANOTHER CAVE FULL OF FLAMES AND GLOWING LAVA. WHAT A TREACHEROUS PLACE! I HAD BETTER HURRY."
      };
      tables[10] = new String[]{
         "HAHAHA... WELCOME TO MY CASTLE! YOU ARE DIFFERENT FROM ALL THOSE DULL WARRIORS. WON'T YOU JOIN US? I WILL SHARE MY SECRET OF IMMORTALITY WITH YOU.",
         "SECRET? YOU CANNOT EVEN SURVIVE WITHOUT HUMAN BLOOD! YOU TAKE PEOPLE'S LIVES IN EXCHANGE FOR YOUR LIES. YOU ARE THE ROOT OF ALL EVIL! "
            + "TODAY I WILL END YOUR REIGN OF TERROR ONCE AND FOR ALL!",
         "I SEE... THEN TODAY I WILL SHOW YOU MY TRUE POWER!",
         "WHAT... NO! DEFEATED... I CANNOT BELIEVE IT!",
         "YOU SHOULD USE YOUR LAST MOMENTS TO BEG FORGIVENESS FOR EVERYTHING YOU HAVE DONE.",
         "THOSE PEOPLE WANTED IMMORTALITY AND GAVE UP THEIR BLOOD WILLINGLY! SOME SURVIVED AND BECAME VAMPIRES, MOST DIED. BUT THAT WAS THEIR OWN FAULT! "
            + "THEY DESERVED IT.",
         "YOU LIE!",
         "ONE DAY YOU WILL SEE THAT I WAS RIGHT. THE MOST DANGEROUS THINGS IN THE WORLD ARE NOT VAMPIRES, BUT YOU HUMANS. AH... I AM BLEEDING, "
            + "MY POWER... IS LEAVING ME. I WILL LIE DOWN TO SLEEP IN THE DARKNESS...",
         "IT IS OVER FOR HIM. MOTHER, FATHER, REST IN PEACE."
      };
   }

   /** Credits keep every name and line of the original; only the bracketed headings and two labels are translated. */
   private static String credits(String original) {
      StringBuffer out = new StringBuffer();
      int heading = 0;
      int i = 0;

      while (i < original.length()) {
         char c = original.charAt(i);
         if (c == '{') {
            int end = original.indexOf("}", i);
            if (end < 0 || heading >= CREDIT_HEADINGS.length) {
               return original;
            }

            out.append("{").append(CREDIT_HEADINGS[heading++]).append("}");
            i = end + 1;
         } else {
            out.append(c);
            i++;
         }
      }

      if (heading != CREDIT_HEADINGS.length) {
         return original;
      }

      String s = out.toString();
      s = swap(s, "VERZE:", "VERSION:");
      return swap(s, "INFO A PODPORA:", "INFO AND SUPPORT:");
   }

   private static String swap(String s, String from, String to) {
      int at = s.indexOf(from);
      return at < 0 ? s : s.substring(0, at) + to + s.substring(at + from.length());
   }

   static String get(int table, int index, String original) {
      if (tables == null) {
         build();
      }

      if (original == null) {
         return null;
      }

      if (table == 0 && index == 28) {
         return credits(original);
      }

      if (table < 0 || table >= tables.length || tables[table] == null || index < 0 || index >= tables[table].length) {
         return null;
      }

      return tables[table][index];
   }
}
