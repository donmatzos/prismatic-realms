import math
from diabell_gen import new_grid, shade_blade
from lightning_scythe_gen import half_moon, seg
# The Shallow Waters (the Ocean unique scythe), in the Cerulean Abyss palette: dark abyssal indigo rim, cerulean body, pale cerulean
# honed edge, bioluminescent teal accents; abyss-indigo handle. Blade shading as for the Crimson Diabell (shade_blade keys).
PAL={".":None,
     "o":"#0a0a2a",                                                          # blade rim
     "1":"#141650","2":"#232a7c","3":"#3346a8","4":"#4a70cc","5":"#6690dc","6":"#8fb4f0",   # abyss -> cerulean -> pale edge
     "d":"#1a1f5e",                                                          # fin ribs (darker than the body)
     "t":"#6ff5dc","T":"#2fb8b0",                                            # bioluminescent teal: bright, deep
     "p":"#e2eeff","P":"#93b8f2",                                            # pearl: highlight, shade
     "k":"#05061a","K":"#11143a","L":"#1d2256","M":"#2d3474"}                # abyss-indigo handle: outline, body, light, sheen

def shaft(g,top_y,bottom_y,bands):
    for y in range(top_y,bottom_y+1):
        c=16-y
        g[y][c-1]='k'; g[y][c]='M' if y%3==0 else 'L' if y%2 else 'K'; g[y][c+1]='k'
        if y in bands: g[y][c]='t'; g[y][c-1]='T'; g[y][c+1]='T'
def collar(g):
    g[8][8]='t'; g[8][7]='T'; g[7][9]='T'
def blade(extra=frozenset()):
    m=half_moon((8.6,9.6),9.2,(6.2,11.4),8.0,lambda x,y: y<8.0 and x<14.6)
    return m|{(9,7),(10,7),(11,7),(12,7),(10,6),(11,6),(12,6)}|set(extra)

def pearl_end(g):
    for x,y,c in ((1,14,'P'),(2,14,'p'),(3,14,'k'),(1,15,'P'),(2,15,'P')):
        g[y][x]=c

DEEPER={'6':'5','5':'4','4':'3','3':'2','2':'1','1':'1'}
def along(x,y,center=(8.0,9.6)):
    """0 at the blade's tip (left) .. 1 at its root (right), by angle around the curve centre"""
    cx,cy=center; a=(math.degrees(math.atan2(cy-(y+.5),(x+.5)-cx))+360)%360   # 0 = right, 90 = up, 180 = left
    return max(0.0,min(1.0,(200.0-a)/200.0))

def bioluminescent_edge():
    # a dark abyssal blade (body two steps deeper) whose honed edge glows bioluminescent teal
    g=new_grid(); m=blade(); shade_blade(g,m,(8.0,9.6),5.8,9.4)
    for (x,y) in m:
        c=g[y][x]
        if c=='6': g[y][x]='t'
        elif c in '12345': g[y][x]=DEEPER[DEEPER[c]]
    shaft(g,8,13,bands=(11,)); collar(g); pearl_end(g)
    return [''.join(r) for r in g]
FINAL=bioluminescent_edge

def in_hand(sx=1.55):
    """32px in-hand texture with a RE-DRAWN, longer blade (no stretching of the icon): the icon's crescent is two circles;
    here the same crescent is built from two ellipses sx times wider (anchored at the root on the right), so the blade is
    longer and smoothly curved at the same pixel thickness. Same shading and bioluminescent recolour as the icon; a long
    abyss-indigo shaft with two teal bands, the teal collar and the pearl end."""
    g=[['.']*32 for _ in range(32)]
    root_x=16+14.6                                    # the icon's right clip edge, moved into the 32px canvas
    def ex(x): return root_x+(x-root_x)/sx            # map an in-hand x back into the icon's (circular) geometry
    ox,oy,ro=8.6+16,9.6,9.2; ix,iy,ri=6.2+16,11.4,8.0
    m={(x,y) for y in range(9) for x in range(32)
       if math.hypot(ex(x+.5)-ox,y+.5-oy)<=ro and math.hypot(ex(x+.5)-ix,y+.5-iy)>ri and y+.5<8.0 and x+.5<root_x}
    m|={(x+16,y) for x,y in ((9,7),(10,7),(11,7),(12,7),(10,6),(11,6),(12,6))}
    # shading as shade_blade, measured in the icon's geometry so the ellipse shades like the circle
    cx,cy,r_in,r_out=8.0+16,9.6,5.8,9.4
    rad=lambda x,y: math.hypot(ex(x+.5)-cx,y+.5-cy)
    for x,y in m:
        empty=[(x+i,y+j) for i,j in((1,0),(-1,0),(0,1),(0,-1)) if (x+i,y+j) not in m]
        r=rad(x,y); t=max(0.0,min(1.0,(r-r_in)/(r_out-r_in)))
        if any(rad(*e)<r-0.3 for e in empty): c='t'                     # glowing honed edge
        elif empty: c='o'
        else: c=DEEPER[DEEPER["54321"[min(4,int(t*4.99))]]]             # body two steps deeper, like the icon
        g[y][x]=c
    for y in range(9,30):
        c=32-y
        g[y][c-1]='k'; g[y][c]='M' if y%3==0 else 'L' if y%2 else 'K'; g[y][c+1]='k'
        if y in (13,24): g[y][c]='t'; g[y][c-1]='T'; g[y][c+1]='T'
    g[8][24]='t'; g[8][23]='T'; g[7][25]='T'; g[8][25]='k'; g[8][23]='T'   # teal collar at the top of the shaft
    g[9][22]='k' if g[9][22]=='.' else g[9][22]
    for x,y,c in ((1,30,'P'),(2,30,'p'),(3,30,'k'),(1,31,'P'),(2,31,'P')):  # pearl end
        g[y][x]=c
    return [''.join(r) for r in g]

