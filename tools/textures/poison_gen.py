import math
from bolt_gen import bevel, seg  # facet shading (o/d/m/l/h/w ramp keys)
# poison palette: near-black plum outline -> deep violet -> amethyst -> orchid -> pale lilac, plus an acid-green toxic accent
PAL={".":None,"o":"#1f0a2e","a":"#2e1047","d":"#4a1a6e","m":"#7a2fa8","l":"#a95bd6","h":"#d7a3f2","w":"#f6e8ff",
     "v":"#b6f23c","V":"#5fb820"}
def blank(): return [['.']*16 for _ in range(16)]
def outline(g):
    for y in range(16):
        for x in range(16):
            if g[y][x]=='.' and any(0<=y+j<16 and 0<=x+i<16 and g[y+j][x+i] not in '.O' for i,j in((1,0),(-1,0),(0,1),(0,-1))):
                g[y][x]='O'
    return [''.join(r).replace('O','o') for r in g]
LIGHT=(-0.6,-0.8)
def blob_pentagon(cx,cy,radii,bulge,rot=-90):
    # pentagon with uneven points and softly bulging sides; returns (outline points, corner points)
    corners=[(cx+r*math.cos(math.radians(rot+72*k)),cy+r*math.sin(math.radians(rot+72*k))) for k,r in enumerate(radii)]
    pts=[]
    for k in range(5):
        (x1,y1),(x2,y2)=corners[k],corners[(k+1)%5]
        mx,my=(x1+x2)/2-cx,(y1+y2)/2-cy; ml=math.hypot(mx,my)
        for i in range(6):
            t=i/6; b=bulge[k]*math.sin(math.pi*t)
            pts.append((x1+(x2-x1)*t+mx/ml*b, y1+(y2-y1)*t+my/ml*b))
    return pts,corners
def pentagon_body(g,cx,cy,radii,bulge,rot,cracks):
    # faceted organic pentagon (five facets meeting at the center, bright ridges between them), drawn into g at any size and rotation
    from nature_gen import inpoly
    pts,corners=blob_pentagon(cx,cy,radii,bulge,rot)
    for y in range(16):
        for x in range(16):
            px,py=x+.5,y+.5
            if not inpoly(px,py,pts): continue
            a=(math.degrees(math.atan2(py-cy,px-cx))-rot)%360; k=int(a//72)
            (x1,y1),(x2,y2)=corners[k],corners[(k+1)%5]
            nx,ny=(x1+x2)/2-cx,(y1+y2)/2-cy; nl=math.hypot(nx,ny)
            lit=(nx*LIGHT[0]+ny*LIGHT[1])/nl
            c='h' if lit>0.55 else 'l' if lit>0.05 else 'm' if lit>-0.6 else 'd'
            if ((a%72)<7 or (a%72)>65) and math.hypot(px-cx,py-cy)>1.2: c={'h':'w','l':'h','m':'l','d':'m'}[c]
            g[y][x]=c
            if min(seg(px,py,a2,b2) for a2,b2 in cracks)<0.45: g[y][x]='v'
def crown(spikes,body,cracks,tips=(),bubbles=(),glint=()):
    # crown of small crystal spikes rising from the flat top of the pentagon body
    g=blank()
    for pts in spikes:
        part=bevel(pts,deep=0.9,spine_d=9)
        for y in range(16):
            for x in range(16):
                if part[y][x]!='.': g[y][x]=part[y][x]
    for y in range(16):
        for x in range(16):
            if g[y][x]!='.' and min(seg(x+.5,y+.5,a,b) for a,b in cracks)<0.45: g[y][x]='v'
    pentagon_body(g,*body,cracks)
    for x,y,c in list(tips)+list(glint): g[y][x]=c
    rows=[list(r) for r in outline(g)]
    for x,y,c in bubbles: rows[y][x]=c
    return [''.join(r) for r in rows]
BODY=(8.0,10.2,[5.9,5.6,5.8,5.5,5.7],[0.45,0.55,0.35,0.5,0.6],-54)   # flat side on top
BODY_CRACKS=[((7.4,11.0),(5.8,13.0)),((8.6,11.2),(10.2,13.4)),((8.8,9.4),(10.2,7.8))]
def crown_b():
    # outer spikes splayed outwards like a crown, a crack running up into the middle spike
    return crown([[(6.6,6.4),(8.0,1.0),(9.4,6.4)],[(3.4,7.4),(2.2,2.6),(6.0,6.2)],[(10.0,6.2),(13.8,2.6),(12.6,7.4)]],
                 BODY,BODY_CRACKS+[((8.8,9.4),(8.2,3.4))],bubbles=[(1,0,'V'),(15,4,'v'),(5,1,'v')],glint=[(6,9,'w')])

# --- venom crown shards: broken pieces of the crowned pentagon, venom glowing along the breaks ---
def clip(g,keep):
    for y in range(16):
        for x in range(16):
            if g[y][x]!='.' and not keep(x+.5,y+.5): g[y][x]='.'
def glow_breaks(g,breaks,width=0.6):
    for y in range(16):
        for x in range(16):
            if g[y][x]!='.' and min(seg(x+.5,y+.5,a,b) for a,b in breaks)<width: g[y][x]='v'
def shard_facet():
    # a single triangular facet chipped out of the pentagon, its broken sides leaking venom
    from nature_gen import inpoly
    g=blank(); pentagon_body(g,4.0,13.0,[13.0,12.4,12.8,12.2,12.6],[0.45,0.55,0.35,0.5,0.6],-54,[((7.6,9.6),(10.4,7.0))])
    tri=[(2.4,13.8),(6.4,1.6),(14.2,7.2)]
    jag=[(2.4,13.8),(4.0,9.6),(3.6,6.6),(6.4,1.6),(14.2,7.2),(10.6,9.0),(9.6,11.4),(6.2,12.0)]
    clip(g,lambda px,py: inpoly(px,py,jag))
    glow_breaks(g,[(jag[i],jag[i+1]) for i in (0,1,2)]+[(jag[i],jag[(i+1)%8]) for i in (4,5,6,7)],0.5)
    g[4][7]='w'; g[5][7]='h'
    rows=[list(r) for r in outline(g)]
    for x,y,c in ((1,3,'v'),(13,1,'V')): rows[y][x]=c
    return [''.join(r) for r in rows]
