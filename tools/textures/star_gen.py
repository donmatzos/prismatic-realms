import math
PAL={".":None,"o":"#164552","d":"#2a8592","m":"#4fc4c4","l":"#8be8dc","h":"#d2fff6","w":"#ffffff"}
LIGHT=(-0.6,-0.8)  # light from upper-left (y down)
def star(cx,cy,r0,R,wmax,peak,core=None,ridge=0.5,seam=False):
    g=[['.']*16 for _ in range(16)]; sec=[[None]*16 for _ in range(16)]
    for y in range(16):
        for x in range(16):
            px,py=x+.5-cx,y+.5-cy
            inside=False
            for k in range(5):
                a=math.radians(-90+72*k); d=(math.cos(a),math.sin(a)); n=(-d[1],d[0])
                u=px*d[0]+py*d[1]; v=px*n[0]+py*n[1]
                if u<r0 or u>R: continue
                t=(u-r0)/(R-r0)
                base=0.0 if r0>0 else 0.55
                w=wmax*((base+(1-base)*t/peak) if t<peak else (1-t)/(1-peak))
                w=max(w,0.5)
                if abs(v)<=w+0.05: inside=True
            if not inside: continue
            # owner = arm whose axis is angularly nearest; shade that arm's two facets
            ang=math.degrees(math.atan2(py,px))+90
            k=round(ang/72)%5; a=math.radians(-90+72*k); d=(math.cos(a),math.sin(a)); n=(-d[1],d[0])
            u=px*d[0]+py*d[1]; v=px*n[0]+py*n[1]; t=(u-r0)/(R-r0)
            side=(n[0]*math.copysign(1,v),n[1]*math.copysign(1,v)); lit=side[0]*LIGHT[0]+side[1]*LIGHT[1]
            if abs(v)<=ridge: c='w' if 0.6<t<0.88 else 'h'
            elif lit>0.3: c='l'
            elif lit>-0.3: c='m'
            else: c='d'
            g[y][x]=c; sec[y][x]=k
            if core and math.hypot(px,py)<=core[0]: g[y][x]=core[1]
    if seam:
        for y in range(16):
            for x in range(16):
                if sec[y][x] is not None and g[y][x] not in 'wh':
                    for i,j in((1,0),(0,1)):
                        if 0<=y+j<16 and 0<=x+i<16 and sec[y+j][x+i] not in (None,sec[y][x]) and math.hypot(x+.5-cx,y+.5-cy)>1.5:
                            g[y][x]='d'
    for y in range(16):
        for x in range(16):
            if g[y][x]=='.' and any(0<=y+j<16 and 0<=x+i<16 and g[y+j][x+i] not in '.O' for i,j in((1,0),(-1,0),(0,1),(0,-1))):
                g[y][x]='O'
    return [''.join(r).replace('O','o') for r in g]