# ---- Full rework (user, 2026-10-12: the look is "too generic, it needs a full rework"). Hand-drawn 16px icons around a
# recognisably nautical idea instead of a plain crescent. Extra keys: bone (b/B/n), chain (c/C).
PAL.update({"b":"#c4d6f0","B":"#7d93c4","n":"#3a4a7a","c":"#8fa3c8","C":"#3c4a78"})
def draw(rows,shaft_top=8,bands=(11,),end=pearl_end):
    g=new_grid()
    for y,row in enumerate(rows):
        for x,ch in enumerate(row):
            if ch!='.': g[y][x]=ch
    shaft(g,shaft_top,13,bands)
    if end: end(g)
    return g
TRIDENT=[             # Poseidon's reaper: a trident head (three tines on a crossbar) with a scythe blade hanging off its side
    "...........6....",
    "...oooo..6.5.6..",
    ".oo34455o5.4.5..",
    "o3455666o4.3.4..",
    "o56t...o43o3o3..",
    "o6t.....o33333o.",
    "ot........oTo...",
    ".t......kMk....."]
def trident_reaper():
    g=draw(TRIDENT)
    return [''.join(r) for r in g]
ANCHOR=[              # the head is an anchor: ring on top of the shank, a stock across it, the blade ending in an arrowhead fluke
    ".............PP.",
    "............P..P",
    "....ooooooo.P..P",
    "..oo3344455oPPP.",
    ".o3456666o4.....",
    "o456t.....o.....",
    "o56t............",
    "oo6.............",
    "o66o............",
    ".oo............."]
def anchor_reaper():
    g=draw(ANCHOR,shaft_top=4,bands=())
    for x,y,c in ((7,5,'c'),(8,6,'c'),(10,8,'c'),(11,9,'C')):   # the stock, square to the shank
        g[y][x]=c
    for y in (10,12):                     # chain links wound round the shank
        c=16-y; g[y][c-1]='c'; g[y][c+1]='C'
    return [''.join(r) for r in g]
BONE=[                # a leviathan's jawbone: pale bone crescent, its inner edge glowing teal, the root bending into a spine
    ".....nnnnnn.....",
    "...nnpppbbbnn...",
    "..npbbbBBbbbbn..",
    ".npbBnttttnBBbn.",
    "npbBnt....tnBbn.",
    "nbBt.......tBbn.",
    "nBt.......nBbn..",
    "nt.......nBbn...",
    ".t......nbBn....",]
def leviathan_bone():
    g=draw(BONE,shaft_top=9,bands=())
    for y in range(9,14):                 # a spine for a handle: vertebrae in bone, dark gaps between them
        c=16-y; g[y][c]='b' if y%2 else 'n'
    return [''.join(r) for r in g]
REWORK=(trident_reaper,anchor_reaper,leviathan_bone)

# ---- Rework round 2 (user: "deep blue colors, 16x16 is simplified, detail can be added to the in hand model"):
# a solid hand-drawn crescent in deep blues only, plus ONE nautical feature per candidate; the detail goes into the in-hand.
DEEP={".":None,
      "o":"#040720",                                            # rim
      "1":"#08124a","2":"#0d2272","3":"#14359a","4":"#1f4cbc",   # deep navy -> deep sea blue
      "e":"#3a72dc","f":"#7fb0f2",                              # honed edge (still blue), rare foam/glint
      "k":"#020310","K":"#070b2a","L":"#0e1640","M":"#18245c"}  # near-black navy handle
DEEP_BLADE=[
    "......ooooo.....",
    "....oo11222oo...",
    "..oo1222333322oo",
    ".o12eeeeeee3332o",
    "o12e.......e332o",
    "o2e........e332o",
    "2e........e332o.",
    ".e.......ee32o.."]
def deep(extra=(),blade=DEEP_BLADE):
    g=new_grid()
    for y,row in enumerate(blade):
        for x,ch in enumerate(row):
            if ch!='.': g[y][x]=ch
    for y in range(8,14):
        c=16-y; g[y][c-1]='k'; g[y][c]='M' if y%3==0 else 'L' if y%2 else 'K'; g[y][c+1]='k'
    g[8][8]='3'; g[14][1]='k'; g[14][2]='M'; g[14][3]='k'; g[15][1]='k'
    for x,y,ch in extra: g[y][x]=ch
    return [''.join(r) for r in g]
