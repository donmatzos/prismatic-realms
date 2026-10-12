import math
from diabell_gen import new_grid, shade_blade
from lightning_scythe_gen import half_moon
# Melting Sunray (the Sun unique scythe), in the Cruel Sun palette: dark crust rim, molten body (deep red -> orange -> gold),
# white-hot honed edge; charred handle with ember bands. Clean crescent, few deliberate details (user direction).
PAL={".":None,
     "o":"#3d1206",                                                          # crust rim
     "1":"#5a2a1e","2":"#9c2a0e","3":"#d6501a","4":"#f28a22","5":"#ffc23c","6":"#fff0a0",   # crust -> molten -> white-hot
     "b":"#14100e","B":"#241c1a","D":"#3a302c",                              # obsidian (eclipse blade)
     "s":"#ffffff",                                                          # sun-orb core
     "k":"#120806","K":"#2a1410","L":"#3e1e16","M":"#5a2c20",                # charred handle: outline, body, light, sheen
     "e":"#d6501a","n":"#f28a22","g":"#ffc23c"}                              # ember band / collar
DEEPER={'6':'5','5':'4','4':'3','3':'2','2':'1','1':'1'}

def shaft(g,top_y,bottom_y,bands):
    for y in range(top_y,bottom_y+1):
        c=16-y
        g[y][c-1]='k'; g[y][c]='M' if y%3==0 else 'L' if y%2 else 'K'; g[y][c+1]='k'
        if y in bands: g[y][c]='n'; g[y][c-1]='e'; g[y][c+1]='e'
def collar(g):
    g[8][8]='g'; g[8][7]='n'; g[7][9]='n'
def ember_end(g):
    for x,y,c in ((1,14,'e'),(2,14,'n'),(3,14,'k'),(1,15,'e'),(2,15,'e')):
        g[y][x]=c
TANG={(9,7),(10,7),(11,7),(12,7),(10,6),(11,6),(12,6)}
def blade(outer=(8.6,9.6),r_out=9.2,inner=(6.2,11.4),r_in=8.0):
    return half_moon(outer,r_out,inner,r_in,lambda x,y: y<8.0 and x<14.6)|TANG

def pocket_sun():
    # a broader molten crescent with a small glowing sun orb set where the blade meets the shaft
    g=new_grid(); m=blade((8.6,10.0),9.6,(6.4,12.2),8.2); shade_blade(g,m,(8.0,10.0),6.0,10.0)
    # the orb: a 3x3 sun (white core, gold glow) framed by a ring of dark crust so it reads as set into the blade
    cx,cy=12,5
    for dy in range(-2,3):
        for dx in range(-2,3):
            x,y=cx+dx,cy+dy
            if (x,y) not in m: continue
            ring=max(abs(dx),abs(dy))==2
            g[y][x]='1' if ring and abs(dx)+abs(dy)<4 else (g[y][x] if ring else ('s' if (dx,dy)==(0,0) else '6' if abs(dx)+abs(dy)==1 else '5'))
    shaft(g,8,13,bands=(11,)); collar(g); ember_end(g)
    return [''.join(r) for r in g]
# Hand-drawn heads (round 4): the sun orb sits at the very top (round core x 10-14, y 1-5, crust ring around it) on a long
# shaft, and the blade grows out of the orb's LEFT SIDE and runs out sideways before curving down to its tip.
ORB={            # round sun orb (user: "make the orbs rounder"): a 3-5-5-5-3 core inside a ring that follows the curve,
    0:(11,"ooo"),   # highlight at the upper left, cooling to orange-red at the lower right
    1:(10,"o665o"),
    2:(9,"o5s654o"),
    3:(9,"o66543o"),
    4:(9,"o55433o"),
    5:(10,"o432o"),
    6:(12,"oo")}
def head(blade_rows):
    g=new_grid()
    for y,row in enumerate(blade_rows):
        for x,c in enumerate(row):
            if c!='.': g[y][x]=c
    for y in (1,2):                                    # the blade meets the orb's ring across its whole thickness
        if g[y][9]=='.' and g[y][8]!='.': g[y][9]='5' if y==1 else '6'
    for y,(x0,px) in ORB.items():
        for i,c in enumerate(px): g[y][x0+i]=c
    shaft(g,6,13,bands=(10,)); ember_end(g)            # the shaft runs right up into the bottom of the orb
    return [''.join(r) for r in g]

SIDE=[            # out of the orb's side (x 9 meets the ring at x 10), straight, then down at the tip
    "....oooooo......",
    "..oo234445......",
    ".o3566666.......",
    "o56.............",
    "6..............."]
DROOP=[           # leaves the side and droops in a deeper curve, tip hanging lower
    ".....ooooo......",
    "...oo23445......",
    "..o356666.......",
    ".o56............",
    "o56.............",
    "o6..............",
    ".6.............."]
ARCH=[            # thicker, rising a little as it leaves the orb, then arcing down
    "...ooooooo......",
    ".oo2334445......",
    "o23456666.......",
    "o566............",
    "o6..............",
    "6..............."]
def side_blade():
    return head(SIDE)
def drooping_blade():
    return head(DROOP)
def arched_blade():
    return head(ARCH)

FINAL=arched_blade

# In-hand blade, RE-DRAWN (not stretched): per column x of the 32px canvas, (top row, thickness), placed by hand so the blade
# rises out of the orb's left side (the orb's ring is at x 25), arcs over and tapers down to a fine tip at the far left.
LONG_ARCH={
    24:(0,4),23:(0,4),22:(0,4),21:(0,4),20:(0,4),19:(0,4),18:(0,4),17:(0,4),16:(0,4),15:(0,4),14:(0,4),
    13:(1,4),12:(1,4),11:(1,4),10:(1,4),9:(1,4),
    8:(2,3),7:(2,3),6:(2,3),
    5:(3,3),4:(3,3),
    3:(4,3),2:(5,3),1:(6,2),0:(7,2)}
def in_hand():
    """32px in-hand texture: the icon's round orb at the same pixel size at the top right, a long charred shaft with two
    ember bands down to the ember end, and the re-drawn long arched blade (LONG_ARCH) shaded like the icon's: crust rim on
    top, molten body (darker towards the tip), white-hot edge underneath."""
    g=[['.']*32 for _ in range(32)]
    for x,(top,thick) in LONG_ARCH.items():
        body='2' if x<8 else '3' if x<14 else '4'
        keys=['o']+[body,'5'][:max(0,thick-2)]+['6']
        if thick==2: keys=['o','6']
        for i,c in enumerate(keys[:thick]):
            g[top+i][x]=c
    for y,(x0,px) in ORB.items():
        for i,c in enumerate(px): g[y][x0+16+i]=c
    for y in range(6,30):
        c=32-y
        g[y][c-1]='k'; g[y][c]='M' if y%3==0 else 'L' if y%2 else 'K'; g[y][c+1]='k'
        if y in (13,24): g[y][c]='n'; g[y][c-1]='e'; g[y][c+1]='e'
    for x,y,c in ((1,30,'e'),(2,30,'n'),(3,30,'k'),(1,31,'e'),(2,31,'e')):   # ember end
        g[y][x]=c
    return [''.join(r) for r in g]
