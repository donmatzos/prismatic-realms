import json, math
from ore_gen import blank, stamp, outline, spike, rows, seg
from sun_gen import vnoise
from ore_storm_fang import fractal
PAL=json.load(open("tools/textures/specs/ice_star_shard.json"))["palette"]   # the aquamarine shard's palette
TINTS={"q":{"color":"#d8f4ff","strength":0.38},"r":{"color":"#b4e6f0","strength":0.25}}   # rime frost, fainter frost
def frost(g,cx,cy,R,seed):
    # patchy rime spreading over the stone, thicker near the ice and breaking up further out
    for y in range(16):
        for x in range(16):
            if g[y][x]!='.': continue
            r=math.hypot(x+.5-cx,y+.5-cy); n=vnoise(x*0.9,y*0.9,seed)
            if r<R*(0.55+0.6*n): g[y][x]='q'
            elif r<R*(0.8+0.6*n) and (x+2*y)%3==0: g[y][x]='r'
def frost_bloom():
    # frost flowers: fine ice dendrites growing out over the stone from a small aquamarine star
    g=blank()
    for k in range(5):
        a=math.radians(-90+72*k)
        stamp(g,spike([(8.0,7.8),(8.0+3.0*math.cos(a-0.35),7.8+3.0*math.sin(a-0.35)),(8.0+4.2*math.cos(a),7.8+4.2*math.sin(a)),(8.0+3.0*math.cos(a+0.35),7.8+3.0*math.sin(a+0.35))],deep=0.6,bright=1))
    g[7][7]='w'
    g=outline(g)
    segs=[]
    for k,ang in enumerate((-1.9,-0.3,1.0,2.2,2.9)):
        fractal((8+4.4*math.cos(ang),7.8+4.4*math.sin(ang)),ang,4,0.7,90+k*9,segs)
    for y in range(16):
        for x in range(16):
            if g[y][x]=='.' and min(seg(x+.5,y+.5,a,b)-w*0.3 for a,b,w,d in segs)<0.3: g[y][x]='h' if (x+y)%2 else 'l'
    frost(g,8.0,7.8,7.0,5)
    return rows(g)
FINAL=frost_bloom