# ---- Rework round 3 (user: "no effects on the icon, simply a scythe, features should be on the blade or top, if any";
# the icon is shown rotated 45 degrees, so "top" means the shaft's top-right end along its diagonal, not straight up).
def deep_plain():
    # simply a scythe: a solid deep-blue crescent, nothing added
    return deep()
SPIKE_BLADE=[         # the blade hangs off the shaft's upper-left side so the shaft can run on to a point at the top right
    "...ooooooo......",
    ".oo1122233oo....",
    "o12223333344o...",
    "o2eeeeee3344o...",
    "oe......e34o....",
    "e.......e3o.....",
    "................",
    "................"]
def deep_spike():
    # the shaft runs past the blade to a short spear point in the top-right corner, in line with the shaft
    g=new_grid()
    for y,row in enumerate(SPIKE_BLADE):
        for x,ch in enumerate(row):
            if ch!='.': g[y][x]=ch
    for y in range(3,14):
        c=16-y; g[y][c-1]='k'; g[y][c]='M' if y%3==0 else 'L' if y%2 else 'K'; g[y][c+1]='k'
    for x,y,ch in ((12,3,'o'),(13,3,'3'),(14,3,'o'),                    # ferrule where the shaft ends
                   (13,2,'4'),(14,1,'e'),(15,0,'f'),                    # spear point along the shaft's diagonal
                   (12,2,'o'),(13,1,'o'),(14,0,'o'),(14,2,'o'),(15,1,'o')):   # its outline on both sides
        g[y][x]=ch
    g[14][1]='k'; g[14][2]='M'; g[14][3]='k'; g[15][1]='k'
    return [''.join(r) for r in g]
def deep_ripple():
    # a single lighter ripple running along the blade's body, like a wave line in deep water
    return deep(((3,3,'4'),(4,2,'4'),(5,2,'4'),(6,1,'4'),(7,1,'4'),(8,2,'4'),(9,2,'4'),(10,1,'4'),(11,2,'4'),(12,3,'4')))
REWORK3=(deep_plain,deep_spike,deep_ripple)

# In-hand for the picked round-3 icon (deep_spike): the reaper-length crescent (long_blade.crescent) hangs off the shaft's
# upper-left side along its whole length, touching its outline (clip x + y <= 30), and the shaft runs on through a ferrule to the spear point in
# the top-right corner, in line with the shaft like the icon's. Detail lives here, not on the icon (user).
def _long_spike(colour,bands=()):
    from long_blade import crescent
    g=[['.']*32 for _ in range(32)]
    depth,edges=crescent(clip=lambda x,y: x+y<=30)
    for (x,y),dd in depth.items():
        g[y][x]=colour(x,y,dd,(x,y) in edges)
    for y in range(4,31):                                       # near-black navy shaft, centre x = 32 - y
        c=32-y; g[y][c-1]='k'; g[y][c]='M' if y%3==0 else 'L' if y%2 else 'K'; g[y][c+1]='k'
        if y in bands: g[y][c]='3'; g[y][c-1]='2'; g[y][c+1]='2'
    for x,y,ch in ((27,4,'o'),(28,4,'3'),(29,4,'o'),(28,3,'o'),(29,3,'4'),(30,3,'o'),      # ferrule
                   (30,1,'e'),(31,0,'f'),(29,2,'4'),                                    # spear point (spine x + y = 31)
                   (29,1,'o'),(30,0,'o'),(28,2,'o'),(31,1,'o'),(30,2,'o')):             # its outline
        g[y][x]=ch
    g[31][1]='k'; g[31][2]='k'; g[30][1]='k'                     # end cap
    return [''.join(r) for r in g]
def _ramp(x,dd,keys="1234"):
    return keys[max(0,min(len(keys)-1,dd-1-(1 if x<9 and dd>1 else 0)))]
def long_clean():
    # the icon's blade made long: navy rim, deep body getting lighter towards the honed edge, blue edge
    return _long_spike(lambda x,y,dd,edge: 'o' if dd==0 else 'e' if edge else _ramp(x,dd))
def long_ripple():
    # the clean blade with wave lines running along its body (lighter crests rising and falling every 3px)
    def col(x,y,dd,edge):
        if dd==0: return 'o'
        if edge: return 'e'
        if dd==2+((x//3)%2) and x>3: return '4'
        return _ramp(x,dd,"1223")
    return _long_spike(col)
def long_glint():
    # sunlight through deep water: the blade brightens from the abyssal root to a sea-blue tip, pale glints on the edge,
    # and two deep-blue bands on the shaft
    def col(x,y,dd,edge):
        if dd==0: return 'o'
        if edge: return 'f' if x%7==3 else 'e'
        shade=min(3,max(0,dd-1+(1 if x<14 else 0)-(1 if x>22 else 0)))
        return "1234"[shade]
    return _long_spike(col,bands=(13,24))
LONG_CANDIDATES=(long_clean,long_ripple,long_glint)

FINAL=deep_spike   # picked in round 3 (user: "keep version 2"); palette DEEP
LONG_FINAL=long_ripple   # picked (user: "keep version 2")
