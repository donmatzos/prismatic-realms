import math
# lightning palette: dark amber outline -> amber -> gold -> yellow -> pale yellow -> white, plus an electric cyan hint
PAL={".":None,"o":"#3e2a05","d":"#a86f08","m":"#e0a814","l":"#f8d838","h":"#fff3a0","w":"#ffffff","c":"#9ff0ff","C":"#4cc8e0"}
LIGHT=(-0.6,-0.8)
def blank(): return [['.']*16 for _ in range(16)]
def inpoly(x,y,pts):
    c=False
    for i in range(len(pts)):
        (x1,y1),(x2,y2)=pts[i],pts[i-1]
        if (y1>y)!=(y2>y) and x<(x2-x1)*(y-y1)/(y2-y1)+x1: c=not c
    return c
def seg(px,py,a,b):
    (x1,y1),(x2,y2)=a,b; dx,dy=x2-x1,y2-y1; t=max(0,min(1,((px-x1)*dx+(py-y1)*dy)/(dx*dx+dy*dy)))
    return math.hypot(px-x1-t*dx,py-y1-t*dy)
def area(pts): return sum(pts[i-1][0]*pts[i][1]-pts[i][0]*pts[i-1][1] for i in range(len(pts)))/2
def bevel(pts,core='h',spine='w',deep=1.5,spine_d=2.2):
    # bevelled crystal: each pixel takes the facet of its nearest edge (lit or shadowed), the inside glows
    g=blank(); ccw=area(pts)>0
    for y in range(16):
        for x in range(16):
            px,py=x+.5,y+.5
            if not inpoly(px,py,pts): continue
            best=None
            for i in range(len(pts)):
                a,b=pts[i-1],pts[i]; d=seg(px,py,a,b)
                if best is None or d<best[0]:
                    ex,ey=b[0]-a[0],b[1]-a[1]; L=math.hypot(ex,ey); n=(-ey/L,ex/L) if not ccw else (ey/L,-ex/L)
                    best=(d,n)
            d,n=best; lit=n[0]*LIGHT[0]+n[1]*LIGHT[1]
            if d>spine_d: c=spine
            elif d>deep: c=core
            else: c='l' if lit>0.2 else 'm' if lit>-0.35 else 'd'
            g[y][x]=c
    return g
def outline(g):
    for y in range(16):
        for x in range(16):
            if g[y][x]=='.' and any(0<=y+j<16 and 0<=x+i<16 and g[y+j][x+i] not in '.O' and g[y+j][x+i] not in 'cC' for i,j in((1,0),(-1,0),(0,1),(0,-1))):
                g[y][x]='O'
    return [''.join(r).replace('O','o') for r in g]
def put(g,pix):
    for x,y,c in pix:
        if g[y][x]=='.': g[y][x]=c
    return g
BOLT=[(7.6,0.6),(13.6,0.6),(10.4,6.0),(14.2,6.0),(4.4,15.4),(7.4,8.8),(3.0,8.8)]
def bolt_classic():
    # bold classic bolt, bevelled facets, white-hot core, a few cyan sparks off the bends
    g=bevel(BOLT,deep=1.3,spine_d=2.0)
    rows=outline(g); g=[list(r) for r in rows]
    return [''.join(r) for r in put(g,[(1,7,'c'),(2,11,'c'),(15,4,'c'),(12,10,'c'),(14,9,'C')])]

# --- storm fang shards: broken-off pieces of the bolt ---
def broken(pts,breaks,hot='w',warm='h',deep=1.2,spine_d=1.9,sparks=()):
    # bevelled piece whose snapped faces (breaks: list of point-index pairs) glow where the charge leaks out
    g=bevel(pts,deep=deep,spine_d=spine_d)
    for y in range(16):
        for x in range(16):
            if g[y][x]=='.': continue
            d=min(seg(x+.5,y+.5,pts[a],pts[b]) for a,b in breaks)
            if d<0.8: g[y][x]=hot
            elif d<1.5 and g[y][x] in 'dm': g[y][x]=warm
    rows=outline(g); g=[list(r) for r in rows]
    return [''.join(r) for r in put(g,sparks)]
def shard_chip():
    # chunky middle bend of the bolt, its jagged broken faces leaking cyan charge
    pts=[(4.6,3.2),(6.8,1.6),(8.6,3.0),(11.8,1.8),(9.6,6.6),(13.6,6.6),(11.0,12.6),(8.8,14.4),(7.2,12.8),(4.2,14.2),(6.4,9.4),(2.4,9.4)]
    return broken(pts,[(0,1),(1,2),(2,3),(6,7),(7,8),(8,9)],hot='c',warm='h',sparks=[(13,1,'c'),(2,14,'C'),(14,11,'c')])
