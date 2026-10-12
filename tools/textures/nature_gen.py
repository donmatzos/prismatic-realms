import math
from bolt_gen import bevel, seg  # same facet shading as the storm fang bolt (shares the o/d/m/l/h/w ramp keys)
# nature palette: deep green outline -> emerald crystal ramp -> pale mint, plus moss and bark accents
PAL={".":None,"o":"#0f2e18","d":"#1e6a33","m":"#3aa64c","l":"#7bd866","h":"#c6f5a6","w":"#f2ffe6",
     "s":"#3f5f1a","t":"#6b9426","u":"#9cc43c","b":"#5a3b22","B":"#7d5534"}
def blank(): return [['.']*16 for _ in range(16)]
def hsh(x,y,s=0):
    n=(x*374761393+y*668265263+s*982451653)&0xffffffff; n=(n^(n>>13))*1274126177&0xffffffff
    return ((n^(n>>16))&0xffff)/65535
def inpoly(x,y,pts):
    c=False
    for i in range(len(pts)):
        (x1,y1),(x2,y2)=pts[i],pts[i-1]
        if (y1>y)!=(y2>y) and x<(x2-x1)*(y-y1)/(y2-y1)+x1: c=not c
    return c
def outline(g):
    for y in range(16):
        for x in range(16):
            if g[y][x]=='.' and any(0<=y+j<16 and 0<=x+i<16 and g[y+j][x+i] not in '.O' for i,j in((1,0),(-1,0),(0,1),(0,-1))):
                g[y][x]='O'
    return [list(''.join(r).replace('O','o')) for r in g]
def moss(g,mask,seed):
    # lumpy moss: lighter on top-left, darker underneath, with a few bright tufts
    for y in range(16):
        for x in range(16):
            if mask(x,y):
                r=hsh(x,y,seed); g[y][x]='u' if r>0.78 else 't' if r>0.3 else 's'
def seed():
    # (archived: unused-item-sprites/nature_sprouting_seed)
    # faceted emerald seed cracking open, a sapling with two leaves rising from the crack
    g=bevel([(4.2,9.4),(6.4,7.4),(9.6,7.4),(11.8,9.4),(11.8,12.4),(9.6,14.8),(6.4,14.8),(4.2,12.4)],deep=1.2,spine_d=2.2)
    g[8][8]='o'; g[9][8]='d'   # the crack the sprout grows from
    moss(g,lambda x,y: g[y][x]!='.' and y>=13 and hsh(x,y,5)>0.4,5)
    for y in (4,5,6,7): g[y][8]='t'
    def leaf(cx,cy,ang,L,W):
        a=math.radians(ang)
        for y in range(16):
            for x in range(16):
                px,py=x+.5-cx,y+.5-cy; u=px*math.cos(a)+py*math.sin(a); v=-px*math.sin(a)+py*math.cos(a)
                if 0<=u<=L and abs(v)<=W*math.sin(math.pi*u/L):
                    g[y][x]='h' if abs(v)<0.45 and u<L-1 else ('l' if v<0 else 'm')
    leaf(8.3,4.8,-152,5.4,1.8); leaf(8.7,3.4,-30,5.6,1.8)
    return [''.join(r) for r in outline(g)]
def leafgem2():
    # glassy emerald leaf: lit upper half, shadowed lower half, bright midrib with a few short veins, moss patches and a curled tendril
    # (archived: unused-item-sprites/nature_mossy_leaf)
    g=blank(); A=(3.2,12.8); T=(14.0,2.0); dx,dy=T[0]-A[0],T[1]-A[1]; L=math.hypot(dx,dy); d=(dx/L,dy/L); n=(-d[1],d[0])
    for y in range(16):
        for x in range(16):
            px,py=x+.5-A[0],y+.5-A[1]; u=(px*d[0]+py*d[1])/L; v=px*n[0]+py*n[1]
            if not 0<=u<=1: continue
            w=4.2*math.sin(math.pi*u)**0.75
            if abs(v)>w: continue
            if abs(v)<0.5 and u<0.92: c='h'
            elif any(abs(u*L-abs(v)*0.9-k)<0.5 and abs(v)<w*0.65 for k in (3.6,6.6,9.6)): c='h' if v<0 else 'l'
            elif v<0: c='l'
            else: c='m' if abs(v)<w-1.0 else 'd'
            g[y][x]=c
    g[4][11]='w'; g[3][12]='w'; g[5][10]='h'
    for x,y in ((5,11),(6,11),(5,10),(9,9),(10,8),(4,9)): g[y][x]='t' if (x+y)%2 else 's'   # moss patches on the blade
    g[9][4]='u'
    for x,y,c in ((2,13,'b'),(1,14,'B'),(1,12,'s'),(0,11,'t'),(1,10,'t'),(2,10,'u')): g[y][x]=c   # stalk curling into a mossy tendril
    return [''.join(r) for r in outline(g)]
