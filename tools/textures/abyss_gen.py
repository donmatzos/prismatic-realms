import math
# abyss palette: near-black indigo outline -> deep indigo -> cerulean -> pale ice-blue, plus bioluminescent teal and abyssal rock
PAL={".":None,"o":"#0a0a2a","a":"#141650","d":"#232a7c","m":"#3346a8","l":"#4f78d4","h":"#93b8f2","w":"#e2eeff",
     "g":"#6ff5dc","G":"#2fb8b0","r":"#26263a","R":"#40405a"}
DEEPER={"w":"h","h":"l","l":"m","m":"d","d":"a"}
def blank(): return [['.']*16 for _ in range(16)]
def hsh(x,y,s=0):
    n=(x*374761393+y*668265263+s*982451653)&0xffffffff; n=(n^(n>>13))*1274126177&0xffffffff
    return ((n^(n>>16))&0xffff)/65535
def outline(g):
    for y in range(16):
        for x in range(16):
            if g[y][x]=='.' and any(0<=y+j<16 and 0<=x+i<16 and g[y+j][x+i] not in '.O' for i,j in((1,0),(-1,0),(0,1),(0,-1))):
                g[y][x]='O'
    return [''.join(r).replace('O','o') for r in g]
def geode():
    # cracked-open abyssal geode: dark rock rind around a cavity lined with cerulean crystal teeth, glowing at its depths
    g=blank(); cx,cy=8.0,8.4
    for y in range(16):
        for x in range(16):
            px,py=x+.5-cx,y+.5-cy; r=math.hypot(px,py)/(1+0.06*math.sin(math.atan2(py,px)*5)); th=math.atan2(py,px)
            if r>7.0: continue
            if r>5.3: g[y][x]='R' if (px+py<0) else 'r'; continue
            tooth=(math.degrees(th)%40)/40; edge=5.3-2.2*(1-abs(tooth-0.5)*2)
            if r>edge:
                lit=-0.6*math.cos(th)-0.8*math.sin(th)
                g[y][x]=('l' if tooth<0.5 else 'm') if lit<0 else ('h' if tooth<0.5 else 'l')
                if r>edge+1.4 and lit>=0: g[y][x]='m'
            elif r>1.9: g[y][x]='d' if r>2.9 else 'a'
            else: g[y][x]='a'
    for x,y,c in ((7,8,'g'),(8,9,'G'),(9,7,'G'),(6,10,'g')): g[y][x]=c
    return outline(g)

# --- cerulean abyss shards: pieces broken out of the geode ---
def geode_px(px,py,k=1.0):
    # the geode's material at a point relative to its center, at k times the crystal's size (None outside / in the core void)
    r=math.hypot(px,py)/k/(1+0.06*math.sin(math.atan2(py,px)*5)); th=math.atan2(py,px)
    if r>7.0: return None
    if r>5.3: return 'R' if px+py<0 else 'r'
    tooth=(math.degrees(th)%(40/k))/(40/k); edge=5.3-2.2*(1-abs(tooth-0.5)*2)
    if r>edge:
        lit=-0.6*math.cos(th)-0.8*math.sin(th)
        c=('l' if tooth<0.5 else 'm') if lit<0 else ('h' if tooth<0.5 else 'l')
        return 'm' if r>edge+1.4 and lit>=0 else c
    return 'd' if r>2.9 else 'a'
def shard_wedge():
    # pie-slice wedge of the geode: rock rind on its curved back, crystal teeth inside, glowing core at the broken point
    g=blank(); cx,cy=12.6,12.6
    for y in range(16):
        for x in range(16):
            px,py=x+.5-cx,y+.5-cy; r=math.hypot(px,py); a=math.degrees(math.atan2(py,px))%360
            lo=188+5*math.sin(r*1.7); hi=272+5*math.sin(r*1.3+1)   # jagged break lines
            if not lo<=a<=hi or r<1.2: continue
            c=geode_px(px,py,1.55)
            if c: g[y][x]=c
    for x,y,c in ((11,11,'g'),(10,11,'G'),(11,10,'G'),(9,10,'g')): g[y][x]=c
    return [r[::-1] for r in outline(g)]   # mirrored horizontally at the user's request
