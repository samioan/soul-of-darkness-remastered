"""Verify startup artwork and logo/title/menu/credits/gameplay transitions."""
import argparse
import binascii
from pathlib import Path
import struct
import subprocess
from bsprite import Sprite, decode_png
from game_assets import DEFAULT_DATA, resources, require
from check_native_fonts import Reference, glyph


def story_crc(data, width, y):
    """Independent composition of the English story and original font layout."""
    # Independent expected desktop translation; the supplied resource is Czech.
    story_text = (
        'Darkness has always cast an ominous shadow over human history. No legend was more terrifying than that of the vampires.^^'
        'When Kale was a child, vampires attacked his family and killed both his parents. He devoted his life to hunting vampires, learning how to fight them and survive.^^'
        'After three years of searching, Kale finally finds a lead in the town of Ominos. The townspeople suffer constant attacks by the undead and werewolves. '
        'A powerful vampire must be ruling them. Gripping his fire sword, Kale enters the town to take his revenge...'
    ).upper()
    font = Reference(data)
    pixels = [0xff000000]*(width*320)

    def line(text, top, palette, clip):
        x = width//2-font.measure(text)//2
        if top < clip[1]-20 or top > clip[3]: return palette
        for c in text:
            if c == '{': palette = 1; continue
            if c == '}': palette = 0; continue
            dx, dy, w, h, image = font.image(False, glyph(c), palette)
            for py in range(h):
                for px in range(w):
                    xx, yy = x+dx+px, top+dy+py
                    if clip[0] <= xx < clip[2] and clip[1] <= yy < clip[3] and image[py*w+px] >> 24:
                        pixels[yy*width+xx] = image[py*w+px]
            x += font.font.frame_rects[glyph(c)][2]-1
        return palette

    story = '}'+story_text
    start = palette = 0
    left = (width-240)//2
    for row, end in enumerate(font.wrap(story, 230, 0)):
        palette = line(story[start:end], y+row*(font.height+8), palette, (left,20,left+240,286)); start = end+1
    line('ENTER - NEXT   ESC - BACK', 300, 0, (0,0,width,320))
    return binascii.crc32(struct.pack(f'>{len(pixels)}I', *pixels)) & 0xffffffff


