import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HexFormat;
import java.util.List;
import java.util.Objects;

/** Original/recompiled sprite loader/decoder checks and an ignored pixel/bounds oracle. */
public final class SpriteCheck {
    static Path fontOraclePath;
    static long comparisons;
    static final class Access implements AutoCloseable {
        final URLClassLoader loader;
        final Class<?> sprite;
        final boolean renamed;
        Access(Path classes, Path assets, Path stubs, boolean renamed) throws Exception {
            this.renamed = renamed;
            List<URL> urls = new ArrayList<>();
            // Only this checker uses the ImageIO-backed test Image adapter.
            urls.add(SpriteCheck.class.getProtectionDomain().getCodeSource().getLocation());
            urls.add(classes.toUri().toURL()); urls.add(assets.toUri().toURL());
            for (String stub : List.of("midpapi20.jar", "cldcapi11.jar", "nokiaui.jar"))
                urls.add(stubs.resolve(stub).toUri().toURL());
            loader = new URLClassLoader(urls.toArray(new URL[0]), ClassLoader.getPlatformClassLoader());
            sprite = Class.forName(renamed ? "BSprite" : "d", true, loader);
        }
        Object call(Object target, String old, String name, Class<?>[] types, Object... args) throws Exception {
            Method method = sprite.getDeclaredMethod(renamed ? name : old, types);
            method.setAccessible(true);
            return method.invoke(target, args);
        }
        Object get(Object target, String old, String name) throws Exception {
            Field field = sprite.getDeclaredField(renamed ? name : old);
            field.setAccessible(true); return field.get(target);
        }
        Object load(byte[] data) throws Exception {
            Constructor<?> constructor = sprite.getDeclaredConstructor(); constructor.setAccessible(true);
            Object target = constructor.newInstance();
            call(target, "a", "load", new Class<?>[]{byte[].class, int.class}, data, 0);
            Class<?> reporter = Class.forName(renamed ? "ErrorReporter" : "c", true, loader);
            Field field = reporter.getDeclaredField(renamed ? "firstException" : "a"); field.setAccessible(true);
            if (field.get(null) != null) throw new AssertionError("Sprite loader swallowed an exception", (Exception)field.get(null));
            return target;
        }
        public void close() throws Exception { loader.close(); }
    }
    static int u16(byte[] bytes, int at) { return (bytes[at]&255) | ((bytes[at+1]&255)<<8); }
    static int i32(byte[] bytes, int at) { return u16(bytes, at) | (u16(bytes, at+2)<<16); }
    static void equal(Object a, Object b, String message) {
        ++comparisons;
        if (!Objects.deepEquals(a, b)) throw new AssertionError(message);
    }
    static String hash(int[] pixels) throws Exception {
        MessageDigest hash = MessageDigest.getInstance("SHA-256");
        for (int p : pixels) hash.update(new byte[]{(byte)(p>>>24), (byte)(p>>>16), (byte)(p>>>8), (byte)p});
        return HexFormat.of().formatHex(hash.digest());
    }
    static int number(Object image, String name) throws Exception { return (int)image.getClass().getMethod(name).invoke(image); }
    static int[] imagePixels(Object image, int x, int y, int width, int height) throws Exception {
        int[] pixels = new int[width*height];
        image.getClass().getMethod("getRGB", int[].class, int.class, int.class, int.class, int.class, int.class, int.class)
            .invoke(image, pixels, 0, width, x, y, width, height);
        return pixels;
    }
    static Object image(Access access, Object sprite, int group, int palette, int flags) throws Exception {
        Object images = access.get(sprite, "H", "cachedImages");
        return (flags & 4) != 0 ? Array.get(Array.get(images, group), palette) : Array.get(Array.get(images, palette), 0);
    }
    static void checkSprite(Access a, Access b, byte[] data, int pack, int id, PrintWriter oracle) throws Exception {
        Object original = a.load(data), reference = b.load(data);
        String context = pack + ":" + id;
        String[][] fields = {{"b","moduleCount"},{"c","moduleX"},{"d","moduleY"},{"e","moduleImageIndex"},
            {"f","moduleWidth"},{"g","moduleHeight"},{"h","frameModuleCount"},{"i","frameModuleStart"},
            {"j","frameCollisionStart"},{"k","collisionRects"},{"l","frameRects"},{"m","frameModules"},
            {"n","animationFrameCount"},{"o","animationFrameStart"},{"p","animationFrames"},{"v","palettes"},
            {"w","pngPaletteChunks"},{"x","paletteCount"},{"y","pngColorCount"},{"A","hasAlpha"},
            {"B","featureFlags"},{"C","pixelCodec"},{"D","packedColorMask"},{"E","packedColorBits"},
            {"F","imageData"},{"G","moduleDataOffset"}};
        for (String[] names : fields) equal(a.get(original,names[0],names[1]), b.get(reference,names[0],names[1]), context+" "+names[1]);
        int flags = (int)a.get(original,"B","featureFlags");
        if ((flags & 536870912) != 0 && (flags & 4) == 0) {
            int palettes = (int)a.get(original,"x","paletteCount");
            for (int palette = 0; palette < palettes; ++palette) {
                Class<?>[] signature = {int.class,int.class,int.class,int.class};
                a.call(original,"a","buildImages",signature,palette,0,-1,-1);
                b.call(reference,"a","buildImages",signature,palette,0,-1,-1);
            }
        }
        if ((flags & 16777216) == 0) {
            Object images = a.get(original,"H","cachedImages");
            int groups = (flags&4) != 0 ? Array.getLength(images) : 1;
            for (int group = 0; group < groups; ++group) {
                int palettes = (flags&4) != 0 ? Array.getLength(Array.get(images,group)) : Array.getLength(images);
                for (int palette = 0; palette < palettes; ++palette) {
                    Object ia = image(a,original,group,palette,flags), ib = image(b,reference,group,palette,flags);
                    int w = number(ia,"getWidth"), h = number(ia,"getHeight");
                    equal(w,number(ib,"getWidth"),context+" PNG width"); equal(h,number(ib,"getHeight"),context+" PNG height");
                    int[] pixels = imagePixels(ia,0,0,w,h);
                    equal(pixels,imagePixels(ib,0,0,w,h),context+" PNG pixels");
                    oracle.println("image\t"+pack+"\t"+id+"\t"+group+"\t"+palette+"\t"+w+"\t"+h+"\t"+hash(pixels));
                }
            }
        }
        int modules = (int)a.get(original,"b","moduleCount");
        byte[] widths = (byte[])a.get(original,"f","moduleWidth"), heights = (byte[])a.get(original,"g","moduleHeight");
        for (int module = 0; module < modules; ++module) {
            byte[] groups = (byte[])a.get(original,"e","moduleImageIndex");
            int group = (flags&4) != 0 ? groups[module]&255 : 0;
            int palettes;
            if ((flags&16777216) != 0) palettes = ((int[][][])a.get(original,"v","palettes"))[group].length;
            else {
                Object images = a.get(original,"H","cachedImages");
                palettes = (flags&4) != 0 ? Array.getLength(Array.get(images,group)) : Array.getLength(images);
            }
            for (int palette = 0; palette < palettes; ++palette) {
                setPalette(a,original,palette); setPalette(b,reference,palette);
                int w=widths[module]&255, h=heights[module]&255;
                for (int flip = 0; flip < ((flags&16777216) != 0 ? 4 : 1); ++flip) {
                    int[] pa, pb;
                    if ((flags&16777216) != 0) {
                        pa = (int[])a.call(original,"f","decodeModule",new Class<?>[]{int.class,int.class},module,flip);
                        pb = (int[])b.call(reference,"f","decodeModule",new Class<?>[]{int.class,int.class},module,flip);
                        if (pa == null || pb == null) throw new AssertionError(context+" null module decode");
                        pa=Arrays.copyOf(pa,w*h); pb=Arrays.copyOf(pb,w*h);
                    } else {
                        byte[] xs=(byte[])a.get(original,"c","moduleX"), ys=(byte[])a.get(original,"d","moduleY");
                        int x=xs == null ? 0 : xs[module]&255, y=ys == null ? 0 : ys[module]&255;
                        pa=imagePixels(image(a,original,group,palette,flags),x,y,w,h);
                        pb=imagePixels(image(b,reference,group,palette,flags),x,y,w,h);
                    }
                    equal(pa,pb,context+" module pixels");
                    oracle.println("module\t"+pack+"\t"+id+"\t"+palette+"\t"+module+"\t"+flip+"\t"+hash(pa));
                }
            }
        }
        byte[] frameCounts=(byte[])a.get(original,"h","frameModuleCount");
        if (frameCounts != null) for (int frame=0; frame<frameCounts.length; ++frame) {
            short[] starts=(short[])a.get(original,"j","frameCollisionStart");
            for (int rect=-1; rect<=starts[frame+1]-starts[frame]; ++rect) for (int flip=0;flip<4;++flip) {
                int[] ba=new int[4], bb=new int[4];
                Class<?>[] signature={int[].class,int.class,int.class,int.class,int.class,int.class};
                a.call(original,"a","getFrameBounds",signature,ba,frame,rect,flip,0,0);
                b.call(reference,"a","getFrameBounds",signature,bb,frame,rect,flip,0,0);
                equal(ba,bb,context+" frame bounds");
                oracle.println("bounds\t"+pack+"\t"+id+"\t"+frame+"\t"+rect+"\t"+flip+"\t"+ba[0]+","+ba[1]+","+ba[2]+","+ba[3]);
            }
        }
        byte[] animations=(byte[])a.get(original,"n","animationFrameCount");
        if (animations != null) for (int animation=0;animation<animations.length;++animation) {
            equal(a.call(original,"a","getAnimationFrameCount",new Class<?>[]{int.class},animation),
                  b.call(reference,"a","getAnimationFrameCount",new Class<?>[]{int.class},animation),context+" animation count");
            for (int frame=0;frame<(animations[animation]&255);++frame)
                equal(a.call(original,"a","getAnimationFrameDuration",new Class<?>[]{int.class,int.class},animation,frame),
                      b.call(reference,"a","getAnimationFrameDuration",new Class<?>[]{int.class,int.class},animation,frame),context+" frame duration");
        }
        if (pack==1 && id==0) checkFont(a,b,original,reference);
        if ((flags&16777216) != 0) {
            // Cache registration disables software flips: MIDP drawRegion applies them later.
            a.call(original,"d","registerImageCache",new Class<?>[]{int.class},0);
            b.call(reference,"d","registerImageCache",new Class<?>[]{int.class},0);
            a.call(null,"g","createImageCache",new Class<?>[]{int.class,int.class},0,3);
            b.call(null,"g","createImageCache",new Class<?>[]{int.class,int.class},0,3);
            for (String[] names : new String[][]{{"O","cacheBase"},{"P","cacheIndex"},{"L","cacheImageCounts"},
                    {"K","cacheEvictionSlots"},{"N","cacheCapacity"}})
                equal(a.get(original,names[0],names[1]),b.get(reference,names[0],names[1]),context+" image cache");
            for (int flip=1;flip<4;++flip) {
                int[] baseline=(int[])a.call(original,"f","decodeModule",new Class<?>[]{int.class,int.class},0,0);
                baseline=Arrays.copyOf(baseline,(widths[0]&255)*(heights[0]&255));
                int[] transformed=(int[])b.call(reference,"f","decodeModule",new Class<?>[]{int.class,int.class},0,flip);
                equal(baseline,Arrays.copyOf(transformed,baseline.length),context+" cached decode must not flip");
            }
            a.call(null,"c","destroyImageCache",new Class<?>[]{int.class},0);
            b.call(null,"c","destroyImageCache",new Class<?>[]{int.class},0);
        }
    }
    static void checkFont(Access a,Access b,Object original,Object reference) throws Exception {
        for (Access access : List.of(a,b)) {
            Class<?> game=Class.forName(access.renamed ? "Game" : "e",true,access.loader);
            Field field=game.getDeclaredField(access.renamed ? "font" : "a"); field.setAccessible(true);
            field.set(null,access.renamed ? reference : original);
        }
        for (char c : " ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!?".toCharArray())
            equal(a.call(original,"a","getGlyphFrame",new Class<?>[]{char.class},c),
                  b.call(reference,"a","getGlyphFrame",new Class<?>[]{char.class},c),"font glyph mapping");
        for (String text : List.of("SOUL OF DARKNESS","{SOUL} OF DARKNESS","~0 12345","HELLO^WORLD")) {
            if (!text.contains("^"))
                equal(a.call(original,"a","measureText",new Class<?>[]{String.class},text),
                      b.call(reference,"a","measureText",new Class<?>[]{String.class},text),"font width");
            for (int width : new int[]{40,120,240}) {
                a.call(original,"a","wrapText",new Class<?>[]{String.class,int.class},text,width);
                b.call(reference,"a","wrapText",new Class<?>[]{String.class,int.class},text,width);
                equal(a.get(original,"X","wrappedLineEnds"),b.get(reference,"X","wrappedLineEnds"),"font wrapping");
                equal(a.get(original,"Y","wrappedLineCount"),b.get(reference,"Y","wrappedLineCount"),"font line count");
            }
        }
        // Force fresh layouts: the original's global cache ignores width/palette.
        try (PrintWriter out=new PrintWriter(Files.newBufferedWriter(fontOraclePath))) {
            for (char c=32;c<256;c++) {
                Object first=a.call(original,"a","getGlyphFrame",new Class<?>[]{char.class},c);
                equal(first,b.call(reference,"a","getGlyphFrame",new Class<?>[]{char.class},c),"extended font mapping");
                out.println("G\t"+(int)c+"\t"+first);
            }
            for (String text : List.of("", "SOUL OF DARKNESS", "{SOUL} OF DARKNESS", "~0 12345", "HELLO^WORLD",
                    "ONE TWO THREE FOUR FIVE SIX SEVEN EIGHT NINE TEN", "ABCDEFGHIJKLMNO", "A^^B^", "~0 ~1 ~2 ~3 ~4", "{A B C} D E", "lowercase")) {
                StringBuilder hex=new StringBuilder();
                for (char c:text.toCharArray()) hex.append(String.format("%04x",(int)c));
                for (int palette : new int[]{0,5}) for (int width : new int[]{0,40,120,240,569}) {
                    for (Access access : List.of(a,b)) {
                        Object instance=access.renamed ? reference : original;
                        setPalette(access,instance,palette);
                        Field cached=access.sprite.getDeclaredField(access.renamed ? "wrappedText" : "Z"); cached.setAccessible(true); cached.set(null,null);
                        access.call(instance,"a","wrapText",new Class<?>[]{String.class,int.class},text,width);
                    }
                    equal(a.get(original,"X","wrappedLineEnds"),b.get(reference,"X","wrappedLineEnds"),"fresh font wrapping");
                    equal(a.get(original,"Y","wrappedLineCount"),b.get(reference,"Y","wrappedLineCount"),"fresh font lines");
                    Object measured=a.call(original,"a","measureText",new Class<?>[]{String.class},text);
                    equal(measured,b.call(reference,"a","measureText",new Class<?>[]{String.class},text),"fresh font measure");
                    int count=(int)a.get(original,"Y","wrappedLineCount");
                    int[] ends=(int[])a.get(original,"X","wrappedLineEnds");
                    out.print("F\t"+hex+"\t"+palette+"\t"+width+"\t"+measured);
                    for (int i=0;i<count;i++) out.print("\t"+ends[i]);
                    out.println();
                }
            }
        }
        setPalette(a,original,0); setPalette(b,reference,0);
    }
    static void setPalette(Access access,Object sprite,int palette) throws Exception {
        Field field=access.sprite.getDeclaredField(access.renamed ? "currentPalette" : "z"); field.setAccessible(true); field.setInt(sprite,palette);
    }
    public static void main(String[] args) throws Exception {
        fontOraclePath=Path.of(args[3]).resolveSibling("font-oracle.tsv");
        Path assets=Path.of(args[0]), compiled=Path.of(args[1]), stubs=Path.of(args[2]), output=Path.of(args[3]);
        Files.createDirectories(output.getParent());
        try (Access original=new Access(assets,assets,stubs,false); Access reference=new Access(compiled,assets,stubs,true);
             PrintWriter oracle=new PrintWriter(Files.newBufferedWriter(output))) {
            for (int pack : new int[]{1,2,16}) {
                byte[] bytes=Files.readAllBytes(assets.resolve(Integer.toString(pack)));
                for (int id=0;id<u16(bytes,0);++id) {
                    int start=i32(bytes,6+id*4),end=i32(bytes,10+id*4);
                    if (start<end) checkSprite(original,reference,Arrays.copyOfRange(bytes,start+1,end),pack,id,oracle);
                }
            }
            if (oracle.checkError()) throw new AssertionError("Could not write sprite oracle");
        }
        System.out.println("sprite behavior: OK ("+comparisons+" comparisons); oracle: "+output);
    }
}
