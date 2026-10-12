import math
from bolt_gen import bevel, seg  # facet shading (o/d/m/l/h/w ramp keys)
# blood palette: near-black maroon outline -> deep crimson -> ruby -> scarlet -> rose -> pale pink, plus clotted (c) and fresh, saturated blood (b, B)
PAL={".":None,"o":"#25040a","a":"#3d0810","d":"#640d18","m":"#a3172a","l":"#d8303f","h":"#f2737d","w":"#ffd9dd","c":"#4e0a14","b":"#ff1a33","B":"#9a0016"}
DEEPER={"w":"h","h":"l","l":"m","m":"d","d":"a","a":"a"}
def blank(): return [['.']*16 for _ in range(16)]
def outline(g):
    for y in range(16):
        for x in range(16):
            if g[y][x]=='.' and any(0<=y+j<16 and 0<=x+i<16 and g[y+j][x+i] not in '.O' for i,j in((1,0),(-1,0),(0,1),(0,-1))):
                g[y][x]='O'
    return [''.join(r).replace('O','o') for r in g]
SPIKE=[(5.0,15.0),(3.6,9.6),(5.6,4.4),(8.6,0.6),(11.6,4.0),(12.4,9.8),(10.4,15.0)]
def base_spike():
    g=bevel(SPIKE,deep=1.3,spine_d=2.6); g[3][7]='w'; g[4][7]='w'
    return g
def wounded():
    # the ruby spike with a small wound on its face: a dark gash rimmed with fresh blood, a trickle running down to a drip
    g=base_spike()
    for x,y,c in ((7,6,'B'),(8,6,'c'),(9,6,'B'),(7,7,'c'),(8,7,'c'),(9,7,'B'),(8,8,'B'),(6,7,'b'),(10,6,'b'),(8,5,'b')): g[y][x]=c   # wound
    for x,y in ((8,9),(8,10),(9,11),(9,12),(9,13)): g[y][x]='b' if y<12 else 'B'   # trickle
    rows=[list(r) for r in outline(g)]
    rows[15][9]='b'   # drip
    return [''.join(r) for r in rows]

# --- crimson sanguis shards: broken ruby pieces, fresh blood along the breaks ---
def bloody_breaks(g,pts,breaks,width=0.6):
    # fresh blood on the snapped faces: bright at the edge, darker just inside
    for y in range(16):
        for x in range(16):
            if g[y][x]=='.': continue
            d=min(seg(x+.5,y+.5,pts[a],pts[b]) for a,b in breaks)
            if d<width: g[y][x]='b'
            elif d<width+0.7 and g[y][x] in 'lm': g[y][x]='B'
def shard_tip():
    # the spike's pointed tip, snapped off along a jagged line where fresh blood beads and drips
    pts=[(8.0,0.8),(11.4,5.0),(12.4,9.6),(10.6,11.6),(8.8,10.4),(7.0,12.0),(5.2,10.2),(3.6,10.8),(4.6,5.0)]
    g=bevel(pts,deep=1.2,spine_d=2.4)
    bloody_breaks(g,pts,[(2,3),(3,4),(4,5),(5,6),(6,7)])
    g[3][7]='w'; g[4][7]='w'
    rows=[list(r) for r in outline(g)]
    rows[13][7]='b'; rows[15][7]='b'
    return [''.join(r) for r in rows]
