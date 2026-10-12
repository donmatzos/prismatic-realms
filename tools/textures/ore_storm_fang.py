import math
from ore_gen import blank, stamp, outline, spike, rows, seg
from bolt_gen import PAL
from sun_gen import vnoise
TINTS={"q":{"color":"#2a2a1a","strength":0.5},"z":{"color":"#4cc8e0","strength":0.28}}
def fractal(start,angle,length,width,seed,out,depth=0,inside=lambda x,y: 0<=x<16 and 0<=y<16):
    # organic lightning: a jittery path that thins out as it goes and forks repeatedly (Lichtenberg-like)
    x,y=start; steps=max(1,int(length)); base=angle
    for i in range(steps):
        base=angle if i==0 else base
        angle=base+(vnoise(i*1.3+seed,depth*2.3+seed*0.1,seed)-0.5)*1.5   # zigzag around a steady heading
        nx,ny=x+0.9*math.cos(angle),y+0.9*math.sin(angle)
        if not inside(nx,ny): break
        out.append(((x,y),(nx,ny),width*(1-0.55*i/steps),depth)); x,y=nx,ny
        if depth<3 and vnoise(i*0.9,seed*0.37+depth,seed+5)>0.62 and i>1:
            fractal((x,y),base+(0.75 if (i+seed)%2 else -0.75),length*0.5,width*0.6,seed+13*(i+1),out,depth+1,inside)
def glow_paint(g,segs,core='h',mid='l',far='m',halo=0.8,halo_key='z'):
    # pale-yellow core instead of white (the user asked for less white); yellow and gold towards the tips
    for y in range(16):
        for x in range(16):
            best=None
            for a,b,w,dep in segs:
                d=seg(x+.5,y+.5,a,b)-w*0.5
                if best is None or d<best[0]: best=(d,dep,w)
            d,dep,w=best
            if d<0.05: g[y][x]=core if dep==0 and w>0.6 else mid if dep<=1 else far
            elif d<halo and g[y][x]=='.': g[y][x]=halo_key
def scar_lichtenberg():
    # Lichtenberg figure: fine branches fanning out from a white-hot strike point, cyan halo, sparks at the tips
    g=blank(); segs=[]
    # kept to a rounded area around the strike point so the fissures stay narrow and never reach the corners
    reach=lambda x,y: (x-8.2)**2/5.6**2+(y-7.6)**2/5.0**2<=1
    for k,ang in enumerate((-2.4,-0.6,0.9,2.6)):
        fractal((8.2,7.6),ang,7,1.1,11+k*5,segs,inside=reach)
    glow_paint(g,segs)
    g[7][8]='w'; g[7][7]='h'; g[8][8]='h'   # a single white-hot pixel at the strike point
    tips=[b for a,b,w,dep in segs if w<0.45]
    for i,(x,y) in enumerate(tips[::5]):
        xi,yi=int(x),int(y)
        if 0<=xi<16 and 0<=yi<16 and g[yi][xi] in '.z': g[yi][xi]='c' if i%2 else 'C'
    return rows(g)
FINAL=scar_lichtenberg
