import math
from ore_gen import blank, stamp, outline, spike, rows, seg
from nature_gen import PAL, hsh
from sun_gen import vnoise
from ore_storm_fang import fractal
TINTS={"q":{"color":"#3f5f1a","strength":0.45}}   # damp, moss-stained stone
def moss_patch(g,cx,cy,R,seed,stain=True):
    # irregular moss cushion: lit tufts on top, darker underneath, a damp stain fringe around it
    for y in range(16):
        for x in range(16):
            r=math.hypot((x+.5-cx)/R,(y+.5-cy)/(R*0.8)); n=vnoise(x*0.8,y*0.8,seed)
            if r<0.75+0.5*n:
                if g[y][x]!='.': continue
                top=r>0.45+0.5*n and y+.5<cy
                c=hsh(x,y,seed); g[y][x]='u' if (top and c>0.3) or c>0.85 else 't' if c>0.3 else 's'
            elif stain and r<1.05+0.4*n and g[y][x]=='.' and hsh(x,y,seed+1)>0.35: g[y][x]='q'
def roots(g,segs_list):
    for y in range(16):
        for x in range(16):
            if g[y][x] in '.q' and min(seg(x+.5,y+.5,a,b)-w*0.35 for a,b,w,d in segs_list)<0.3: g[y][x]='b' if (x+y)%2 else 'B'
def overgrown_pocket():
    # a pocket in the stone choked with moss, an emerald glinting through a gap, a root trailing out
    g=blank()
    moss_patch(g,8.0,8.6,5.0,11)
    for x,y,c in ((7,8,'m'),(8,8,'l'),(9,8,'h'),(7,9,'d'),(8,9,'m'),(8,7,'w')): g[y][x]=c   # emerald glimpse
    for x,y in ((6,7),(6,8),(6,9),(7,10),(8,10),(9,9),(10,8),(9,7),(7,7),(10,9)):
        if g[y][x] in 'tu': g[y][x]='s'   # dark moss framing the glimpse
    segs=[]; fractal((11.6,11.6),0.6,5,0.8,77,segs); roots(g,segs)
    return rows(g)
FINAL=overgrown_pocket
