import math
# pastel rainbow (red, orange, yellow, green, blue, indigo, violet) + soft lavender body ramp
PAL={".":None,"o":"#4e4866","d":"#8a82a8","m":"#bdb6d4","l":"#dedaee","h":"#f3f1fa","w":"#ffffff",
     "1":"#f3a9a9","2":"#f5c79c","3":"#f1e59a","4":"#b3e3ab","5":"#a9cdf2","6":"#b2b0ee","7":"#d6b0ee"}
RB="1234567"
def blank(): return [['.']*16 for _ in range(16)]
def outline(g):
    for y in range(16):
        for x in range(16):
            if g[y][x]=='.' and any(0<=y+j<16 and 0<=x+i<16 and g[y+j][x+i] not in '.O' for i,j in((1,0),(-1,0),(0,1),(0,-1))):
                g[y][x]='O'
    return [''.join(r).replace('O','o') for r in g]
def shade(px,py,r):  # sphere shading, light upper-left
    nz=math.sqrt(max(0,1-(px*px+py*py)/(r*r))); nx,ny=px/r,py/r
    L=(-0.5,-0.6,0.62); s=nx*L[0]+ny*L[1]+nz*L[2]
    return 'h' if s>0.85 else 'l' if s>0.55 else 'm' if s>0.15 else 'd'
def orb():
    # glowing nucleus inside a pale shell, with the seven colours orbiting as loose motes
    g=blank(); cx=cy=8.0; R=4.9
    for y in range(16):
        for x in range(16):
            px,py=x+.5-cx,y+.5-cy; r=math.hypot(px,py)
            if r<=R: g[y][x]=shade(px,py,R)
            if r<=1.5: g[y][x]='w'
            elif r<=2.3 and g[y][x] in 'dm': g[y][x]='l'
    g[5][6]='w'
    rows=outline(g); g=[list(r) for r in rows]
    for k in range(7):
        a=math.radians(-100+k*360/7); x=int(round(cx-.5+6.6*math.cos(a))); y=int(round(cy-.5+6.6*math.sin(a)))
        g[y][x]=RB[k]
    return [''.join(r) for r in g]
def gyro():
    # core held inside a ring, with the seven colours set into the ring like inlaid stones
    g=blank(); cx=cy=8.0
    for y in range(16):
        for x in range(16):
            px,py=x+.5-cx,y+.5-cy; r=math.hypot(px,py)
            if 5.3<=r<=7.0: g[y][x]='l' if px+py<-1 else 'm' if px+py<3 else 'd'
            if r<=2.5: g[y][x]="w" if r<=1.2 else shade(px,py,2.5)
    for k in range(7):
        ang=math.radians(-90+k*360/7); x=int(cx+6.0*math.cos(ang)); y=int(cy+6.0*math.sin(ang)); g[y][x]=RB[k]
    return outline(g)
def gem():
    # octahedral core: four facets lit from the upper left, a faint rainbow refracted along the girdle
    g=blank(); cx,cy=8.0,8.0
    for y in range(16):
        for x in range(16):
            px,py=x+.5-cx,y+.5-cy
            if abs(px)/5.6+abs(py)/7.4>1: continue
            g[y][x]={(True,True):'h',(True,False):'l',(False,True):'m',(False,False):'d'}[(py<0,px<0)]
            if math.hypot(px,py)<=1.5: g[y][x]='w'
    xs=[x for x in range(16) if g[7][x]!='.' and g[7][x]!='w']+[x for x in range(16) if g[8][x]!='.' and g[8][x]!='w']
    row7=[x for x in range(16) if g[7][x] not in '.w']; row8=[x for x in range(16) if g[8][x] not in '.w']
    seq=row7  # spread the seven colours along the girdle, skipping the glowing centre
    cols=[RB[min(6,int(i*7/len(seq)))] for i in range(len(seq))]
    for x,c in zip(seq,cols): g[8 if x>=8 else 7][x]=c
    return outline(g)