EMERALD=[(5.2,1.4),(10.8,1.4),(13.0,3.6),(13.0,12.4),(10.8,14.6),(5.2,14.6),(3.0,12.4),(3.0,3.6)]
def emerald_top():
    # step-cut emerald with moss over its top-left corner and a small sprout (the picked top half)
    g=bevel(EMERALD,core='l',spine='h',deep=1.6,spine_d=3.2)
    for x,y in ((6,4),(7,4),(6,5)): g[y][x]='w'
    for x,y in ((3,4),(4,3),(5,2),(6,2),(3,5),(4,4),(5,3),(3,6),(4,5),(3,7),(7,2),(3,8),(4,6)):
        g[y][x]='t' if (x*3+y)%3 else 's'
    for x,y in ((4,2),(3,3),(5,1)): g[y][x]='u'
    for x,y,c in ((3,1,'t'),(2,0,'l'),(4,0,'l')): g[y][x]=c
    return g
def overgrow(g,edge,window,bulge,seed):
    # moss swallows everything below a wavy edge (bulging past the gem's sides): lit rim on top, darker towards the lower right,
    # leaving a small window onto the crystal framed by dark moss
    for y in range(16):
        for x in range(16):
            inside=g[y][x]!='.' or (x,y) in bulge
            if not inside or y<edge(x) or (x,y) in window: continue
            depth=y-edge(x); shade=0.55*x/15+0.45*min(1,depth/6)
            c='u' if depth<1 and x<11 else 't' if shade<0.62 else 's'
            r=hsh(x,y,seed)
            if c=='t' and r>0.86: c='u'
            elif c=='t' and r<0.12: c='s'
            g[y][x]=c
    for x,y in window:
        for i,j in ((1,0),(-1,0),(0,1),(0,-1)):
            if (x+i,y+j) not in window and g[y+j][x+i] in 'tu': g[y+j][x+i]='s'
    return g
def roots(rows,pix):
    # thin roots drawn after the outline so they stay one pixel wide
    g=[list(r) for r in rows]
    for x,y,c in pix: g[y][x]=c
    return [''.join(r) for r in g]
WIN=lambda x0,y0:{(x0,y0),(x0+1,y0),(x0+2,y0),(x0,y0+1),(x0+1,y0+1),(x0+2,y0+1),(x0+1,y0+2)}
def overgrown_a():
    # moss over the lower half with a clear window onto the gem; one root wraps across the moss, another pokes out to the right
    g=overgrow(emerald_top(),lambda x:8.6+0.8*math.sin(x*1.4),WIN(6,10),{(2,11),(2,12),(13,10),(13,11),(13,12)},21)
    for x,y in ((3,13),(4,13),(5,14),(6,14)): g[y][x]='b'
    return roots(outline(g),[(13,12,'b'),(14,12,'b'),(15,13,'B'),(15,14,'b')])

# --- overgrown prosperity shards: broken emerald pieces with moss clinging to them ---
def mossy(g,cells,seed):
    # moss patch on the given cells, lit on its upper-left edge
    for x,y in cells:
        r=hsh(x,y,seed); up=(x,y-1) not in cells or (x-1,y) not in cells
        g[y][x]='u' if up and r>0.35 else 't' if r>0.3 else 's'
def shard_corner():
    # corner broken off the step-cut emerald: step facets on the outside, a bright jagged break, moss on the outer bevel, one root
    pts=[(2.6,6.0),(6.0,2.6),(13.4,2.6),(11.8,6.2),(13.0,8.8),(9.6,10.4),(10.2,13.4),(6.6,12.4),(2.6,13.2)]
    g=bevel(pts,core='l',spine='h',deep=1.6,spine_d=3.0)
    for x,y in ((6,5),(7,5),(6,6)): g[y][x]='w'
    for y in range(16):
        for x in range(16):
            if g[y][x]=='.': continue
            d=min(seg(x+.5,y+.5,pts[i],pts[i+1]) for i in range(3,8))
            if d<0.8: g[y][x]='w' if (x+y)%3 else 'h'   # snapped faces
    mossy(g,{(3,6),(4,5),(5,4),(3,7),(4,6),(3,8),(3,9),(4,7),(6,3),(5,3),(3,10),(3,11),(4,11),(4,12),(5,12)},51)
    return roots(outline(g),[(2,13,'b'),(1,14,'B'),(1,15,'b')])
