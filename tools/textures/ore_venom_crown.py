import math
from ore_gen import blank, stamp, outline, spike, rows, seg
from poison_gen import PAL, pentagon_body
from sun_gen import vnoise
TINTS={"q":{"color":"#5fb820","strength":0.3},"p":{"color":"#4a1a6e","strength":0.4}}   # sickly venom stain, bruised-violet stain
def stain(g,cx,cy,R,seed,keys='qp'):
    # patchy toxic staining spreading out from the exposure, never reaching the block edges
    for y in range(1,15):
        for x in range(1,15):
            if g[y][x]!='.': continue
            r=math.hypot(x+.5-cx,y+.5-cy); n=vnoise(x*0.9,y*0.9,seed)
            if r<R*(0.75+0.55*n): g[y][x]=keys[0] if n>0.55 else keys[1]
def corroded_pit():
    # stone corroded into an irregular pit, a faceted amethyst glinting inside, venom bubbles at its rim
    g=blank(); cx,cy=8.0,8.4
    def pit(x,y):
        px,py=x+.5-cx,y+.5-cy; th=math.atan2(py,px)
        return math.hypot(px,py)<=4.0*(1+0.15*math.sin(3*th+1)+0.08*math.sin(5*th))+0.8*(vnoise(math.cos(th)*2,math.sin(th)*2,17)-0.5)
    for y in range(16):
        for x in range(16):
            if pit(x,y): g[y][x]='a'
    pentagon_body(g,8.0,8.6,[2.6,2.4,2.5,2.4,2.5],[0.2,0.25,0.2,0.25,0.25],-54,[((8.0,8.6),(9.2,9.6))])
    g[7][7]='w'
    g=outline(g)
    for x,y,c in ((4,5,'v'),(11,4,'V'),(12,11,'v'),(5,12,'V'),(3,9,'v')):
        if g[y][x] in '.o': g[y][x]=c   # venom bubbles at the rim
    stain(g,cx,cy,6.4,21)
    return rows(g)
FINAL=corroded_pit
