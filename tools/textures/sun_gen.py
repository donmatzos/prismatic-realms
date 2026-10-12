import math
# molten palette: outline -> crust -> deep red -> red-orange -> orange -> gold -> pale yellow -> white
PAL={".":None,"o":"#3d1206","k":"#5a2a1e","r":"#9c2a0e","e":"#d6501a","n":"#f28a22","g":"#ffc23c","y":"#fff0a0","w":"#ffffff"}
RAMP="rengyw"  # dark -> bright
def blank(): return [['.']*16 for _ in range(16)]
def h(x,y,s=0):
    n=(x*374761393+y*668265263+s*982451653)&0xffffffff; n=(n^(n>>13))*1274126177&0xffffffff
    return ((n^(n>>16))&0xffff)/65535
def vnoise(x,y,s):
    xi,yi=math.floor(x),math.floor(y); xf,yf=x-xi,y-yi
    sm=lambda t:t*t*(3-2*t); u,v=sm(xf),sm(yf)
    a,b,c,d=h(xi,yi,s),h(xi+1,yi,s),h(xi,yi+1,s),h(xi+1,yi+1,s)
    return a+(b-a)*u+(c-a)*v+(a-b-c+d)*u*v
def outline(g):
    for y in range(16):
        for x in range(16):
            if g[y][x]=='.' and any(0<=y+j<16 and 0<=x+i<16 and g[y+j][x+i] not in '.O' for i,j in((1,0),(-1,0),(0,1),(0,-1))):
                g[y][x]='O'
    return [''.join(r).replace('O','o') for r in g]
def lit(px,py,R):  # 0..1 sphere brightness, light from upper-left
    nz=math.sqrt(max(0,1-(px*px+py*py)/(R*R))); return max(0,(-0.45*px/R-0.55*py/R+0.7*nz))
def molten():
    # churning lava orb: swirling noise bands over sphere shading, small flares on the rim
    g=blank(); cx=cy=8.0; R=6.0
    for y in range(16):
        for x in range(16):
            px,py=x+.5-cx,y+.5-cy; r=math.hypot(px,py)
            if r>R: continue
            a=math.atan2(py,px)
            swirl=vnoise(px*0.8+math.cos(a*2+r*0.9)*1.2, py*0.8+math.sin(a*2+r*0.9)*1.2, 3)
            b=0.38*lit(px,py,R)+0.75*swirl-0.12
            g[y][x]=RAMP[max(0,min(4,int(b*6)))]
    for x,y,c in ((8,1,'n'),(1,7,'e'),(14,9,'e'),(4,13,'r'),(12,3,'g')):
        g[y][x]=c
    g[4][5]='w'; g[4][6]='y'; g[5][5]='y'  # specular glint
    return outline(g)
def magma():
    # cooled crust plates split by glowing seams, molten light leaking through
    g=blank(); cx=cy=8.0; R=6.4
    seeds=[(-3.2,-3.0),(1.8,-3.8),(4.2,0.6),(0.4,1.2),(-3.8,1.6),(2.2,4.6),(-1.6,5.0),(-0.4,-0.9)]
    for y in range(16):
        for x in range(16):
            px,py=x+.5-cx,y+.5-cy
            if math.hypot(px,py)>R: continue
            d=sorted(math.hypot(px-sx,py-sy) for sx,sy in seeds); gap=d[1]-d[0]
            L=lit(px,py,R)
            if gap<0.75: g[y][x]='y' if L>0.55 else 'g' if L>0.25 else 'n'
            elif gap<1.5 and L>0.2: g[y][x]='e'
            else: g[y][x]='r' if L>0.6 else 'k'
    return outline(g)
def corona():
    # blazing pocket sun: hot white-gold core fading outwards, with flame prominences around the rim
    g=blank(); cx=cy=8.0; R=4.6
    for y in range(16):
        for x in range(16):
            px,py=x+.5-cx,y+.5-cy; r=math.hypot(px,py); a=math.atan2(py,px)
            flare=R+1.2+1.4*max(0,math.sin(a*8+0.6))*vnoise(math.cos(a)*3,math.sin(a)*3,7)*1.6
            if r<=R:
                b=1.0-r/R*0.75+0.25*lit(px,py,R)-0.15*vnoise(px*0.8,py*0.8,5)
                g[y][x]=RAMP[max(1,min(5,int(b*6)))]
            elif r<=flare:
                g[y][x]='n' if r<R+0.9 else 'e' if r<flare-0.6 else 'r'
    return outline(g)

# --- cruel sun shards: lava fragments in the churning-lava style of molten() ---
def inpoly(x,y,pts):
    c=False
    for i in range(len(pts)):
        (x1,y1),(x2,y2)=pts[i],pts[i-1]
        if (y1>y)!=(y2>y) and x<(x2-x1)*(y-y1)/(y2-y1)+x1: c=not c
    return c
def arc(cx,cy,R,a0,a1,n=8):
    return [(cx+R*math.cos(math.radians(a0+(a1-a0)*i/n)),cy+R*math.sin(math.radians(a0+(a1-a0)*i/n))) for i in range(n+1)]
def lava_fill(mask,seed=3,glint=None):
    # swirl noise lit from the upper left; pixels on the rim drop one step for volume
    g=blank()
    for y in range(16):
        for x in range(16):
            if not mask(x+.5,y+.5): continue
            swirl=vnoise(x*0.75+math.sin(y*0.6)*1.3,y*0.75+math.cos(x*0.6)*1.3,seed)
            b=0.4*(1-(x+y)/30)+0.75*swirl-0.1
            g[y][x]=min(4,max(0,int(b*6)))
    for y in range(16):
        for x in range(16):
            if g[y][x]!='.' and any(not(0<=y+j<16 and 0<=x+i<16) or g[y+j][x+i]=='.' for i,j in((1,0),(-1,0),(0,1),(0,-1))) and (x+y)>14:
                g[y][x]=max(0,g[y][x]-1)
    g=[[RAMP[c] if c!='.' else '.' for c in row] for row in g]
    for x,y,c in (glint or []): g[y][x]=c
    return outline(g)
def shard_chunk():
    # wedge broken off the orb: curved crust outside, glowing molten core exposed along the break
    cut=[(12.6,2.6),(11.0,5.8),(12.4,8.4),(8.8,9.4),(8.6,12.4),(5.6,11.6),(2.8,12.8)]
    pts=arc(13.2,13.2,11.2,184,266)+cut
    g=[list(r) for r in lava_fill(lambda x,y:inpoly(x,y,pts),seed=3,glint=[(4,5,'w'),(5,5,'y'),(4,6,'y')])]
    def seg_d(px,py,a,b):
        (x1,y1),(x2,y2)=a,b; dx,dy=x2-x1,y2-y1; t=max(0,min(1,((px-x1)*dx+(py-y1)*dy)/(dx*dx+dy*dy)))
        return math.hypot(px-x1-t*dx,py-y1-t*dy)
    for y in range(16):
        for x in range(16):
            if g[y][x] in '.o': continue
            d=min(seg_d(x+.5,y+.5,cut[i],cut[i+1]) for i in range(len(cut)-1))
            if d<0.9: g[y][x]='y'
            elif d<1.8: g[y][x]='g'
    return [''.join(r) for r in g]
