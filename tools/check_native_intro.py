"""Verify native intro script 76 against original bytecode and actual host flow."""
import argparse
from pathlib import Path
import subprocess
p=argparse.ArgumentParser(description=__doc__)
p.add_argument("--exe",type=Path,required=True)
p.add_argument("--port-exe",type=Path)
p.add_argument("--data",type=Path,default=Path("soul/extracted"))
a=p.parse_args()
expected=Path("soul/analysis/intro-oracle.tsv").read_text().splitlines()
actual=subprocess.check_output([str(a.exe),"--data",str(a.data),"--intro-manifest"],text=True).splitlines()
assert len(expected)==5460 and len(actual)==len(expected),(len(actual),len(expected))
for i,(want,got) in enumerate(zip(expected,actual)):
    assert want==got,f"Intro row {i}:\nJava: {want}\nC++:  {got}"
print("PASS: 5460 original-bytecode intro timeline, camera, flags, dialogue-anchor and actor-presence rows")

if a.port_exe:
    def run(width,fps,game=False,skip=False,interpolate=True,frames=None):
        flag="--game-intro-skip-script" if skip else "--game-intro-script"
        if not game:flag="--intro-skip-script" if skip else "--intro-script"
        cmd=[str(a.port_exe),"--game" if game else "--movement-preview",flag,"--data",str(a.data),"--headless","--width",str(width),"--fps",str(fps),"--frames",str(frames if frames is not None else fps*(50 if game else 40))]
        if not interpolate:cmd.append("--no-interpolation")
        return dict(x.split("=") for x in subprocess.check_output(cmd,text=True).split())
    keys=("player_x","player_y","intro_tick","intro_dialogue","intro_finishes","intro_skips","intro_dialogues_opened")
    for frames,values in ((1,(121,259,0,-1,0,0,0)),(25,(-50,120,24,-1,0,0,0)),(55,(72,114,45,-1,0,0,1)),(100,(157,260,78,2,0,0,3)),(150,(167,259,-2,-1,1,0,4))):
        state=run(240,"native",frames=frames)
        assert tuple(int(state[k]) for k in keys)==values,(frames,state)
    skipped=run(240,"native",skip=True,frames=55)
    assert tuple(int(skipped[k]) for k in keys)==(167,259,-2,-1,1,1,1),skipped
    # Movement input during ticks 10..14 must stay locked out of the running script.
    locked=run(240,"native",frames=15)
    assert locked["player_x"]=="-50" and locked["player_vx"]=="0" and locked["intro_control_ticks"]=="0",locked
    for game in (False,True):
        for skip in (False,True):
            baseline=None
            for width in (240,569):
                crc=None
                for fps in (30,60,144):
                    for interpolate in (True,False):
                        state=run(width,fps,game,skip,interpolate)
                        for key,value in {"intro_active":0,"intro_tick":-2,"intro_starts":1,"intro_finishes":1,"intro_skips":int(skip),"intro_dialogues_opened":1 if skip else 4,"intro_control_ticks":20,"intro_trigger_alive":0,"intro_bird83_alive":0,"intro_prompt_hidden":0,"player_x":167,"player_y":259,"player_vx":0,"defeated":0,"player_health":60,"melee_attacks":0,"fire_casts":0}.items():
                            assert int(state[key])==value,(game,skip,key,state[key],value)
                        stable={k:v for k,v in state.items() if k not in {"frames","width","movement_crc","frontend_crc"}}
                        if baseline is None:baseline=stable
                        assert stable==baseline,"Display settings changed intro simulation"
                        if crc is None:crc=state["movement_crc"]
                        assert state["movement_crc"]==crc,"FPS/interpolation changed intro render"
    # Raw default New Game must start the intro without an intro fixture option.
    normal=dict(x.split("=") for x in subprocess.check_output([str(a.port_exe),"--game","--game-script","--data",str(a.data),"--headless","--fps","native","--frames","165"],text=True).split())
    assert normal["intro_active"]=="1" and normal["intro_dialogue"]=="0",normal
    print("PASS: seven milestones, 48 playback/skip host runs, input lock/release and automatic New Game intro at 240/569 across FPS/interpolation")
