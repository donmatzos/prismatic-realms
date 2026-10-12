import math
from ore_gen import blank, stamp, outline, spike, rows, seg
from abyss_gen import PAL
from sun_gen import vnoise
TINTS={"q":{"color":"#141650","strength":0.45},"z":{"color":"#2fb8b0","strength":0.22}}   # abyssal shadow, faint bioluminescent glow
def yin_yang_halves(cx,cy,R,rot,gap):
    # the two comma-shaped halves of a yin-yang disc (shape only, no dots). Only the S-curve between them is cut away
    # (half-width `gap`), so the round heads keep their size and the channel shows as bare stone.
    c,sn=math.cos(rot),math.sin(rot)
    def local(x,y):
        px,py=x+.5-cx,y+.5-cy; return px*c+py*sn,-px*sn+py*c
    def s_dist(x,y):
        # distance to the S: the upper semicircle bulges right, the lower one bulges left
        du=abs(math.hypot(x,y+R/2)-R/2) if x>=0 else math.hypot(x,y+R/2*0)+9
        dl=abs(math.hypot(x,y-R/2)-R/2) if x<=0 else 9
        return min(du if x>=0 else 9,dl)
    halves=([],[])
    for y in range(16):
        for x in range(16):
            lx,ly=local(x,y)
            if math.hypot(lx,ly)>R or s_dist(lx,ly)<gap: continue
            top=math.hypot(lx,ly+R/2)<=R/2; bottom=math.hypot(lx,ly-R/2)<=R/2
            yin=top or (lx<0 and not bottom)
            halves[0 if yin else 1].append((x,y))
    return halves
def yin_yang_vein(cx,cy,R,rot,gap,seed):
    # the S-shaped channel between the halves stays bare stone: only the outer rim gets the dark outline,
    # and the halves' edges along the channel are shaded dark instead
    g=blank(); halves=yin_yang_halves(cx,cy,R,rot,gap); taken=set(halves[0])|set(halves[1])
    for cells in halves:
        cs=set(cells); xs=[x for x,y in cells]; ys=[y for x,y in cells]
        mx,my=sum(xs)/len(xs),sum(ys)/len(ys)
        for x,y in cells:
            channel=any((x+i,y+j) not in taken and math.hypot(x+i+.5-cx,y+j+.5-cy)<R-0.3 for i,j in((1,0),(-1,0),(0,1),(0,-1)))
            s2=-(x-mx)*0.55-(y-my)*0.7+1.2*(vnoise(x*0.9,y*0.9,seed)-0.5)
            g[y][x]='d' if channel else 'l' if s2>1.0 else 'm' if s2>-0.9 else 'd'
        inner=[(x,y) for x,y in cells if g[y][x]!='d']
        for i,(x,y) in enumerate(inner[::max(1,len(inner)//3)][:3]): g[y][x]='g' if i%2 else 'G'   # bioluminescent specks
    out=[r[:] for r in g]
    for y in range(16):
        for x in range(16):
            if g[y][x]=='.' and math.hypot(x+.5-cx,y+.5-cy)>=R-0.3 and any(0<=y+j<16 and 0<=x+i<16 and g[y+j][x+i]!='.' for i,j in((1,0),(-1,0),(0,1),(0,-1))):
                out[y][x]='o'
    g=out
    for y in range(1,15):
        for x in range(1,15):
            n=vnoise(x*0.9,y*0.9,seed+3); r=math.hypot(x+.5-cx,y+.5-cy)
            if g[y][x]=='.' and R<=r<R+1.2+1.0*n: g[y][x]='z' if n>0.62 else 'q'
    return rows(g)
def yin_yang_tilted():
    # the same pair turned on its side, the S-channel running left to right
    return yin_yang_vein(8.0,8.0,5.2,0.0,0.9,73)
FINAL=yin_yang_tilted