def main():
    p = argparse.ArgumentParser(description=__doc__)
    p.add_argument('--exe', type=Path, required=True)
    p.add_argument('--data', type=Path, default=DEFAULT_DATA)
    a = p.parse_args()
    logo = decode_png((a.data/'logo.png').read_bytes())
    title = decode_png(Sprite.parse(dict(resources(a.data/'1'))[1]).png_images[0][0])
    for width in (240, 569):
        for name, frames, source in (('logo', 1, logo), ('title', 48, title)):
            w, h, pixels = source
            canvas = [0xffffffff if name == 'logo' else 0xff000000] * (width*320)
            dx, dy = (width//2-w//2, 160-h//2) if name == 'logo' else ((width-240)//2, 0)
            for y in range(h):
                for x in range(w):
                    if 0 <= dx+x < width and 0 <= dy+y < 320:
                        color = pixels[y*w+x]
                        require(color >> 24 in (0, 255), 'Artwork requires explicit partial-alpha policy')
                        if color >> 24: canvas[(dy+y)*width+dx+x] = color
            crc = binascii.crc32(b''.join(struct.pack('>I', color) for color in canvas)) & 0xffffffff
            cmd = [str(a.exe), '--game', '--headless', '--data', str(a.data), '--width', str(width), '--fps', 'native', '--frames', str(frames)]
            v = dict(x.split('=') for x in subprocess.check_output(cmd, text=True).split())
            require(int(v['frontend_crc']) == crc, f'{name} pixels differ at width {width}')
    for frames, state in ((48, '2'), (65, '6'), (90, '20'), (112, '19')):
        v = dict(x.split('=') for x in subprocess.check_output([str(a.exe), '--game', '--game-script', '--headless', '--data', str(a.data), '--fps', 'native', '--frames', str(frames)], text=True).split())
        require(v['frontend_state'] == state, 'Menu/credits/prologue/selection script transition failed')
    for width in (240,569):
        for frames in (90,105):
            v = dict(x.split('=') for x in subprocess.check_output([str(a.exe), '--game', '--game-script', '--headless', '--data', str(a.data), '--width', str(width), '--fps', 'native', '--frames', str(frames)], text=True).split())
            require(int(v['frontend_crc']) == story_crc(a.data, width, 240-int(v['frontend_frame'])), 'Original story text pixels or spacing differ')
    reference = None
    for width in (240, 569):
        crc = None
        story_pixels = story_crc(a.data, width, 223)  # 96 ticks: story entered at tick 80.
        for fps in (30, 60, 144):
            for interpolation in (True, False):
                story_cmd = [str(a.exe), '--game', '--game-script', '--headless', '--data', str(a.data), '--width', str(width), '--fps', str(fps), '--frames', str(fps*8)]
                if not interpolation: story_cmd += ['--no-interpolation']
                story = dict(x.split('=') for x in subprocess.check_output(story_cmd, text=True).split())
                require(story['frontend_state'] == '20' and story['frontend_frame'] == '17' and int(story['frontend_crc']) == story_pixels, 'FPS/interpolation changed prologue timing or pixels')
                cmd = [str(a.exe), '--game', '--game-script', '--headless', '--data', str(a.data), '--width', str(width), '--fps', str(fps), '--frames', str(fps*10)]
                if not interpolation: cmd += ['--no-interpolation']
                v = dict(x.split('=') for x in subprocess.check_output(cmd, text=True).split())
                require(v['frontend_state'] == '8' and v['enemies'] == '20' and v['platforms'] == '7', 'Startup did not reach live level')
                state = {k: val for k, val in v.items() if k not in ('frames', 'width', 'movement_crc', 'frontend_crc')}
                if reference is None: reference = state
                require(reference == state, 'Width/FPS changed boot/game state')
                if crc is None: crc = v['frontend_crc']
                require(crc == v['frontend_crc'], 'FPS/interpolation changed startup pixels')
    # The scripted pause spans ticks 128..133. All live state must freeze,
    # then resume at tick 134 without rebuilding the level.
    for width in (240,569):
        snapshots=[]
        for frames in (127,129,133,134):
            v=dict(x.split('=') for x in subprocess.check_output([str(a.exe),'--game','--game-script','--headless','--data',str(a.data),'--width',str(width),'--fps','native','--frames',str(frames)],text=True).split())
            snapshots.append(v)
        before,paused,last,resumed=snapshots
        require([v['frontend_state'] for v in snapshots]==['8','21','21','8'], 'Pause/resume boundaries differ')
        excluded={'frames','ticks','frontend_state','frontend_frame','frontend_crc','movement_crc'}
        require({k:v for k,v in before.items() if k not in excluded}=={k:v for k,v in last.items() if k not in excluded},'Pause advanced gameplay state')
        require(paused['frontend_crc']==last['frontend_crc'] and paused['frontend_crc']!=before['frontend_crc'],'Pause overlay must be stable and visible')
        require(resumed['frontend_frame']=='1' and resumed['player_x']==before['player_x'],'Resume rebuilt or moved the player')
        expected=None
        for fps in (30,60,144):
            for interpolate in (True,False):
                cmd=[str(a.exe),'--game','--game-script','--headless','--data',str(a.data),'--width',str(width),'--fps',str(fps),'--frames',str(fps*11)]
                if not interpolate:cmd+=['--no-interpolation']
                v=dict(x.split('=') for x in subprocess.check_output(cmd,text=True).split())
                require(v['frontend_state']=='21','Timed run did not reach pause')
                state={k:val for k,val in v.items() if k!='frames'}
                if expected is None:expected=state
                require(state==expected,'FPS/interpolation changed paused state or HUD pixels')
    # The ordinary startup fixture pauses during the intro now. Verify HUD output
    # separately after the complete intro fixture has returned control.
    for width in (240,569):
        expected=None
        for fps in (30,60,144):
            for interpolate in (True,False):
                cmd=[str(a.exe),'--game','--game-intro-script','--headless','--data',str(a.data),'--width',str(width),'--fps',str(fps),'--frames',str(fps*30)]
                if not interpolate:cmd+=['--no-interpolation']
                v=dict(x.split('=') for x in subprocess.check_output(cmd,text=True).split())
                require(v['intro_active']=='0' and v['intro_finishes']=='1' and v['frontend_state']=='8' and v['player_health']=='60','HUD fixture did not complete intro')
                state={k:val for k,val in v.items() if k!='frames'}
                if expected is None:expected=state
                require(state==expected,'FPS/interpolation changed post-intro HUD or simulation')
    print('PASS: original logo/title and English story pixels at 240/569, menu/credits/prologue/selection transitions, 12 timed prologue and 12 startup-to-game runs, pause/intro freeze/resume, 12 timed pause runs and 12 post-intro HUD runs')


if __name__ == '__main__': main()
