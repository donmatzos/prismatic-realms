import math
from ore_gen import blank, stamp, outline, spike, rows, seg
from sun_gen import PAL, vnoise
# the bevel ramp keys (d/m/l/h/w) mapped onto the molten palette
MOLTEN={'d':'r','m':'e','l':'n','h':'g','w':'y'}
TINTS={"q":{"color":"#3a1a10","strength":0.55}}
def lava(g):
    return [[MOLTEN.get(c,c) for c in r] for r in g]
def churn(g,cells,seed=3):
    # fill cells with the churning-lava swirl of the crystal (sun_gen.molten), lit from the upper left
    for x,y in cells:
        sw=vnoise(x*0.8+math.sin(y*0.7)*1.3,y*0.8+math.cos(x*0.7)*1.3,seed)
        b=0.35*(1-(x+y)/30)+0.75*sw-0.05
        g[y][x]="renggy"[max(0,min(4,int(b*6)))]
def blob(cx,cy,R,lumps,seed):
    # irregular, lumpy disc: the radius wobbles with the angle and a little value noise, so nothing reads as a perfect circle
    a1,p1,a2,p2=lumps
    def inside(x,y):
        px,py=x+.5-cx,y+.5-cy; th=math.atan2(py,px)
        r=R*(1+a1*math.sin(3*th+p1)+a2*math.sin(5*th+p2))+0.9*(vnoise(math.cos(th)*2+seed,math.sin(th)*2,seed)-0.5)
        return math.hypot(px,py)<=r
    return inside
def scorch(g,cx,cy,R,seed,strength_key='q'):
    # patchy scorch marks fading out from the orb: dense close to it, broken further out
    for y in range(16):
        for x in range(16):
            if g[y][x]!='.': continue
            r=math.hypot(x+.5-cx,y+.5-cy); n=vnoise(x*0.9,y*0.9,seed)
            if r<R+1.2+1.6*n and n+0.55*(1-(r-R)/3.2)>0.62: g[y][x]=strength_key
def organic_seeping():
    # off-centre molten orb half sunk into the stone, lava seeping out into branching cracks
    g=blank(); cx,cy=6.6,6.8; inside=blob(cx,cy,3.2,(0.12,2.0,0.1,0.4),11)
    cells=[(x,y) for y in range(16) for x in range(16) if inside(x,y)]
    churn(g,cells,7); g[5][5]='y'; g[5][6]='g'
    cracks=[((9.0,8.4),(11.6,10.2)),((11.6,10.2),(14.6,10.0)),((11.6,10.2),(12.4,13.8)),((5.4,9.8),(4.0,12.6)),((4.0,12.6),(2.0,14.2)),((8.6,4.4),(11.0,2.2))]
    g=outline(g); scorch(g,cx,cy,3.2,13)
    for y in range(16):   # thin lava threads drawn after the outline, so they stay one pixel wide
        for x in range(16):
            if g[y][x] in '.q' and min(seg(x+.5,y+.5,a,b) for a,b in cracks)<0.45:
                g[y][x]='n' if math.hypot(x+.5-cx,y+.5-cy)<6.0 else 'e'
    return rows(g)
FINAL=organic_seeping
